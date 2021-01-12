package br.unb.cic.goda.rtgoretoprism.generator.goda.writer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.antlr.v4.runtime.misc.ParseCancellationException;

import br.unb.cic.goda.model.ModelTypeEnum;
import br.unb.cic.goda.model.Plan;
import br.unb.cic.goda.rtgoretoprism.generator.CodeGenerationException;
import br.unb.cic.goda.rtgoretoprism.generator.goda.parser.CtxParser;
import br.unb.cic.goda.rtgoretoprism.generator.kl.AgentDefinition;
import br.unb.cic.goda.rtgoretoprism.model.kl.Const;
import br.unb.cic.goda.rtgoretoprism.model.kl.GoalContainer;
import br.unb.cic.goda.rtgoretoprism.model.kl.PlanContainer;
import br.unb.cic.goda.rtgoretoprism.model.kl.RTContainer;
import br.unb.cic.goda.rtgoretoprism.paramformula.GenerateCombination;
import br.unb.cic.goda.rtgoretoprism.util.PathLocation;

public class PrismWriter {
	/** the set of placeholder founded into template files that are 
	 * substituted with the proper values during the code generation
	 * process. */

	private static final String MODULE_NAME_TAG			= "$MODULE_NAME$";
	private static final String TIME_SLOT_TAG			= "$TIME_SLOT$";
	private static final String PREV_TIME_SLOT_TAG		= "$PREV_TIME_SLOT$";
	private static final String GID_TAG					= "$GID$";
	private static final String PREV_GID_TAG 			= "$PREV_GID$";
	private static final String GOAL_MODULES_TAG 		= "$GOAL_MODULES$";
	private static final String DEC_HEADER_TAG	 		= "$DEC_HEADER$";
	private static final String DEC_TYPE_TAG	 		= "$DEC_TYPE$";
	private static final String DEC_TYPE_TRY_TAG	 	= "$DEC_TYPE_TRY$";
	private static final String REWARD_TAG				= "$REWARD_STRUCTURE$";
	private static final String COST_VALUE_TAG			= "$COST$";
	private static final String CONST_PARAM_TAG			= "$CONST_PARAM$";
	private static final String MAX_TRIES_TAG			= "$MAX_TRIES$";
	private static final String MAX_RETRIES_TAG 		= "$MAX_RETRIES$";
	/*private static final String PARAMS_BASH_TAG	 		= "$PARAMS_BASH$";
	private static final String REPLACE_BASH_TAG	 	= "$REPLACE_BASH$";*/

	private final String constOrParam;
	private static final String DEC_TRY_CTX = "$CTX_GID$ : (s$GID$'=1) + (1 - $CTX_GID$) : (s$GID$'=3); //init to running or skip";
	private static final String DEC_TRY_NO_CTX = "(s$GID$'=1);//init to running";
	
	/** where to find PRISM related template base section, inside the template folder */
	private final String TEMPLATE_PRISM_BASE_PATH = "PRISM/";

	private String templateInputBaseFolder;
	/** template input PRISM folder */
	private String inputPRISMFolder;
	/** generated agent target folder */
	private String agentOutputFolder;
	/** the folder that will contain all the generated agent */
	private String basicOutputFolder;
	/** the base package for the current Agent */
	private String basicAgentPackage;

	// Strings that contain the parts of the ADF skeleton, read from file
	private String header, body, reward;/*, evalBash;*/
	private StringBuilder planModules = new StringBuilder();
	private StringBuilder rewardModule = new StringBuilder();
	/*private String evalFormulaParams = "";
	private String evalFormulaReplace = "";
	private StringBuilder evalFormulaContexts = new StringBuilder();*/

	/** PRISM patterns */
	private String leafGoalPattern;
	private String andDecPattern;
	private String ctxHeaderPattern;
	private String ctxFailPattern;
	private String optPattern;
	private String optHeaderPattern;
	private String ctxSkipPattern;
	private String rewardPattern;
	private String ndPattern;
	private String ndHeaderPattern;
	private String ndBodyPattern;
	private String prevFailurePattern;
	private String rtryCardPattern;
	private String trySucessPattern;
	private String tryFailPattern;
	private String tryOriginalPattern;

	/** Has all the informations about the agent. */ 
	private AgentDefinition ad;
	/** the list of plan that are root for a capability of the selected agent */
	private List<Plan> capabilityPlanList;

	private List<String> rewardVariables = new ArrayList<String>();
	
