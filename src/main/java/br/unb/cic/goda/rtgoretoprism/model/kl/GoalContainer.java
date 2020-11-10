package br.unb.cic.goda.rtgoretoprism.model.kl;

import br.unb.cic.goda.model.Goal;

import java.util.ArrayList;

public class GoalContainer extends RTContainer {
    public final Const achieve;
    public Const request;
    private ArrayList<String[]> dependencies;
    private ArrayList<GoalContainer> parentlist;
    private String formula;

    public GoalContainer(Goal goal, Const achieve) {
        super(goal);
        this.achieve = achieve;
        this.request = Const.NONE;
        parentlist = new ArrayList<>();
        dependencies = new ArrayList<>();
        this.addFulfillmentConditions(goal.getCreationProperty());
    }

    public void setRequest(Const request) {
        this.request = request;
    }

    public PlanContainer addMERealPlan(PlanContainer child) {
        plans.add(child);
        child.setRoot(this);
        if (decomposition == Const.OR || decomposition == Const.ME) {
            assert decomposition == Const.ME;// otherwise there is an error elsewhere!
            // needed to add more triggering goals to one real plan
            child.addMEGoal(this);
        }
        return child;
    }

    public GoalContainer addDecomp(GoalContainer child) {
        goals.add(child);
        child.setRoot(this);
        if (decomposition == Const.OR || decomposition == Const.ME) {
            child.addParent(this);
        }
        return child;
    }

    public ArrayList<GoalContainer> getParentGoals() {
        return parentlist;
    }

    private void addParent(GoalContainer gc) {
        parentlist.add(gc);
    }

    public void addDependency(String dependum, String dependee) {
        // dependencies.add(dep);
        dependencies.add(new String[]{dependum, dependee});
    }

    public ArrayList<String[]> getDependencies() {
        return dependencies;
    }

    public String getActorFromDependency(String[] dep) {
        return dep[1];
    }

    public String getDependumGoalFromDependency(String[] dep) {
        return dep[0];
    }

    public void setRoot(RTContainer root) {
        super.setRoot(root);
        setUid(super.getElId());
    }

    public String getFormula() { return this.formula; }

    public void setFormula(String formula) { this.formula = formula; }
}