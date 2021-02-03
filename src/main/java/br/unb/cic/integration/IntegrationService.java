package br.unb.cic.integration;

import static br.unb.cic.goda.rtgoretoprism.util.SintaticAnaliser.verifySintaxModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.management.RuntimeErrorException;

import br.unb.cic.goda.model.*;
import br.unb.cic.goda.rtgoretoprism.generator.CodeGenerationException;
import br.unb.cic.goda.rtgoretoprism.generator.goda.producer.PARAMProducer;
import br.unb.cic.goda.rtgoretoprism.generator.goda.writer.ManageWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.unb.cic.goda.rtgoretoprism.action.PRISMCodeGenerationAction;
import br.unb.cic.goda.rtgoretoprism.action.RunParamAction;
import br.unb.cic.pistar.model.PistarActor;
import br.unb.cic.pistar.model.PistarLink;
import br.unb.cic.pistar.model.PistarModel;
import br.unb.cic.pistar.model.PistarNode;

@Service
public class IntegrationService {
	public void executePrism(String content, String typeModel, String output) {
		Gson gson = new GsonBuilder().create();
		PistarModel model = gson.fromJson(content, PistarModel.class);
		Set<Actor> selectedActors = new HashSet<>();
		Set<Goal> selectedGoals = new HashSet<>();
		transformToTao4meEntities(model, selectedActors, selectedGoals, typeModel);
		try {
			cleanFolder(typeModel.toLowerCase());
			new PRISMCodeGenerationAction(selectedActors, selectedGoals, typeModel).run();
			FileOutputStream fos = new FileOutputStream(output);
			ZipOutputStream zos = new ZipOutputStream(fos);
			DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(typeModel.toLowerCase()));
			for (Path path : directoryStream) {
				byte[] bytes = Files.readAllBytes(path);
				zos.putNextEntry(new ZipEntry(path.getFileName().toString()));
				zos.write(bytes, 0, bytes.length);
				zos.closeEntry();
			}
			zos.close();
			cleanFolder(typeModel.toLowerCase());
		} catch (IOException ex) {
			ex.printStackTrace();
//			throw new RuntimeException(ex.getMessage());
		}
	}

	public void executeParam(String content, String typeModel, Boolean isParam, String output) {
		Gson gson = new GsonBuilder().create();
		PistarModel model = gson.fromJson(content, PistarModel.class);
		Set<Actor> selectedActors = new HashSet<>();
		Set<Goal> selectedGoals = new HashSet<>();
		transformToTao4meEntities(model, selectedActors, selectedGoals, typeModel);
		try {
			cleanFolder(typeModel.toLowerCase());
			new RunParamAction(selectedActors, selectedGoals, isParam, typeModel).run();
			FileOutputStream fos = new FileOutputStream(output);
			ZipOutputStream zos = new ZipOutputStream(fos);
			DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(typeModel.toLowerCase()));
			for (Path path : directoryStream) {
				byte[] bytes = Files.readAllBytes(path);
				zos.putNextEntry(new ZipEntry(path.getFileName().toString()));
				zos.write(bytes, 0, bytes.length);
				zos.closeEntry();
			}
			zos.close();
			cleanFolder(typeModel.toLowerCase());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
	}

	private void cleanFolder(String typeModel) throws IOException {
		if(Files.exists(Paths.get(typeModel))) {
			Files.walk(Paths.get(typeModel), FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder()).map(Path::toFile)
			.forEach(f -> {
				if (f.isFile()) {
					f.delete();
				}
			});
		}
	}

	public static void transformToTao4meEntities(PistarModel model, Set<Actor> selectedActors,
			Set<Goal> selectedGoals, String typeModel) {
		List<PistarActor> pistarActors = model.getActors();
		pistarActors.forEach(pistarActor -> {
			verifyNodeSintax(pistarActor.getNodes(), typeModel);
			Actor actor = new ActorImpl(pistarActor);
			List<PistarNode> notDerivedPlans = new ArrayList<>();
			notDerivedPlans.addAll(pistarActor.getAllPlans());
			model.getLinks().forEach(pistarDependency -> {
				addNotationByDecomposition(pistarDependency, model.getLinks(), pistarActor.getNodes());
				pistarActor.getAllPlans().forEach(pistarPlan -> {
					if (pistarDependency.getSource().equals(pistarPlan.getId())) {
						boolean planTargetIsGoal = pistarActor.getAllGoals().stream()
								.filter(a -> a.getId().equals(pistarDependency.getTarget()))
								.collect(Collectors.toList()).isEmpty();
						if (planTargetIsGoal) {
							notDerivedPlans.remove(pistarPlan);
						}
					}
				});
			});
			notDerivedPlans.forEach(notDerivedPlan -> {
				Plan plan = new PlanImpl(notDerivedPlan);
				actor.addToPlanList(plan);
			});
			pistarActor.getAllGoals().forEach(pistarGoal -> {
				Goal goal = fillDecompositionList(model, pistarActor, pistarGoal, new GoalImpl(pistarGoal));
				boolean isRootGoal = model.getLinks().stream().noneMatch(l -> l.getSource().equals(pistarGoal.getId()));
				goal.setRootGoal(isRootGoal);
				actor.addHardGoal(goal);
				if (goal.isSelected()) {
					selectedGoals.add(goal);
					if (!selectedActors.contains(actor)) {
						selectedActors.add(actor);
					}
				}
			});
		});
	}

	
	private static Goal fillDecompositionList(PistarModel model, PistarActor pistarActor, PistarNode pistarGoal,
			Goal goal) {
		List<PistarLink> linksToGoal = model.getLinks().stream()
				.filter(d -> d.getTarget().equals(pistarGoal.getId()) && d.getType().contains("Link"))
				.collect(Collectors.toList());
		
		List<PistarLink> linksToChildrenGoal = new ArrayList<PistarLink>();
		for(PistarLink l: linksToGoal){
			linksToChildrenGoal = model.getLinks().stream()
				.filter(d -> ( d.getTarget().equals(l.getSource()) || d.getTarget().equals(l.getTarget()) || d.getSource().equals(l.getSource()) || d.getSource().equals(l.getTarget())) 
							&& (d.getType().contains("TryRefinementLink") || d.getType().contains("ParalelRefinementLink")))
				.collect(Collectors.toList());
		}
		
		linksToGoal.addAll(linksToChildrenGoal);
		for(PistarLink l: linksToGoal){
			List<PistarNode> sourceGoals = pistarActor.getAllGoals().stream()
					.filter(g -> l.getSource().equals(g.getId()))
					.collect(Collectors.toList());
			if (!sourceGoals.isEmpty() || !linksToChildrenGoal.isEmpty()) {
				String type = l.getType();
				if (type.contains("AndRefinementLink")) {
					goal.setAndDecomposition(true);
				} else if (type.contains("OrRefinementLink")) {
					goal.setOrDecomposition(true);
				} else if (type.contains("OrParalelRefinementLink")) {
					goal.setOrParalelDecomposition(true);
				} else if (type.contains("AndParalelRefinementLink")) {
					goal.setAndParalelDecomposition(true);
				} else if (type.contains("TryRefinementLink")) {
					goal.setTryDecomposition(true);
				} else if (type.contains("RetryRefinementLink")) {
					goal.setRetryDecomposition(true);
				}
			}
			fillMeansToAndEndPlansList(model, pistarActor, pistarGoal, goal);
			sourceGoals.forEach(g -> {
				Goal dependencyGoal = fillDecompositionList(model, pistarActor, g, new GoalImpl(g));
				goal.addToDecompositionList(dependencyGoal);
			});
		}
		return goal;
	}

	private static void fillMeansToAndEndPlansList(PistarModel model, PistarActor pistarActor, PistarNode pistarGoal,
			Goal goal) {
		List<PistarLink> linksToGoal = model.getLinks().stream()
				.filter(l -> l.getTarget().equals(pistarGoal.getId()) && l.getType().contains("Link"))
				.collect(Collectors.toList());

		linksToGoal.forEach(link -> {
			List<PistarNode> sourcePlans = pistarActor.getAllPlans().stream()
					.filter(p -> link.getSource().equals(p.getId())).collect(Collectors.toList());
			sourcePlans.forEach(sp -> {
				Plan meansToAnEndPlan = fillEndPlans(model, pistarActor, sp, new PlanImpl(sp));
				goal.addToMeansToAnEndPlans(meansToAnEndPlan);
			});
		});
	}

	private static Plan fillEndPlans(PistarModel model, PistarActor pistarActor, PistarNode pistarPlan,
			Plan meansToAnEndPlan) {
		List<PistarLink> linksToPlan = model.getLinks().stream()
				.filter(l -> l.getTarget().equals(pistarPlan.getId()) && l.getType().contains("Link"))
				.collect(Collectors.toList());

		linksToPlan.forEach(link -> {
			List<PistarNode> sourcePlans = pistarActor.getAllPlans().stream()
					.filter(p -> link.getSource().equals(p.getId())).collect(Collectors.toList());
			if (!sourcePlans.isEmpty()) {
				String type = link.getType();
				if (type.contains("AndRefinementLink")) {
					meansToAnEndPlan.setAndDecomposition(true);
				} else if (type.contains("OrRefinementLink")) {
					meansToAnEndPlan.setOrDecomposition(true);
				} else if (type.contains("OrParalelRefinementLink")) {
					meansToAnEndPlan.setOrParalelDecomposition(true);
				} else if (type.contains("AndParalelRefinementLink")) {
					meansToAnEndPlan.setAndParalelDecomposition(true);
				} else if (type.contains("TryRefinementLink")) {
					meansToAnEndPlan.setTryDecomposition(true);
				} else if (type.contains("RetryRefinementLink")) {
					meansToAnEndPlan.setRetryDecomposition(true);
				}
			}
			sourcePlans.forEach(p -> {
				Plan endPlan = fillEndPlans(model, pistarActor, p, new PlanImpl(p));
				meansToAnEndPlan.addToEndPlans(endPlan);
			});
		});
		return meansToAnEndPlan;
	}
	
	// cria relacionamentos atraves de links entre target e source
	@SuppressWarnings("unused")
	private static void addNotationByDecomposition(PistarLink linkCurrent, List<PistarLink> links,
			List<PistarNode> nodes) {

//		configNotationRetryInNodes(nodes);
		configNotationParalelAndTryInNodes(linkCurrent, links, nodes);
		
	}
	
	@SuppressWarnings("unused")
	private static void verifyNodeSintax(List<PistarNode> nodes, String typeModel) {
		for (PistarNode node : nodes) {
			//Verifica a Sintaxe para cada nó
			//Settando o novo texto apenas para remover espaços em branco indesejados no nome do nó
			node.setText(verifySintaxModel(node.getText(), typeModel)) ;
		}
		
	}

	private static void configNotationParalelAndTryInNodes(PistarLink linkCurrent, List<PistarLink> links, List<PistarNode> nodes) {
		if ((linkCurrent.getType().contains("OrParalelRefinementLink"))
				|| (linkCurrent.getType().contains("AndParalelRefinementLink"))
				|| (linkCurrent.getType().contains("TryRefinementLink"))) {
			List<String> linksFiltrados = new ArrayList<String>();

			// verifica se o link atual tem associação com outros links,
			// para descobrir qual o Objeto pai dos links e anotá-lo com a decomposition
			// correta
			// Funcao principal do LOOP: Recuperar o objeto pai dos links
			for (PistarLink link : links) {
				if (!link.getId().equals(linkCurrent.getId())) {
					if (link.getTarget().equals(linkCurrent.getTarget())
							|| link.getTarget().equals(linkCurrent.getSource())) {
						linksFiltrados.add(link.getSource());
					} else if (link.getSource().equals(linkCurrent.getTarget())
							|| link.getSource().equals(linkCurrent.getSource())) {
						linksFiltrados.add(link.getTarget());
					}
				}
				linksFiltrados = linksFiltrados.stream().distinct().collect(Collectors.toList());
			}

			String nomeDestino = "";
			String nomeOrigem = "";
			// recuperar nomes de origem e destino
			for (PistarNode node : nodes) {
				String texto = handlerNameNode(node.getText());
				if (node.getId().equals(linkCurrent.getSource())) {
					nomeOrigem = texto;
				} else if (node.getId().equals(linkCurrent.getTarget())) {
					nomeDestino = texto;
				}
			}

			for (String linkFiltrado : linksFiltrados) {
				for (PistarNode node : nodes) {
					String texto = handlerNameNode(node.getText());
					if (node.getId().equals(linkFiltrado)) {
						if (linkCurrent.getType().contains("TryRefinementLink")) {
							texto = replaceNotationByDecomposition("[try(" + nomeDestino + ")?" + nomeOrigem  + ":skip]",
									node.getText());
							node.setText(texto);
						} else {
							texto = replaceNotationByDecomposition("[" + nomeDestino + "#" + nomeOrigem + "]",
									node.getText());
							node.setText(texto);
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	private static void configNotationRetryInNodes(List<PistarNode> nodes) {
		List<PistarNode> retryNodes = new ArrayList<PistarNode>();
		
		for (PistarNode node : nodes) {
			//Verificar se existe property do tipo Retry
			if(node.getCustomProperties() != null && node.getCustomProperties().get("selectRetry") != null) {
				retryNodes.add(node);
			}
		}

	}

	private static String replaceNotationByDecomposition(String label, String textoOrig) {
		if (!textoOrig.contains(label)) {
			textoOrig = textoOrig + label;
		}

		return textoOrig;
	}
	
	//	recupera apenas o nome principal do nó
	private static String handlerNameNode(String text) {
		int tam = (text.indexOf(":") > 0 ? text.indexOf(":") : text.length());
		return text.substring(0, tam);
	}

	public String getReliabilityFormulaTree(String id, String goal) {
		return loadFormulaTreeFromJson(id, goal, true);
	}

	public String getCostFormulaTree(String id, String goal) {
		return loadFormulaTreeFromJson(id, goal, false);
	}

	public String loadFormulaTreeFromJson(String id, String goal, boolean isReliability) {
		String content;
		try {
			if (isReliability) {
				content = ManageWriter.readFileAsString("resources/reliability/" + id + "_reliability.json");
			} else {
				content = ManageWriter.readFileAsString("resources/cost/" + id + "_cost.json");
			}

			ObjectMapper objectMapper = new ObjectMapper();
			FormulaTreeNode formulaTree = objectMapper.readValue(content, FormulaTreeNode.class);
			formulaTree = getFormulaSubTree(formulaTree, goal);

			String json = objectMapper.writeValueAsString(formulaTree);
			return json;
		} catch(Exception e) {
			return "";
		}
	}

	private FormulaTreeNode getFormulaSubTree(FormulaTreeNode formulaTreeNode, String goal) {
		Queue<FormulaTreeNode> formulaTreeNodeQueue = new LinkedList<>();
		Set<String> visited = new HashSet<>();
		FormulaTreeNode node = null;

		formulaTreeNodeQueue.add(formulaTreeNode);
		visited.add(formulaTreeNode.id);

		while (!formulaTreeNodeQueue.isEmpty()) {
			node = formulaTreeNodeQueue.remove();
			if (node.id.equals(goal)) {
				break;
			} else {
				node.subNodes.forEach(subNode -> {
					if (!visited.contains(subNode.id)) {
						visited.add(subNode.id);
						formulaTreeNodeQueue.add((subNode));
					}
				});
			}
		}

		return node;
	}

}