	private int nonDeterminismCtxId = 1;
	private Map<RTContainer,String> nonDeterminismCtxList = new HashMap<RTContainer,String>();
	private Map<String,String> contextList = new HashMap<String,String>();
	/**
	 * Creates a new AgentWriter instance
	 * 
	 * @param ad the source agentDefinition object from which data should be extracted
	 * @param inputFolder template input folder 
	 * @param outputFolder generated code target folder
	 */

	public PrismWriter(AgentDefinition ad, List<Plan> capPlan, String input, String output, Boolean parametric) {
		this.ad = ad;
		this.capabilityPlanList = capPlan;
		this.templateInputBaseFolder = input + "/";
		this.inputPRISMFolder = templateInputBaseFolder + TEMPLATE_PRISM_BASE_PATH;
		this.basicOutputFolder = output + "/";
		this.basicAgentPackage = PathLocation.BASIC_AGENT_PACKAGE_PREFIX + ad.getAgentName();
		this.constOrParam = "const";
	}

	/**	private List<Plan> capabilityPlanList;

	 * Writes the whole Agent (ADF + Java plan bodies).
	 * 
	 * @throws CodeGenerationException 
	 * @throws IOException 
	 */
	public void writeModel(String typeModel) throws CodeGenerationException, IOException {
		
		String utilPkgName = basicAgentPackage + PathLocation.UTIL_KL_PKG;
		String prismInputFolder = inputPRISMFolder;
		String planOutputFolder = basicOutputFolder + "plans" + "/";
		String planPkgName = basicAgentPackage + ".plans";
		header = ManageWriter.readFileAsString( prismInputFolder + typeModel.toUpperCase() + "_modelheader.nm" );
		body = ManageWriter.readFileAsString( prismInputFolder + "modelbody.nm" );
		reward = ManageWriter.readFileAsString( prismInputFolder + "modelreward.nm" );
		/*evalBash = ManageWriter.readFileAsString( prismInputFolder + "eval_formula.sh" );*/
		writeAnOutputDir(basicOutputFolder);
		PrintWriter modelFile = ManageWriter.createFile(ad.getAgentName() + ".nm", basicOutputFolder);
		/*PrintWriter evalBashFile = ManageWriter.createFile("eval_formula.sh", basicOutputFolder);*/
		writePrismModel(prismInputFolder, ad.rootlist, planOutputFolder, basicAgentPackage, utilPkgName, planPkgName, typeModel);
		printModel(modelFile);
		/*printEvalBash(evalBashFile);*/
	}

	/**
	 * Writes all goals to the ADF file (to beliefbase, goals and plans section) and organizes
	 * (copies) the plan bodies. Works not recursive on the goal structure, but processes all goals
	 * in the list in sequence.
	 * 
	 * @param input the template input folder
	 * @param gb beliefe base goal
	 * @param planOutputFolder
	 * @param pkgName
	 * @param utilPkgName
	 * @param planPkgName 
	 * 
	 * @throws CodeGenerationException 
	 * @throws IOException 
	 */	
	private void writePrismModel( String input, LinkedList<GoalContainer> rootGoals, 
			String planOutputFolder, String pkgName, String utilPkgName, String planPkgName, String typeModel ) throws CodeGenerationException, IOException {

		leafGoalPattern 				= ManageWriter.readFileAsString(input + "pattern_leafgoal.nm");
		andDecPattern 					= ManageWriter.readFileAsString(input + "pattern_and.nm");
		ctxHeaderPattern				= ManageWriter.readFileAsString(input + "pattern_ctx_header.nm");
		ctxSkipPattern					= ManageWriter.readFileAsString(input + "pattern_ctx_skip.nm");
		ctxFailPattern					= ManageWriter.readFileAsString(input + "pattern_ctx_fail.nm");
		rewardPattern					= ManageWriter.readFileAsString(input + "pattern_reward.nm");
		ndPattern						= ManageWriter.readFileAsString(input + "pattern_nondeterminism.nm");
		ndHeaderPattern					= ManageWriter.readFileAsString(input + "pattern_nd_header.nm");
		ndBodyPattern					= ManageWriter.readFileAsString(input + "pattern_nd_body.nm");
		optPattern						= ManageWriter.readFileAsString(input + "pattern_opt.nm");
		optHeaderPattern				= ManageWriter.readFileAsString(input + "pattern_opt_header.nm");
		prevFailurePattern 				= ManageWriter.readFileAsString(input + "pattern_prev_failure.nm");
		rtryCardPattern 				= ManageWriter.readFileAsString(input + "pattern_retry.nm");
		trySucessPattern				= ManageWriter.readFileAsString(input + "pattern_try_success.nm");
		tryFailPattern 					= ManageWriter.readFileAsString(input + "pattern_try_fail.nm");
		tryOriginalPattern 				= ManageWriter.readFileAsString(input + "pattern_try_original.nm");
		

		//Collections.sort(rootGoals);

		for( GoalContainer root : rootGoals ) {
			writeElement(
					root, 
					leafGoalPattern,							
					null,
					typeModel);
			planModules = planModules.append("label \"success\" = " + root.getClearElId() + ";");
		}
	}

