package br.unb.cic.goda.model;

import java.util.List;

public interface Goal extends GeneralEntity {

    String getMode();

    void setMode(String value);

    String getAttribute();

    void setAttribute(String value);

    String getCreationProperty();

    void setCreationProperty(String value);

    String getInvariantProperty();

    void setInvariantProperty(String value);

    String getFulfillmentProperty();

    void setFulfillmentProperty(String value);

    boolean isRootGoal();

    void setRootGoal(boolean rootGoal);

    List<Goal> getDecompositionList();

    void addToDecompositionList(Goal goal);

    List<Plan> getMeansToAnEndPlans();

    void addToMeansToAnEndPlans(Plan plan);

    boolean isAndDecomposition();

    boolean isOrDecomposition();

    void setAndDecomposition(boolean andDecomposition);

    void setOrDecomposition(boolean orDecomposition);

    boolean isSelected();

}
