package br.unb.cic.goda.rtgoretoprism.action;

import br.unb.cic.goda.model.Actor;
import br.unb.cic.goda.model.Goal;
import br.unb.cic.goda.rtgoretoprism.generator.CodeGenerationException;
import br.unb.cic.goda.rtgoretoprism.generator.goda.producer.PARAMProducer;

import java.io.IOException;
import java.util.Set;

public class RunParamAction {

    private Set<Actor> selectedActors;
    private Set<Goal> selectedGoals;
    private boolean isParam;

    public RunParamAction(Set<Actor> selectedActors, Set<Goal> selectedGoals, boolean isParam) {
        this.selectedActors = selectedActors;
        this.selectedGoals = selectedGoals;
        this.isParam = isParam;
    }

    public void run() throws Exception {
        if (selectedActors.isEmpty())
            return;
        String sourceFolder = "src/main/resources/TemplateInput";
        String targetFolder = "dtmc";
        String toolsFolder = "tools";
        PARAMProducer producer = new PARAMProducer(selectedActors, selectedGoals, isParam, sourceFolder, targetFolder, toolsFolder);
        try {
            producer.run();
        } catch (CodeGenerationException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
    }

}