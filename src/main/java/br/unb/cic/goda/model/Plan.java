package br.unb.cic.goda.model;

import java.util.List;

public interface Plan extends GeneralEntity {

    String getNamePrefix();

    String gettA__sub_mode();

    Integer gettA__root_ngroups();

    Integer gettA__sub_block_index();

    String gettA__sub_par_set_index();

    Integer gettA__sub_seq_position();

    Integer gettA__sub_seq_number();

    void addToEndPlans(Plan plan);

    List<Plan> getEndPlans();

    boolean isAndDecomposition();

    boolean isOrDecomposition();

    void setAndDecomposition(boolean andDecomposition);

    void setOrDecomposition(boolean orDecomposition);

    String getMode();

    void setMode(String value);

    String getAttribute();

    void setAttribute(String value);

    String getCreationProperty();

    String getInvariantProperty();

    String getFulfillmentProperty();
}
