package br.unb.cic.goda.rtgoretoprism.generator.goda.producer;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import br.unb.cic.goda.model.Actor;
import br.unb.cic.goda.model.Goal;
import br.unb.cic.goda.rtgoretoprism.generator.CodeGenerationException;
import br.unb.cic.goda.rtgoretoprism.generator.goda.parser.CostParser;
import br.unb.cic.goda.rtgoretoprism.generator.goda.writer.ManageWriter;
import br.unb.cic.goda.rtgoretoprism.generator.goda.writer.ParamWriter;
import br.unb.cic.goda.rtgoretoprism.generator.kl.AgentDefinition;
import br.unb.cic.goda.rtgoretoprism.model.kl.Const;
import br.unb.cic.goda.rtgoretoprism.model.kl.GoalContainer;
import br.unb.cic.goda.rtgoretoprism.model.kl.PlanContainer;
import br.unb.cic.goda.rtgoretoprism.model.kl.RTContainer;
import br.unb.cic.goda.rtgoretoprism.paramformula.SymbolicParamGenerator;
import br.unb.cic.goda.rtgoretoprism.paramwrapper.ParamWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class PARAMProducer {

	private String sourceFolder;
	private String targetFolder;
	private String toolsFolder;
	private Set<Actor> allActors;
	private Set<Goal> allGoals;
	private AgentDefinition ad;
	private boolean isParam;

	private String typeModel;
	private String agentName;
	private List<String> leavesId = new ArrayList<String>();
	private Map<String, String> ctxInformation = new HashMap<String, String>();
	private Map<String, String> varReliabilityInformation = new HashMap<String, String>();
	private Map<String, String> varCostInformation = new HashMap<String, String>();
	private Map<String, String> reliabilityByNode = new HashMap<String, String>();
	private FormulaTreeNode reliabilityFormulaTreeNode;
	private FormulaTreeNode costFormulaTreeNode;

	public class Formulas {
		public Formulas(String reliability, String cost) {
			this.reliability = reliability;
			this.cost  = cost;
			
		}
		public String reliability;
		public String cost;
	}

	public class FormulaTreeNode {
		public String id;
		public String formula;
		public List<FormulaTreeNode> subNodes = new LinkedList<FormulaTreeNode>();
	}
	
	public PARAMProducer(Set<Actor> allActors, Set<Goal> allGoals, boolean isParam, String in, String out,
			String tools, String typeModel) {

		this.sourceFolder = in;
		this.targetFolder = out;
		this.toolsFolder = tools;
		this.allActors = allActors;
		this.allGoals = allGoals;
		this.isParam = isParam;
		this.typeModel = typeModel;
	}

	public PARAMProducer(AgentDefinition ad, Set<Actor> selectedActors, Set<Goal> selectedGoals, String sourceFolder,
			String targetFolder, String toolsFolder, String typeModel) {
		this.sourceFolder = sourceFolder;
		this.targetFolder = targetFolder;
		this.toolsFolder = toolsFolder;
		this.allActors = selectedActors;
		this.allGoals = selectedGoals;
		this.ad = ad;
		this.agentName = "EvaluationActor";
		this.typeModel = typeModel;
	}

	public void run() throws Exception {

		for (Actor actor : allActors) {
			long startTime = 0;
			
			System.out.println("Generating parametric formulas for: " + agentName);

			// Compose goal formula
			startTime = new Date().getTime();
			
			Formulas formulas  = generateFormulas(actor);

			// Print formula
			printFormula(formulas.reliability, formulas.cost);
			System.out.println("Parametric formulas created in " + (new Date().getTime() - startTime) + "ms.");
		}
	}

	private FormulaTreeNode generateFormulaTree(RTContainer rootNode) {
		FormulaTreeNode node = new FormulaTreeNode();
		LinkedList<GoalContainer> decompGoal = removeDuplicates(rootNode.getDecompGoals());
		LinkedList<PlanContainer> decompPlans = removeDuplicates(rootNode.getDecompPlans());

		if (rootNode instanceof GoalContainer) {
			node.id = rootNode.getClearUId();
		} else {
			node.id = rootNode.getClearElId();
		}

		node.formula = rootNode.getFormula();

		for (GoalContainer subNode : decompGoal) {
			FormulaTreeNode sn = generateFormulaTree(subNode);
			node.subNodes.add(sn);
		}

		/* Run for sub tasks */
		for (PlanContainer subNode : decompPlans) {
			FormulaTreeNode sn = generateFormulaTree(subNode);
			node.subNodes.add(sn);
		}

		return node;
	}
	
	public Formulas generateFormulas(Actor actor) throws Exception {
		
		if (this.ad == null) {
			RTGoreProducer producer = new RTGoreProducer(allActors, allGoals, sourceFolder, targetFolder, this.typeModel);
			AgentDefinition ad = producer.run();

			this.ad = ad;
			agentName = ad.getAgentName();
		}

		
		String reliabilityForm = composeNodeForm(ad.rootlist.getFirst(), true);
		ad.rootlist.getFirst().setFormula(reliabilityForm);
		reliabilityFormulaTreeNode = generateFormulaTree(this.ad.rootlist.getFirst());

		String costForm = composeNodeForm(ad.rootlist.getFirst(), false);
		ad.rootlist.getFirst().setFormula(costForm);
		costFormulaTreeNode = generateFormulaTree(this.ad.rootlist.getFirst());

		reliabilityForm = cleanNodeForm(reliabilityForm, true);
		costForm = cleanNodeForm(costForm, false);

		return new Formulas(reliabilityForm, costForm);
	}

	private String cleanNodeForm(String nodeForm, boolean reliability) {

		if (!reliability) {
			nodeForm = replaceReliabilites(nodeForm);
		}

		Map<String, String> mapAux = new HashMap<String, String>();
		for (String ctxKey : this.ctxInformation.keySet()) {
			if (nodeForm.contains("CTX_" + ctxKey)) {
				mapAux.put(ctxKey, this.ctxInformation.get(ctxKey));
			}
		}
		this.ctxInformation = mapAux;

		nodeForm = nodeForm.replaceAll("\\s+", "");
		return nodeForm;
	}

	private String replaceReliabilites(String nodeForm) {
		if (nodeForm.contains(" R_")) {
			for (Map.Entry<String, String> entry : this.reliabilityByNode.entrySet()) {
				String id = entry.getKey();
				if (nodeForm.contains("R_" + id)) {
					String reliability = entry.getValue();
					nodeForm = nodeForm.replaceAll(" R_" + id + " ", " " + reliability + " ");
				}
			}
		}
		return nodeForm;
	}

	private void printFormula(String reliabilityForm, String costForm) throws CodeGenerationException {

		reliabilityForm = composeFormula(reliabilityForm, true);
		costForm = composeFormula(costForm, false);

		String evalForm = composeEvalFormula();

		String output = targetFolder + "/";

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		long id = timestamp.getTime();

		PrintWriter reliabilityFormulaWriter = ManageWriter
				.createFile(agentName + "_" + id + "_reliability.json", "resources/reliability/");

		PrintWriter costFormulaWriter = ManageWriter
				.createFile(agentName + "_" + id + "_cost.json", "resources/cost/");

		PrintWriter reliabilityFormula = ManageWriter.createFile("reliability.out", output);
		PrintWriter costFormula = ManageWriter.createFile("cost.out", output);
		PrintWriter evalBashFile = ManageWriter.createFile("eval_formula.sh", output);
		PrintWriter reliabilityFormulaTree = ManageWriter.createFile("reliability_tree.json", output);
		PrintWriter costFormulaTree = ManageWriter.createFile("cost_tree.json", output);
		PrintWriter formulasId = ManageWriter.createFile("formulas_id.out", output);

		String reliabilityJson = "";
		String costJson = "";

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
			reliabilityJson = objectMapper.writeValueAsString(this.reliabilityFormulaTreeNode);
			costJson = objectMapper.writeValueAsString(this.costFormulaTreeNode);
		} catch (Exception e) {
			System.out.println("Was not possible to parse reliability json or cost json. Exception " + e);
			return;
		}

		ManageWriter.printModel(reliabilityFormulaTree, reliabilityJson);
		ManageWriter.printModel(costFormulaTree, costJson);
		ManageWriter.printModel(reliabilityFormula, reliabilityForm);
		ManageWriter.printModel(costFormula, costForm);
		ManageWriter.printModel(evalBashFile, evalForm);
		ManageWriter.printModel(reliabilityFormulaWriter, reliabilityJson);
		ManageWriter.printModel(costFormulaWriter, costJson);
		ManageWriter.printModel(formulasId, agentName + "_" + id);
	}

	private String composeEvalFormula() throws CodeGenerationException {
		String evalFormulaParams = new String();
		String evalFormulaReplace = new String();
		for (String ctxKey : this.ctxInformation.keySet()) {
			evalFormulaParams += "CTX_" + ctxKey + "=\"1\";\n";
			evalFormulaReplace += " -e \"s/CTX_" + ctxKey + "/$CTX_" + ctxKey + "/g\"";
		}
		for (String var : this.varReliabilityInformation.keySet()) {
			String value = this.varReliabilityInformation.get(var);
			if (value.contains("OPT_")) {
				evalFormulaParams += "OPT_" + var + "=\"1\";\n";
				evalFormulaReplace += " -e \"s/OPT_" + var + "/$OPT_" + var + "/g\"";
			} else {
				evalFormulaParams += "R_" + var + "=\"0.99\";\n";
				evalFormulaReplace += " -e \"s/R_" + var + "/$R_" + var + "/g\"";
			}
		}
		for (String var : this.varCostInformation.keySet()) {
			evalFormulaParams += "W_" + var + "=\"1\";\n";
			evalFormulaReplace += " -e \"s/W_" + var + "/$W_" + var + "/g\"";
		}

		String evalBash = ManageWriter.readFileAsString(sourceFolder + "/PARAM/" + "eval_formula.sh");

		evalBash = evalBash.replace("$PARAMS_BASH$", evalFormulaParams);
		evalBash = evalBash.replace("$REPLACE_BASH$", evalFormulaReplace);

		return evalBash;
	}

	private String composeFormula(String nodeForm, boolean isReliability) throws CodeGenerationException {

		String body = nodeForm + "\n\n";
		for (String ctxKey : ctxInformation.keySet()) {
			body = body + "//CTX_" + ctxKey + " = " + ctxInformation.get(ctxKey) + "\n";
		}
		for (String var : this.varReliabilityInformation.keySet()) {
			body = body + this.varReliabilityInformation.get(var);
		}
		if (!isReliability) {
			for (String var : this.varCostInformation.keySet()) {
				body = body + this.varCostInformation.get(var);
			}
		}

		return body;
	}

	// true: compose reliability, false: compose cost
	private String composeNodeForm(RTContainer rootNode, boolean reliability) throws Exception {

		Const decType;
		String rtAnnot;
		String nodeForm;
		String nodeId;
		LinkedList<GoalContainer> decompGoal = new LinkedList<GoalContainer>();
		LinkedList<PlanContainer> decompPlans = new LinkedList<PlanContainer>();

		if (rootNode instanceof GoalContainer)
			nodeId = rootNode.getClearUId();
		else
			nodeId = rootNode.getClearElId();

		decompGoal = (LinkedList<GoalContainer>) removeDuplicates(rootNode.getDecompGoals());
		decompPlans = (LinkedList<PlanContainer>) removeDuplicates(rootNode.getDecompPlans());
		decType = rootNode.getDecomposition();
		rtAnnot = rootNode.getRtRegex();

		if (!decompGoal.isEmpty() || !decompPlans.isEmpty()) {
			setContextList(decompGoal, decompPlans);
		}

		nodeForm = getNodeForm(decType, rtAnnot, nodeId, reliability, rootNode);

		/* Run for sub goals */
		for (GoalContainer subNode : decompGoal) {
			String subNodeId = subNode.getClearUId();
			String subNodeForm = composeNodeForm(subNode, reliability);
			subNode.setFormula(subNodeForm);

			nodeForm = replaceSubForm(nodeForm, subNodeForm, nodeId, subNodeId, reliability);
		}

		/* Run for sub tasks */
		for (PlanContainer subNode : decompPlans) {
			String subNodeId = subNode.getClearElId();
			String subNodeForm = composeNodeForm(subNode, reliability);
			subNode.setFormula(subNodeForm);
			nodeForm = replaceSubForm(nodeForm, subNodeForm, nodeId, subNodeId, reliability);
		}

		/* If leaf task */
		if ((decompGoal.size() == 0) && (decompPlans.size() == 0)) {

			this.leavesId.add(nodeId);

			if (reliability) {
				// Create DTMC model (param)
				//Comentando esse trecho baseado no codigo da branch master do Gabriel Rodrigues
				ParamWriter writer = new ParamWriter(sourceFolder, nodeId);
				String model = writer.writeModel();

				// Call to param (reliability)
				ParamWrapper paramWrapper = new ParamWrapper(toolsFolder, nodeId);
				nodeForm = paramWrapper.getFormula(model);
				if(nodeForm.length()> 0) {
					nodeForm = nodeForm.replaceFirst("1\\*", "");
				} else {
					nodeForm = nodeId;
					System.err.println("Formula for node " + nodeId + " was not resolved. Using nodeId");
				}
					

				this.varReliabilityInformation.put(nodeId, "//R_" + nodeId + " = reliability of node " + nodeId + "\n");
				if (rootNode.isOptional()) {
					nodeForm += "*OPT_" + nodeId;
					this.varReliabilityInformation.put(nodeId,
							"//OPT_" + nodeId + " = optionality of node " + nodeId + "\n");
				}
			} else {
				// Cost
				nodeForm = getCostFormula(rootNode);
				this.varCostInformation.put(nodeId, "//" + nodeForm + " = cost of node " + nodeId + "\n");
			}
		}
		if (reliability)
			this.reliabilityByNode.put(nodeId, nodeForm);

		return nodeForm;
	}

	private String getNodeForm(Const decType, String rtAnnot, String nodeId, boolean reliability, RTContainer rootNode)
			throws Exception {

		if (rtAnnot == null) {
			return nodeId;
		}

		StringBuilder formula = new StringBuilder();
		SymbolicParamGenerator symbolic = new SymbolicParamGenerator();

		if (rtAnnot.contains(";")) { // Sequential
			String[] ids = getChildrenId(rootNode);

			if (reliability) {
				// Reliability formula
				if (decType.equals(Const.AND) || decType.equals(Const.ME)) { // Sequential AND
					formula = symbolic.getAndReliability(ids, this.ctxInformation);
				} else { // Sequential OR
					formula = symbolic.getOrReliability(ids, this.ctxInformation);
				}
			} else {
				// Cost formula
				if (decType.equals(Const.AND) || decType.equals(Const.ME)) { // Sequential AND
					formula = symbolic.getSequentialAndCost(ids, nodeId, this.ctxInformation, this.isParam);
				} else { // Sequential OR
					formula = symbolic.getSequentialOrCost(ids, nodeId, this.ctxInformation, this.isParam);
				}
			}

			return formula.toString();
		} else if (rtAnnot.contains("#")) { // Parallel
			String[] ids = getChildrenId(rootNode);

			if (reliability) {
				// Reliability formula
				if (decType.equals(Const.AND) || decType.equals(Const.ME)) { // Parallel AND
					formula = symbolic.getAndReliability(ids, this.ctxInformation);
				} else { // Parallel OR
					formula = symbolic.getOrReliability(ids, this.ctxInformation);
				}
			} else {
				// Cost formula
				if (decType.equals(Const.AND) || decType.equals(Const.ME)) { // Parallel AND
					formula = symbolic.getParallelAndCost(ids, nodeId, this.ctxInformation, this.isParam);
				} else { // Parallel OR
					formula = symbolic.getParallelOrCost(ids, nodeId, this.ctxInformation, this.isParam);
				}
			}

			return formula.toString();
		} else if (rtAnnot.contains("DM")) {
			String[] ids = getChildrenId(rootNode);
			if (reliability) {
				formula = symbolic.getDMReliability(ids, this.ctxInformation);
			} else {
				formula = symbolic.getDMCost(ids, ctxInformation, this.isParam);
			}
		} else if (rtAnnot.contains("@")) {
			String[] ids = getChildrenId(rootNode);
			int retryNum = Integer.parseInt(rtAnnot.substring(rtAnnot.indexOf("@") + 1));

			if (reliability) {
				formula = symbolic.getRetryReliability(ids, nodeId, this.ctxInformation, this.isParam, retryNum);
			} else {
				formula = symbolic.getRetryCost(ids, nodeId, this.ctxInformation, this.isParam, retryNum);
			}
			return formula.toString();
		} else if (rtAnnot.contains("try")) {
			String[] ids = getChildrenId(rootNode);
			
			if(rtAnnot.contains("?skip:")) { //try(a)?b:skip
				ids = new String[] {ids[0], "skip", ids[1]};
			}else if(rtAnnot.contains(":skip")) {//try(a)?skip:b
				ids = new String[] {ids[0], ids[1], "skip"};
			} 
			
			if (reliability) {
				formula = symbolic.getTryReliability(ids, this.ctxInformation, this.isParam);
			} else {
				formula = symbolic.getTryCost(ids, this.ctxInformation, this.isParam);
			}
			return formula.toString();
		} else {
			return nodeId;
		}

		return formula.toString();
	}
	
	static <E> LinkedList<E> removeDuplicates(LinkedList<E> list) {
		List<E> nlist = list.stream().distinct().collect(Collectors.toList());
		return new LinkedList<E>(nlist);
	}