	/**
	 * Writes the dispatch plans (with bodies) for every child goal
	 * 
	 * @param goal
	 * @param pattern
	 * @throws IOException 
	 */
	private String[] writeElement(
			RTContainer root, 
			String pattern, 							 
			String prevFormula, 
			String typeModel) throws IOException {
		
		if (root.isDecisionMaking() && isMDP(typeModel)) {
			writeNondeterministicModule(root);
		}
		
		//Creating context repository
		/*if (!root.getFulfillmentConditions().isEmpty()) {
			StringBuilder rootContext = new StringBuilder();
			for (String s : root.getFulfillmentConditions()) {
				rootContext.append(s + "&");
			}
			rootContext.deleteCharAt(rootContext.lastIndexOf("&"));
			if (!this.contextList.containsValue(rootContext.toString())) {
				this.contextList.put(root.getClearElId(), rootContext.toString());
			}
		}*/
		
		String operator = root.getDecomposition() == Const.AND ? " & " : " | ";
		if(!root.getDecompGoals().isEmpty()){
			StringBuilder goalFormula = new StringBuilder();
			String prevGoalFormula = prevFormula;
			int prevTimeSlot = root.getDecompGoals().get(0).getRootTimeSlot();
			
			for(GoalContainer gc : root.getDecompGoals()){
				String currentFormula;
				if(prevTimeSlot < gc.getRootTimeSlot())
					currentFormula = prevGoalFormula;
				else
					currentFormula = prevFormula;
				writeElement(gc, pattern, currentFormula, typeModel);												
				if(gc.isIncluded())
					prevGoalFormula = gc.getClearElId();
				if(prevGoalFormula != null)
					goalFormula.append(prevGoalFormula + operator);							
			}
			if(prevGoalFormula != null)
				goalFormula.replace(goalFormula.lastIndexOf(operator), goalFormula.length(), "");
			if(root.isIncluded()) {
				planModules = planModules.append("formula " + root.getClearElId() + " = " + goalFormula + ";\n");
			}
			return new String [] {root.getClearElId(), goalFormula.toString()};
		}else if(!root.getDecompPlans().isEmpty()){
			StringBuilder taskFormula = new StringBuilder();
			String prevTaskFormula = prevFormula;
			
			for(PlanContainer pc : root.getDecompPlans()){
				String childFormula = writeElement(pc, pattern, prevTaskFormula, typeModel)[1];
				if(!childFormula.isEmpty())
					taskFormula.append("(" + childFormula + ")" + operator);
			}
			if(taskFormula.length() > 0)
				taskFormula.replace(taskFormula.lastIndexOf(operator), taskFormula.length(), "");
			if(root instanceof GoalContainer)
				planModules = planModules.append("formula " + root.getClearElId() + " = " + taskFormula + ";\n\n");
			return new String [] {root.getClearElId(), taskFormula.toString()};
		}else if(root instanceof PlanContainer){
			writeRewardModule(root);
			return writePrismModule(root, pattern, prevFormula);
		}

		return new String[]{"",""};
	}

