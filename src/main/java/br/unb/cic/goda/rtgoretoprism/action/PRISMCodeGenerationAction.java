package br.unb.cic.goda.rtgoretoprism.action;

import java.io.IOException;
import java.util.Set;

import br.unb.cic.goda.model.Actor;
import br.unb.cic.goda.rtgoretoprism.generator.CodeGenerationException;
import br.unb.cic.goda.rtgoretoprism.generator.goda.producer.RTGoreProducer;
import br.unb.cic.goda.model.Goal;

public class PRISMCodeGenerationAction {

    private Set<Actor> selectedActors;
    private Set<Goal> selectedGoals;
    private String typeModel;

    public PRISMCodeGenerationAction(Set<Actor> selectedActors, Set<Goal> selectedGoals, String typeModel) {
        this.selectedActors = selectedActors;
        this.selectedGoals = selectedGoals;
        this.typeModel = typeModel;
    }

    public void run() {
        if (selectedActors.isEmpty()) {
			throw new RuntimeException("Invalid Model. Actors not selected.");
//        	return;
        }
        
        
        String sourceFolder = "src/main/resources/TemplateInput";
        String targetFolder = typeModel.toLowerCase();
        RTGoreProducer producer = new RTGoreProducer(selectedActors, selectedGoals, sourceFolder, targetFolder, typeModel);
        try {
            producer.run();
        } catch (CodeGenerationException | IOException e) {
            e.printStackTrace();
//			throw new RuntimeException(e.getMessage());
        }
    }

}