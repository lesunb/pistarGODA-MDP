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
    private String typeModel;

    public RunParamAction() {
    }

    public RunParamAction(boolean isParam) {
        this.isParam = isParam;
    }
    
    public RunParamAction(Set<Actor> selectedActors, Set<Goal> selectedGoals, boolean isParam, String typeModel) {
        this.selectedActors = selectedActors;
        this.selectedGoals = selectedGoals;
        this.isParam = isParam;
        this.typeModel = typeModel;
    }

    public void run() throws Exception {
        if (selectedActors.isEmpty())
            return;
        String sourceFolder = "src/main/resources/TemplateInput";
        String targetFolder = typeModel.toLowerCase();
        String toolsFolder = "tools";
        PARAMProducer producer = new PARAMProducer(selectedActors, selectedGoals, isParam, sourceFolder, targetFolder, toolsFolder, this.typeModel);
        try {
            producer.run();
        } catch (CodeGenerationException | IOException ex) {
			ex.printStackTrace();
//			throw new RuntimeException(ex.getMessage());
        } catch (Exception ex) {
			ex.printStackTrace();
			// TODO Auto-generated catch block
//			throw new RuntimeException(ex.getMessage());
		}
    }

	public Set<Actor> getSelectedActors() {
		return selectedActors;
	}

	public void setSelectedActors(Set<Actor> selectedActors) {
		this.selectedActors = selectedActors;
	}

	public Set<Goal> getSelectedGoals() {
		return selectedGoals;
	}

	public void setSelectedGoals(Set<Goal> selectedGoals) {
		this.selectedGoals = selectedGoals;
	}

	public boolean isParam() {
		return isParam;
	}

	public void setParam(boolean isParam) {
		this.isParam = isParam;
	}


}