	private void writeNondeterministicModule(RTContainer root) throws ParseCancellationException, IOException {
		String singlePattern = new String(this.ndPattern);

		Map<String, String> mapContexts = getContextList(root);
		
		List<String[]> list = new ArrayList<String[]>();
		GenerateCombination comb1 = new GenerateCombination(mapContexts.keySet().toArray(new String[0]), 0) ;
        while (comb1.hasNext()) {
        	list.add(comb1.next());
        }
        
        StringBuilder sbHeader = new StringBuilder();
    	StringBuilder sbType = new StringBuilder();
    	//int nextState = 1;
        
    	
    	String sbHeaderAux = "\n";
    	String sbTypeAux = new String();
    	String contextUpdate = new String();
    	
        for (String[] ctxCombination : list) {
        	String ndHeaderPattern = new String(this.ndHeaderPattern);
        	ndHeaderPattern = ndHeaderPattern.replace("$N$", Integer.toString(this.nonDeterminismCtxId));
        	sbHeader.append(ndHeaderPattern + " //");
        	
        	//Add comment of contexts
        	contextUpdate = new String();
        	for (Map.Entry<String, String> entry : mapContexts.entrySet()) {
        		String global = "global CTX_" + entry.getKey()+ ": [0..1] init 0;\n";
        		if (!sbHeaderAux.contains(global)) sbHeaderAux = sbHeaderAux.concat(global); 
        			
        		for (int i = 0; i < ctxCombination.length; i++) {
        			if (ctxCombination[i].equals(entry.getKey())) {
            			sbHeader.append(" " + entry.getValue() + " &");
            			//Save contexts to be inserted
            			contextUpdate = contextUpdate.concat(" & (CTX_" + entry.getKey() + "'=1)");
            		}
        		}
        	}
        	sbHeader.replace(sbHeader.length()-1, sbHeader.length(), "\n");
        	
        	//Build the body of the module
        	String ndBodyPattern = new String(this.ndBodyPattern);
        	ndBodyPattern = ndBodyPattern.replace("$N$", Integer.toString(this.nonDeterminismCtxId));
        	//ndBodyPattern = ndBodyPattern.replace("$NEXT_STATE$", Integer.toString(nextState+1));
        	ndBodyPattern = ndBodyPattern.replace("$CONTEXT_UPDATE$", contextUpdate);
        	
        	/*String ndBodyAux = "\t[] s$GID$ = $NEXT_STATE$ -> (s$GID$'=$MAX_ND$)$CONTEXT_UPDATE$;\n";
        	ndBodyAux = ndBodyAux.replace("$CONTEXT_UPDATE$", contextUpdate);
        	ndBodyAux = ndBodyAux.replace("$NEXT_STATE$", Integer.toString(nextState+1));
        	sbTypeAux = sbTypeAux.concat(ndBodyAux);*/
        	
        	if (sbType.length() == 0) sbType.append(ndBodyPattern);
        	else sbType.append("\t" + ndBodyPattern);
        	
        	this.nonDeterminismCtxId++;
        	//nextState++;
        }        
        sbHeader.append(sbHeaderAux);
        //sbTypeAux = sbTypeAux.replace("$MAX_ND$", Integer.toString(nextState+1));

        //singlePattern = singlePattern.replace("$MAX_ND$", Integer.toString(nextState+1));
        singlePattern = singlePattern.replace("$FINAL_TYPE$", sbTypeAux);
        singlePattern = singlePattern.replace(DEC_HEADER_TAG, sbHeader.toString());
        singlePattern = singlePattern.replace(DEC_TYPE_TAG, sbType.toString());
        singlePattern = singlePattern.replace(GID_TAG, root.getClearElId());
        
    	//Time
    	//Integer timeSlot = root.getTimeSlot()-1;
    	Integer timeSlot = root.getTimeSlot();
    	Integer prevTimeSlot = root.getPrevTimeSlot();
    	
    	singlePattern = singlePattern.replace(PREV_TIME_SLOT_TAG, "_" + prevTimeSlot + "");
		singlePattern = singlePattern.replace(TIME_SLOT_TAG, "_" + timeSlot + "");
		
    	/*if(root.getCardType().equals(Const.SEQ))
    		timeSlot -= root.getCardNumber() - 1; 
    	for(int i = root.getCardNumber(); i >= 0; i--){
    		while ((timeSlot - 1 + i) < 0) timeSlot++;
    		singlePattern = singlePattern.replace(PREV_TIME_SLOT_TAG + (i > 1 ? "_N" + i : "") + "$", "_" + (timeSlot - 1 + i) + "");
    		singlePattern = singlePattern.replace(TIME_SLOT_TAG + (i > 1 ? "_N" + i : "") + "$", "_" + (timeSlot + i) + "");
    	}*/
        
        planModules = planModules.append(singlePattern+"\n");
	}
	
	private Map<String, String> getContextList(RTContainer root) throws ParseCancellationException, IOException {
		Map<String, String> context = new TreeMap<String, String>();
		
		StringBuilder fulfillmentConditions = new StringBuilder();
		String id = new String();

		if (!root.getDecompGoals().isEmpty()) {
			for (GoalContainer goal : root.getDecompGoals()) {
				fulfillmentConditions = getContextsInfo(goal);
				id = goal.getClearElId();
				
				this.nonDeterminismCtxList.put(goal, fulfillmentConditions.toString());
				context.put(id, fulfillmentConditions.toString());
			}
		}
		else {
			for (PlanContainer plan : root.getDecompPlans()) {
				fulfillmentConditions = getContextsInfo(plan);				
				id = plan.getClearElId();
				
				this.nonDeterminismCtxList.put(plan, fulfillmentConditions.toString());
				context.put(id, fulfillmentConditions.toString());
			}
		}
		return context;
	}

