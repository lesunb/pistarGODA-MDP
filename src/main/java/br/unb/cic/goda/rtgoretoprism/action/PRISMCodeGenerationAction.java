package br.unb.cic.goda.rtgoretoprism.action;

import br.unb.cic.goda.model.Actor;
import br.unb.cic.goda.model.Goal;
import br.unb.cic.goda.rtgoretoprism.generator.CodeGenerationException;
import br.unb.cic.goda.rtgoretoprism.generator.goda.producer.RTGoreProducer;

import java.io.IOException;
import java.util.Set;

public class PRISMCodeGenerationAction {

    private Set<Actor> selectedActors;
    private Set<Goal> selectedGoals;

    public PRISMCodeGenerationAction(Set<Actor> selectedActors, Set<Goal> selectedGoals) {
        this.selectedActors = selectedActors;
        this.selectedGoals = selectedGoals;
    }

    public void run() {
        if (selectedActors.isEmpty())
            return;
        String sourceFolder = "src/main/resources/TemplateInput";
        String targetFolder = "dtmc";
        RTGoreProducer producer = new RTGoreProducer(selectedActors, selectedGoals, sourceFolder, targetFolder);
        try {
            producer.run();
        } catch (CodeGenerationException | IOException e) {
            e.printStackTrace();
        }
    }

}