//private String getNodeForm(Const decType, String dmAnnot, String nodeId, boolean reliability, RTContainer rootNode) throws Exception {
//		
//		List<String> childrenNodes = getChildrenId(rootNode);
//		StringBuilder formula = new StringBuilder();
//		
//		//TO-DO: Define the formulae templates outside this method (improve legibility)
//		
//		if (dmAnnot == null) {
//			if (childrenNodes.size() <= 1) return nodeId;
//		
//			//AND-Decomposition
//			if (decType.equals(Const.AND)) {
//				if (reliability) {
//					formula.append("( ");
//					for (String id : childrenNodes) {
//						//Children is context-dependent
//						if (this.ctxInformation.containsKey(id)) {
//							formula.append(" CTX_" + id + " * " + id + " * ");
//						}
//						//Children is context-free
//						else {
//							formula.append(id + " * ");
//						}
//					}
//					formula.delete(formula.lastIndexOf("*"), formula.length()-1);
//					formula.append(")");
//				}
//				else {
//					
//					SymbolicParamAndGenerator param = new SymbolicParamAndGenerator();
//					formula = param.getSequentialAndCost((String[]) childrenNodes.toArray(new String[0]), this.ctxInformation);
//					formula.append(" * R_" + nodeId + " ");
//				}
//			}
//			//OR-Decomposition
//			else {
//				if (this.ctxInformation.containsKey(childrenNodes.get(0)) && this.ctxInformation.containsKey(childrenNodes.get(1))) {
//					formula.append("( - CTX_" + childrenNodes.get(0) + " * " + childrenNodes.get(0) 
//						+ " * CTX_" + childrenNodes.get(1) + " * " + childrenNodes.get(1)
//						+ " + CTX_" + childrenNodes.get(0) + " * " + childrenNodes.get(0) 
//						+ " + CTX_" + childrenNodes.get(1) + " * " + childrenNodes.get(1) + " ) ");
//				}
//				else if (this.ctxInformation.containsKey(childrenNodes.get(0))) {
//					formula.append("( - CTX_" + childrenNodes.get(0) + " * " + childrenNodes.get(0) 
//					+ " * " + childrenNodes.get(1)
//					+ " + CTX_" + childrenNodes.get(0) + " * " + childrenNodes.get(0) 
//					+ " + " + childrenNodes.get(1) + " ) ");
//				}
//				else if (this.ctxInformation.containsKey(childrenNodes.get(1))) {
//					formula.append("( - " + childrenNodes.get(0)
//					+ " * CTX_" + childrenNodes.get(1) + " * " + childrenNodes.get(1)
//					+ " + " + childrenNodes.get(0)
//					+ " + CTX_" + childrenNodes.get(1) + " * " + childrenNodes.get(1) + " ) ");
//				}
//				else {
//					formula.append("( - " + childrenNodes.get(0) + " * " + childrenNodes.get(1) + " + " + childrenNodes.get(0) + " + " + childrenNodes.get(1) + " ) ");
//				}
//				
//				String removeFromFormula = new String();
//				String sumCost = new String();
//				if (!reliability) {
//					formula = replaceAll(formula, " " + childrenNodes.get(0) + " ", " R_" + childrenNodes.get(0) + " ");
//					formula = replaceAll(formula, " " + childrenNodes.get(1) + " ", " R_" + childrenNodes.get(1) + " ");
//					
//					if (this.ctxInformation.containsKey(childrenNodes.get(0)) && this.ctxInformation.containsKey(childrenNodes.get(1))) {
//						removeFromFormula = " - CTX_" + childrenNodes.get(0) + " * " + "R_" + childrenNodes.get(0) + " * CTX_" + childrenNodes.get(1)+ " * " + childrenNodes.get(1);
//						sumCost = "CTX_" + childrenNodes.get(0) + " * " + childrenNodes.get(0) + " + CTX_" + childrenNodes.get(1) + " * " + childrenNodes.get(1);
//					}
//					else if (this.ctxInformation.containsKey(childrenNodes.get(0))) {
//						removeFromFormula = " - CTX_" + childrenNodes.get(0) + " * " + "R_" + childrenNodes.get(0) + " * " + childrenNodes.get(1);
//						sumCost = "CTX_" + childrenNodes.get(0) + " * " + childrenNodes.get(0) + " + " + childrenNodes.get(1);
//					}
//					else if (this.ctxInformation.containsKey(childrenNodes.get(1))) {
//						removeFromFormula = " - R_" + childrenNodes.get(0) + " * CTX_" + childrenNodes.get(1)+ " * " + childrenNodes.get(1);
//						sumCost = childrenNodes.get(0) + " + CTX_" + childrenNodes.get(1) + " * " + childrenNodes.get(1);
//					}
//					else {
//						removeFromFormula = " - R_" + childrenNodes.get(0) + " * " + childrenNodes.get(1);
//						sumCost = childrenNodes.get(0) + " + " + childrenNodes.get(1);
//					}
//				}
//
//				for (int i = 2; i < childrenNodes.size(); i++) {
//					if (!reliability) {
//						if (this.ctxInformation.containsKey(childrenNodes.get(i))) {
//							removeFromFormula += " - " + formula.toString() + " * CTX_" + childrenNodes.get(i) + " * " + childrenNodes.get(i);
//							sumCost += " + CTX_" + childrenNodes.get(i) + " * " + childrenNodes.get(i);
//						}
//						else {
//							removeFromFormula += " - " + formula + " * " + childrenNodes.get(i);
//							sumCost += " + " + childrenNodes.get(i);
//						}
//					}
//					String currentFormula = formula.toString();
//					if (this.ctxInformation.containsKey(childrenNodes.get(i))) {
//						formula.insert(0, "( - ");
//						formula.append(currentFormula + " * CTX_" + childrenNodes.get(i) 
//						+ " * " + childrenNodes.get(i) + " + " + currentFormula + " + CTX_" 
//								+ childrenNodes.get(i) + " * " + childrenNodes.get(i) + " ) ");
//					}
//					else {
//						formula.insert(0, "( - ");
//						formula.append(currentFormula + " * " + childrenNodes.get(i) + " + " + currentFormula + " + " + childrenNodes.get(i) + " ) ");
//					}
//					if (!reliability) formula = replaceAll(formula, " " + childrenNodes.get(i) + " ", "R_" + childrenNodes.get(i));
//				}
//				if (!reliability) {
//					String reliabilityFormula = formula.toString();
//					formula = new StringBuilder();
//					formula.append(" ( ( " + sumCost + " ) * " + reliabilityFormula + " " + removeFromFormula + " )");
//				}
//			}
//			return formula.toString();
//		}
//		else {
//			//DM-annotation (children nodes should always contain context information)
//			if (childrenNodes.size() == 1) {
//				//Assuring children nodes contain ctx information
//				if (!this.ctxInformation.containsKey(childrenNodes.get(0))) {
//					throw new Exception();
//				}
//				
//				if (reliability) return " ( CTX_" + childrenNodes.get(0) + " * " + childrenNodes.get(0) + " )";
//				formula.append(" ( CTX_" + childrenNodes.get(0) + " * R_" + childrenNodes.get(0) + " * " + childrenNodes.get(0) + " )");
//			}
//			else {
//				//Assuring children nodes contain ctx information
//				if (!this.ctxInformation.containsKey(childrenNodes.get(0)) && !this.ctxInformation.containsKey(childrenNodes.get(1))) {
//					throw new Exception();
//				}
//				
//				formula.append("( - CTX_" + childrenNodes.get(0) + " * " + childrenNodes.get(0)
//				+ " * CTX_" + childrenNodes.get(1) + " * " + childrenNodes.get(1) 
//				+ " + CTX_" + childrenNodes.get(0) + " * " + childrenNodes.get(0)
//				+ " + CTX_" + childrenNodes.get(1) + " * " + childrenNodes.get(1) + " ) ");
//
//				String removeFromFormula = new String();
//				String sumCost = new String();
//				if (!reliability) {
//					formula = replaceAll(formula, " " + childrenNodes.get(0) + " ", " R_" + childrenNodes.get(0) + " ");
//					formula = replaceAll(formula, " " + childrenNodes.get(1) + " ", " R_" + childrenNodes.get(1) + " ");
//					removeFromFormula = " - CTX_" + childrenNodes.get(0) + " * R_" + childrenNodes.get(0) + " * CTX_" + childrenNodes.get(1) + " * " + childrenNodes.get(1);
//					sumCost = " CTX_" + childrenNodes.get(0) + " * " + childrenNodes.get(0) + " + CTX_" + childrenNodes.get(1) + " * " + childrenNodes.get(1);
//				}
//
//				for (int i = 2; i < childrenNodes.size(); i++) {
//					if (!this.ctxInformation.containsKey(childrenNodes.get(i))) {
//						throw new Exception();
//					}
//					
//					if (!reliability) {
//						removeFromFormula += " - " + formula.toString() + " * CTX_" + childrenNodes.get(i) + " * " + childrenNodes.get(i);
//						sumCost += " + " + childrenNodes.get(i);
//					}
//					String currentFormula = formula.toString();
//					formula.insert(0, "( - ");
//					formula.append(" * CTX_" + childrenNodes.get(i) + " * " + childrenNodes.get(i) 
//						+ " + " + currentFormula + " + CTX_" + childrenNodes.get(i) + " * " + childrenNodes.get(i) + " ) ");
//					if (!reliability) formula = replaceAll(formula, " " + childrenNodes.get(i) + " ", "R_" + childrenNodes.get(i));
//				}
//				if (!reliability) {
//					String reliabilityFormula = formula.toString();
//					formula = new StringBuilder();
//					formula.append(" ( ( " + sumCost + " ) * " + reliabilityFormula + " " + removeFromFormula + " )");
//				}
//			}
//		}
//		return formula.toString();
//	}

	public static StringBuilder replaceAll(StringBuilder sb, String find, String replace) {
		return new StringBuilder(Pattern.compile(find).matcher(sb).replaceAll(replace));
	}

	private void setContextList(LinkedList<GoalContainer> decompGoal, LinkedList<PlanContainer> decompPlans) {

		if (!decompGoal.isEmpty()) {
			for (GoalContainer goal : decompGoal) {
				List<String> ctxList = goal.getFulfillmentConditions();
				if (!ctxList.isEmpty()) {
					List<String> cleanCtx = clearCtxList(ctxList);
					String ctxConcat = concatCtxInformation(cleanCtx);
					this.ctxInformation.put(goal.getClearUId(), ctxConcat);
				}
			}
		} else if (!decompPlans.isEmpty()) {
			for (PlanContainer plan : decompPlans) {
				List<String> ctxList = plan.getFulfillmentConditions();
				if (!ctxList.isEmpty()) {
					List<String> cleanCtx = clearCtxList(ctxList);
					String ctxConcat = concatCtxInformation(cleanCtx);
					this.ctxInformation.put(plan.getClearElId(), ctxConcat);
				}
			}
		}
	}

	private String concatCtxInformation(List<String> cleanCtx) {
		String ctxConcat = new String();
		for (String ctx : cleanCtx) {
			if (ctxConcat.length() == 0) {
				ctxConcat = "(" + ctx + ")";
			} else {
				ctxConcat = ctxConcat.concat(" & (" + ctx + ")");
			}
		}
		return ctxConcat;
	}

	private String getCostFormula(RTContainer rootNode) throws IOException {
		if(! (rootNode instanceof PlanContainer)) {
			throw new IllegalStateException(rootNode.getClearElId() + " should be a plan");
		}
		
		PlanContainer plan = (PlanContainer) rootNode;

		if (plan.getCostRegex() != null) {
			Object[] res = CostParser.parseRegex(plan.getCostRegex());
			return (String) res[2];
		}

		return "W_" + rootNode.getClearElId();
	}

	private List<String> clearCtxList(List<String> ctxAnnot) {

		List<String> clearCtx = new ArrayList<String>();
		for (String ctx : ctxAnnot) {
			String[] aux;
			if (ctx.contains("assertion condition")) {
				aux = ctx.split("^assertion condition\\s*");
			} else {
				aux = ctx.split("^assertion trigger\\s*");
			}
			clearCtx.add(aux[1]);
		}

		return clearCtx;
	}

	private String replaceSubForm(String nodeForm, String subNodeForm, String nodeId, String subNodeId,
			boolean reliability) {

		if (nodeForm.equals(nodeId)) {
			nodeForm = subNodeForm;
		} else {
			subNodeId = restricToString(subNodeId);
			subNodeForm = restricToString(subNodeForm);
			nodeForm = nodeForm.replaceAll(subNodeId, subNodeForm);
		}

		if (subNodeForm.contains("CTX") && nodeForm.contains("XOR")) {
			for (Map.Entry<String, String> entry : ctxInformation.entrySet()) {
				if (entry.getKey().contains(subNodeId.trim())) {
					nodeForm = nodeForm.replaceAll("XOR_" + subNodeId.trim(), entry.getKey());
					return nodeForm;
				}
			}
		}
		return nodeForm;
	}

	private String restricToString(String subNodeString) {
		return " " + subNodeString + " ";
	}

	private String[] getChildrenId(RTContainer rootNode) {
		List<String> childrenId = new ArrayList<String>();
		LinkedList<RTContainer> children = rootNode.getDecompElements();

		if (children.isEmpty())
			return null;

		for (RTContainer child : children) {
			if (child instanceof GoalContainer)
				childrenId.add(child.getClearUId());
			else if (child instanceof PlanContainer)
				childrenId.add(child.getClearElId());
		}

		return childrenId.toArray(new String[0]);
	}
}