	private void writeRewardModule(RTContainer root) {
		PlanContainer plan = (PlanContainer) root;
		String rewardPattern = new String(this.rewardPattern);
		rewardPattern = rewardPattern.replace(GID_TAG, plan.getClearElId());
		
		if (plan.getCostRegex() != null) {
			String costVariable = plan.getCostVariable();
			if (costVariable == null) {
				rewardPattern = rewardPattern.replace(COST_VALUE_TAG, plan.getCostValue());
			}
			else {
				rewardPattern = rewardPattern.replace(COST_VALUE_TAG, plan.getCostValue() + "*" + costVariable);
				if (!this.rewardVariables.contains(costVariable)) this.rewardVariables.add(costVariable);
			}
		}
		else {
			String newCost = "W_" + plan.getClearElId();
			rewardPattern = rewardPattern.replace(COST_VALUE_TAG, newCost);
			if (!this.rewardVariables.contains(newCost)) this.rewardVariables.add(newCost);
		}
		rewardModule = rewardModule.append(rewardPattern);
	}

	private String[] writePrismModule(
			RTContainer root, 
			String singlePattern,							
			String prevFormula) throws IOException{
	
		singlePattern = new String(singlePattern);
	
		String andDecPattern = new String(this.andDecPattern),
				ctxSkipPattern = new String(this.ctxSkipPattern),
				optPattern = new String(this.optPattern),
				optHeaderPattern = new String(this.optHeaderPattern),
				trySuccessPattern = new String(this.trySucessPattern),
				tryFailPattern = new String(this.tryFailPattern),
				tryOriginalPattern = new String(this.tryOriginalPattern);
				//ctxFailPattern = new String(this.ctxFailPattern);
	
		PlanContainer plan = (PlanContainer) root;
		String planModule;
		StringBuilder planFormula = new StringBuilder();
		String ctxId = getContextId(plan);
	
		boolean contextPresent = false;
		//boolean goalContext = false;
		boolean nonDeterminismCtx = false;
	
		if(constOrParam.equals("const") &&
				(!plan.getFulfillmentConditions().isEmpty() ||
						!plan.getAdoptionConditions().isEmpty())){
	
			contextPresent = true;
	
			/*for (String ctxCondition : plan.getFulfillmentConditions()){
				Object [] parsedCtxs = CtxParser.parseRegex(ctxCondition);
				if (((CtxSymbols) parsedCtxs[2] == CtxSymbols.COND) || (plan.getClearElId().contains("X"))) 
					goalContext = true; 
			}*/
			
			/*String ctx = getContextsInfo(plan).toString();
			/RTContainer node = getKeyRTContainer(this.nonDeterminismCtxList, plan);
			if (this.nonDeterminismCtxList.containsKey(node) && (node.equals(plan) || equalsRoot(node, plan))) {
				nonDeterminismCtx = true;
			}*/
			
			if (this.nonDeterminismCtxList.containsKey(plan) || ndCtxListContainsARoot(plan) != null) {
				nonDeterminismCtx = true;
			}
		}
	
		if (plan.getCardType() == Const.RTRY) {
			planModule = rtryCardPattern.replace(MODULE_NAME_TAG, plan.getClearElName());
		} else if (plan.isTry()) {
			planModule = tryOriginalPattern.replace(MODULE_NAME_TAG, plan.getClearElName());
		} else {
			planModule = singlePattern.replace(MODULE_NAME_TAG, plan.getClearElName());
		}
		
		StringBuilder sbHeader = new StringBuilder();
		StringBuilder sbType = new StringBuilder();
		
		if (plan.isOptional()) {
			if (contextPresent) {
				optPattern = optPattern.replace("$IF_CTX$", "*CTX_$GID$");
			}
			optPattern = optPattern.replace("$IF_CTX$", "");
			sbHeader.append(optHeaderPattern);
			sbType.append(optPattern);
		}
		else if (plan.getTryOriginal() != null || plan.getTrySuccess() != null || plan.getTryFailure() != null) {
			if (plan.getTrySuccess() != null || plan.getTryFailure() != null) {
				processPlanFormula(plan, planFormula, Const.TRY);
			} else if (plan.isSuccessTry()) {
				PlanContainer tryPlan = (PlanContainer) plan.getTryOriginal();
				trySuccessPattern = trySuccessPattern.replace(PREV_GID_TAG, tryPlan.getClearElId());
				sbType.append(trySuccessPattern);
				processPlanFormula(plan, planFormula, Const.TRY_S);
			} else {
				PlanContainer tryPlan = (PlanContainer) plan.getTryOriginal();
				tryFailPattern = tryFailPattern.replace(PREV_GID_TAG, tryPlan.getClearElId());
				sbType.append(tryFailPattern);
				processPlanFormula(plan, planFormula, Const.TRY_F);
			}
		}
		else {
			processPlanFormula(plan, planFormula, plan.getRoot().getDecomposition());
		}
		
		if(contextPresent){
			if (!plan.isOptional() && !plan.isTryFailure() && !plan.isTrySuccess()) sbType.append(ctxSkipPattern.replace("$CTX_GID$", "CTX_" + ctxId));	
			if (!nonDeterminismCtx) sbHeader.append(getContextHeader(plan));
		}
		else if (!plan.isOptional() && !plan.isTrySuccess() && !plan.isTryFailure()){
			sbType.append(andDecPattern);
		}
		
		//Header
		planModule = planModule.replace(DEC_HEADER_TAG, sbHeader.toString());
		//Type
		planModule = planModule.replace(DEC_TYPE_TAG, sbType.toString());
		
		if (contextPresent && (plan.isTryFailure() || plan.isTrySuccess())) {
			planModule = planModule.replace(DEC_TYPE_TRY_TAG, DEC_TRY_CTX);
			planModule = planModule.replace("$CTX_GID$", "CTX_" + ctxId);
		}
		else {
			planModule = planModule.replace(DEC_TYPE_TRY_TAG, DEC_TRY_NO_CTX);
		}
		
		planModule = planModule.replace("$PREV_EFFECT$", buildPrevFailureFormula(prevFormula));
		//Prev Success Guard Condition
		String prevSuccessFormula = buildPrevSuccessFormula(prevFormula, plan);
		planModule = planModule.replace("$PREV_SUCCESS$", prevSuccessFormula);
		planModule = planModule.replace("$PREV_SUCCESS_EFFECT$", buildPrevSuccessEffectFormula(prevSuccessFormula, plan));		
	
		//Time
		Integer timeSlot = plan.getTimeSlot();
    	Integer prevTimeSlot = plan.getPrevTimeSlot();
    	
    	planModule = planModule.replace(PREV_TIME_SLOT_TAG, "_" + prevTimeSlot + "");
    	planModule = planModule.replace(TIME_SLOT_TAG, "_" + timeSlot + "");
		/*for(int i = plan.getCardNumber(); i >= 0; i--){
			planModule = planModule.replace(PREV_TIME_SLOT_TAG + (i > 1 ? "_N" + i : "") + "$", "_" + (timeSlot -1 + i) + "");
			planModule = planModule.replace(TIME_SLOT_TAG + (i > 1 ? "_N" + i : "") + "$", "_" + (timeSlot + i) + "");
		}*/
	
		//GID
		planModule = planModule.replace(GID_TAG, plan.getClearElId());
		//CONST OR PARAM
		planModule = planModule.replace(CONST_PARAM_TAG, constOrParam);
		planModule = planModule.replace(MAX_TRIES_TAG, plan.getCardNumber() + 1 + "");
		planModule = planModule.replace(MAX_RETRIES_TAG, plan.getCardNumber() + "");
		planModules = planModules.append(planModule+"\n");				
		return new String[]{plan.getClearElId(), planFormula.toString()};
	}

