package br.unb.cic.integration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.unb.cic.goda.model.Actor;
import br.unb.cic.goda.model.ActorImpl;
import br.unb.cic.goda.model.Goal;
import br.unb.cic.goda.model.GoalImpl;
import br.unb.cic.goda.model.Plan;
import br.unb.cic.goda.model.PlanImpl;
import br.unb.cic.goda.rtgoretoprism.action.PRISMCodeGenerationAction;
import br.unb.cic.goda.rtgoretoprism.action.RunParamAction;
import br.unb.cic.pistar.model.PistarActor;
import br.unb.cic.pistar.model.PistarLink;
import br.unb.cic.pistar.model.PistarModel;
import br.unb.cic.pistar.model.PistarNode;

@RestController
public class Controller {

    @RequestMapping(value = "/prism-dtmc", method = RequestMethod.POST)
    public void prism(@RequestParam(value = "content") String content) {
        Gson gson = new GsonBuilder().create();
        PistarModel model = gson.fromJson(content, PistarModel.class);
        Set<Actor> selectedActors = new HashSet<>();
        Set<Goal> selectedGoals = new HashSet<>();
        transformToTao4meEntities(model, selectedActors, selectedGoals);
        try {
            cleanDTMCFolder();
            new PRISMCodeGenerationAction(selectedActors, selectedGoals).run();
            FileOutputStream fos = new FileOutputStream("src/main/webapp/prism.zip");
            ZipOutputStream zos = new ZipOutputStream(fos);
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get("dtmc"));
            for (Path path : directoryStream) {
                byte[] bytes = Files.readAllBytes(path);
                zos.putNextEntry(new ZipEntry(path.getFileName().toString()));
                zos.write(bytes, 0, bytes.length);
                zos.closeEntry();
            }
            zos.close();
            cleanDTMCFolder();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping(value = "/param-dtmc", method = RequestMethod.POST)
    public void param(@RequestParam(value = "content") String content) {
        Gson gson = new GsonBuilder().create();
        PistarModel model = gson.fromJson(content, PistarModel.class);
        Set<Actor> selectedActors = new HashSet<>();
        Set<Goal> selectedGoals = new HashSet<>();
        transformToTao4meEntities(model, selectedActors, selectedGoals);
        try {
            cleanDTMCFolder();
            new RunParamAction(selectedActors, selectedGoals, true).run();
            FileOutputStream fos = new FileOutputStream("src/main/webapp/param.zip");
            ZipOutputStream zos = new ZipOutputStream(fos);
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get("dtmc"));
            for (Path path : directoryStream) {
                byte[] bytes = Files.readAllBytes(path);
                zos.putNextEntry(new ZipEntry(path.getFileName().toString()));
                zos.write(bytes, 0, bytes.length);
                zos.closeEntry();
            }
            zos.close();
            cleanDTMCFolder();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @RequestMapping(value = "/epmc-dtmc", method = RequestMethod.POST)
    public void epmc(@RequestParam(value = "content") String content) {
        Gson gson = new GsonBuilder().create();
        PistarModel model = gson.fromJson(content, PistarModel.class);
        Set<Actor> selectedActors = new HashSet<>();
        Set<Goal> selectedGoals = new HashSet<>();
        transformToTao4meEntities(model, selectedActors, selectedGoals);
        try {
            cleanDTMCFolder();
            new RunParamAction(selectedActors, selectedGoals, false).run();
            FileOutputStream fos = new FileOutputStream("src/main/webapp/epmc.zip");
            ZipOutputStream zos = new ZipOutputStream(fos);
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get("dtmc"));
            for (Path path : directoryStream) {
                byte[] bytes = Files.readAllBytes(path);
                zos.putNextEntry(new ZipEntry(path.getFileName().toString()));
                zos.write(bytes, 0, bytes.length);
                zos.closeEntry();
            }
            zos.close();
            cleanDTMCFolder();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cleanDTMCFolder() throws IOException {
        Files.walk(Paths.get("dtmc"), FileVisitOption.FOLLOW_LINKS)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(f -> {
                    if (f.isFile()) {
                        f.delete();
                    }
                });
    }

    public static void transformToTao4meEntities(PistarModel model, Set<Actor> selectedActors, Set<Goal> selectedGoals) {
        List<PistarActor> pistarActors = model.getActors();
        pistarActors.forEach(pistarActor -> {
            Actor actor = new ActorImpl(pistarActor);
            List<PistarNode> notDerivedPlans = new ArrayList<>();
            notDerivedPlans.addAll(pistarActor.getAllPlans());
            model.getLinks().forEach(pistarDependency -> {
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

    private static Goal fillDecompositionList(PistarModel model, PistarActor pistarActor, PistarNode pistarGoal, Goal goal) {
        List<PistarLink> linksToGoal = model.getLinks().stream()
                .filter(d -> d.getTarget().equals(pistarGoal.getId()) && d.getType().contains("Link"))
                .collect(Collectors.toList());
        linksToGoal.forEach(l -> {
            List<PistarNode> sourceGoals = pistarActor.getAllGoals().stream()
                    .filter(g -> l.getSource().equals(g.getId()))
                    .collect(Collectors.toList());
            if (!sourceGoals.isEmpty()) {
                if (l.getType().contains("And")) {
                    goal.setAndDecomposition(true);
                } else if (l.getType().contains("Or")) {
                    goal.setOrDecomposition(true);
                }
            }
            fillMeansToAndEndPlansList(model, pistarActor, pistarGoal, goal);
            sourceGoals.forEach(g -> {
                Goal dependencyGoal = fillDecompositionList(model, pistarActor, g, new GoalImpl(g));
                goal.addToDecompositionList(dependencyGoal);
            });
        });
        return goal;
    }

    private static void fillMeansToAndEndPlansList(PistarModel model, PistarActor pistarActor, PistarNode pistarGoal, Goal goal) {
        List<PistarLink> linksToGoal = model.getLinks().stream()
                .filter(l -> l.getTarget().equals(pistarGoal.getId()) && l.getType().contains("Link"))
                .collect(Collectors.toList());
        linksToGoal.forEach(link -> {
            List<PistarNode> sourcePlans = pistarActor.getAllPlans().stream()
                    .filter(p -> link.getSource().equals(p.getId()))
                    .collect(Collectors.toList());
            sourcePlans.forEach(sp -> {
                Plan meansToAnEndPlan = fillEndPlans(model, pistarActor, sp, new PlanImpl(sp));
                goal.addToMeansToAnEndPlans(meansToAnEndPlan);
            });
        });
    }

    private static Plan fillEndPlans(PistarModel model, PistarActor pistarActor, PistarNode pistarPlan, Plan meansToAnEndPlan) {
        List<PistarLink> linksToPlan = model.getLinks().stream()
                .filter(l -> l.getTarget().equals(pistarPlan.getId()) && l.getType().contains("Link"))
                .collect(Collectors.toList());
        linksToPlan.forEach(link -> {
            List<PistarNode> sourcePlans = pistarActor.getAllPlans().stream()
                    .filter(p -> link.getSource().equals(p.getId()))
                    .collect(Collectors.toList());
            if (!sourcePlans.isEmpty()) {
                if (link.getType().contains("And")) {
                    meansToAnEndPlan.setAndDecomposition(true);
                } else if (link.getType().contains("Or")) {
                    meansToAnEndPlan.setOrDecomposition(true);
                }
            }
            sourcePlans.forEach(p -> {
                Plan endPlan = fillEndPlans(model, pistarActor, p, new PlanImpl(p));
                meansToAnEndPlan.addToEndPlans(endPlan);
            });
        });
        return meansToAnEndPlan;
    }
}