	private RTContainer ndCtxListContainsARoot(RTContainer plan) {
		RTContainer root = plan.getRoot();
		while (root != null && !this.nonDeterminismCtxList.containsKey(root)) {
			root = root.getRoot();
		}
		if (root == null) return null;
		return root;
	}

	private RTContainer getKeyRTContainer(Map<RTContainer, String> list, String ctx) {
	
		for (Entry<RTContainer, String> entry : list.entrySet()) {
			if (ctx.equals(entry.getValue())) return entry.getKey();
		}
		return null;
	}
	
	private String getContextHeader(PlanContainer plan) throws ParseCancellationException, IOException {
		String header = new String(ctxHeaderPattern);
		header = commentContextInformation((RTContainer) plan, header);
		return header;
	}
	
	/** Return list contains:
	 *  [0] : fulfillmentCondition
	 *  [1] : adoptionCondition
	 *  //[2] : ctxEffect 
	 */
	private StringBuilder getContextsInfo(RTContainer altFirst) throws ParseCancellationException, IOException {
	
		StringBuilder fullContextCondition = new StringBuilder();
	
		if(!altFirst.getFulfillmentConditions().isEmpty()){				
			for(String ctxCondition : altFirst.getFulfillmentConditions()){
				Object [] parsedCtxs = CtxParser.parseRegex(ctxCondition);
				fullContextCondition.append(fullContextCondition.length() > 0 ? " & " : "").append(parsedCtxs[1]);
			}		
		}
		return fullContextCondition;
	}

	private String commentContextInformation(RTContainer altFirst, String xorVar) throws ParseCancellationException, IOException {
		StringBuilder contextInformation = getContextsInfo(altFirst);
		xorVar = xorVar.substring(0, xorVar.length() - 2);
		xorVar += " //" + contextInformation.toString() + "\n";
		return xorVar;
	}
	
	/*Check if plan descends from alt*/
	private boolean equalsRoot(RTContainer alt, RTContainer plan) {
	
		RTContainer root = plan.getRoot();
		while (!root.equals(this.ad.getRootGoalList().get(0))) {
			if (alt.equals(root)) return true;
			root = root.getRoot();
		}
	
		if (alt.equals(root)) return true;
		return false;
	}
	
	private void processPlanFormula(PlanContainer plan, StringBuilder planFormula, Const decType) throws IOException {
		String op = planFormula.length() == 0 ? "" : " & ";
		switch (decType) {
		case OR:
			planFormula.append(buildAndOrSuccessFormula(plan, planFormula));
			break;
		case AND:
			planFormula.append(buildAndOrSuccessFormula(plan, planFormula));
			break;
		case TRY:
			planFormula.append(buildTryOriginalFormula(plan, planFormula));
			break;
		case TRY_S:
			break;
		case TRY_F:
			break;
		default:
			planFormula.append(op + "(s" + plan.getClearElId() + "=2)");
		}
	}
	
	private String buildTryOriginalFormula(RTContainer plan, StringBuilder planFormula) throws IOException {
		String op = planFormula.length() == 0 ? "" : " & ";
		return op
		+ "(s" + plan.getClearElId() + "=2 & "
		+ buildTrySuccessFailureFormula(plan.getTrySuccess())
		+ ") | "
		+ "(s" + plan.getClearElId() + "=4 & "
		+ buildTrySuccessFailureFormula(plan.getTryFailure())
		+ ")";
	}

	private String buildTrySuccessFailureFormula(RTContainer plan) throws IOException {
		
		return plan != null ? "s" + plan.getClearElId() + "=2" : "true";
	}
	
	private String buildAndOrSuccessFormula(RTContainer plan, StringBuilder planFormula) {
		String op = planFormula.length() == 0 ? "" : " & ";
		String formula = op + "s" + plan.getClearElId() + "=2";
		if (plan.isOptional()) formula += " | s" + plan.getClearElId() + "=3";
		
		return formula;
	}
	
//	private void processPlanFormula(PlanContainer plan, StringBuilder planFormula, Const decType, boolean nonDeterminismCtx) throws IOException{
//		
//		String op = planFormula.length() == 0 ? "" : " & ";
//		String formula = op + "s" + plan.getClearElId() + "=2";
//		if (plan.isOptional()) formula += " | s" + plan.getClearElId() + "=3";
//		planFormula.append(formula);
//	}
	
	private String buildPrevFailureFormula(String prevFormula) {
		if (prevFormula == null)
			return "";
		return new String(this.prevFailurePattern);
	}

	private String buildPrevSuccessFormula(String prevFormula, PlanContainer plan) {
		if (prevFormula == null)
			return "";
		
		StringBuilder sb = new StringBuilder();
		GoalContainer parentGoal = plan.getParentGoal();
		
		if (parentGoal.getRoot() != null) {
			if (parentGoal.getRoot().getDecomposition().equals(Const.OR)) {
				sb.append("!(" + prevFormula + ") & ");
			}
			else {
				sb.append("(" + prevFormula + ") & ");
			}
		}
		return sb.toString();
	}
	
	private String buildPrevSuccessEffectFormula(String prevSuccessFormula, PlanContainer plan) {
		
		if (prevSuccessFormula.equals("")) return "";
		
		GoalContainer parentGoal = plan.getParentGoal();
		if (parentGoal.getRoot() != null) {
			if (parentGoal.getRoot().getDecomposition().equals(Const.OR)) {
				return prevSuccessFormula.substring(1, prevSuccessFormula.length());
			}
			else {
				return "!" + prevSuccessFormula;
			}
		}
		return "";
	}

	/*private void processPlanFormula(PlanContainer plan, StringBuilder planFormula, Const decType, boolean nonDeterminismCtx) throws IOException{
	
		String op = planFormula.length() == 0 ? "" : " & ";
		
		if (plan.isOptional()) {
			String formula = op + "s" + plan.getClearElId() + "=2 | s" + plan.getClearElId() + "=3";
			planFormula.append(formula);
			return;
		}
		else {
			planFormula.append(op + "(s" + plan.getClearElId() + "=2)" + buildContextSuccessFormula(decType, plan));
		}
			
		//switch(decType){
		//case OR: planFormula.append(buildAndOrSuccessFormula(plan, planFormula, decType, nonDeterminismCtx));break;
		//case AND: planFormula.append(buildAndOrSuccessFormula(plan, planFormula, decType, nonDeterminismCtx));break;				  
		//default: planFormula.append(op + "(s" + plan.getClearElId() + "=2)" + buildContextSuccessFormula(plan, nonDeterminismCtx));
		//}
	}
	
	private String buildContextSuccessFormula(Const decType, RTContainer plan) throws IOException{
		
		if (plan.getFulfillmentConditions().isEmpty()) return "";
		
		//If means-end plan
		if ((plan instanceof PlanContainer) && (plan.getRoot() instanceof GoalContainer)) { 
			decType = plan.getRoot().getRoot().getDecomposition();
		}
		
		switch(decType) {
		case OR: return "";
		case AND: return " | (s" + plan.getClearElId() + "=3 & CTX_" + getContextId(plan) + "=0)";
		}
		return "";
	}*/
	
	/*private String buildAndOrSuccessFormula(RTContainer plan, StringBuilder planFormula, Const decType, boolean nonDeterminismCtx) throws IOException{
		String op = planFormula.length() == 0 ? "" : " & ";
		switch(decType){
		case AND: return op + "(s" + plan.getClearElId() + "=2)" + buildContextSuccessFormula(plan, nonDeterminismCtx);
		case OR: return op + "(s" + plan.getClearElId() + "=2)" + buildContextSuccessFormula(plan, nonDeterminismCtx);
		default: return "";
		}		
	}

	private String buildContextSuccessFormula(RTContainer plan, boolean nonDeterminismCtx) throws IOException{
	
		if(this.constOrParam.equals("param"))
			return "";	
	
		StringBuilder sb = new StringBuilder();		
		for(String ctxCondition : plan.getFulfillmentConditions()) {
			Object [] parsedCtxs = CtxParser.parseRegex(ctxCondition);
			if((CtxSymbols)parsedCtxs[2] == CtxSymbols.COND || nonDeterminismCtx || plan.getClearElId().contains("X")){
				sb.append(sb.length() > 0 ? " | " : "").append("CTX_" + getContextId(plan) + "=0");
			}
		}
		if(sb.length() > 0)
			sb.insert(0, " | (s" + plan.getClearElId() + "=3 & ").append(")");
		return sb.toString();
	}*/
	
	private String getContextId(RTContainer plan) throws ParseCancellationException, IOException {
		/*String ctx = getContextsInfo(plan).toString();
		RTContainer node = getKeyRTContainer(this.nonDeterminismCtxList,ctx);
		
		if (this.nonDeterminismCtxList.containsValue(ctx) && (equalsRoot(node, plan))) {
			return node.getClearElId();
		}
		return plan.getClearElId();*/

		RTContainer node = ndCtxListContainsARoot(plan);
		if (node != null) {
			return node.getClearElId();
		}
		return plan.getClearElId();
	}

	/**
	 * Create an agent output dir
	 * 
	 * @param output the dir to be created
	 * 
	 * @throws CodeGenerationException
	 */
	private void writeAnOutputDir( String output ) throws CodeGenerationException {
		File dir = new File( output );
	
		if( !dir.exists() && !dir.mkdirs() ) {
			String msg = "Error: Can't create directory \"" + dir + "\"!";
			System.out.println( msg );
			throw new CodeGenerationException( msg );
		}
	}

	/**
	 * replaces all placeholders in the ADF skeleton and writes the ADF file.
	 * 
	 * @param adf the file to be written to
	 */
	private void printModel( PrintWriter adf ) {
	
		body = body.replace(GOAL_MODULES_TAG, planModules);
		reward = declareRewardVariable() + "\n" + reward.replace(REWARD_TAG, rewardModule);
	
		String model = header + "\n" + body + reward;
		ManageWriter.printModel(adf, model);
	}
	
	/*private void printEvalBash( PrintWriter pw ){

		evalBash = evalBash.replace(PARAMS_BASH_TAG, evalFormulaParams);
		evalBash = evalBash.replace(REPLACE_BASH_TAG, evalFormulaReplace);

		ManageWriter.printModel(pw, evalBash);
	}*/
	
	private String declareRewardVariable() {
		StringBuilder variables = new StringBuilder();
		for (String var : this.rewardVariables) {
			variables.append("\nconst double " + var + ";");
		}
		return variables.toString();
	}
	
	private boolean isMDP(String typeModel) {
		ModelTypeEnum enumn = ModelTypeEnum.valueOf(typeModel);
		return enumn.equals(ModelTypeEnum.MDP);
	}
}