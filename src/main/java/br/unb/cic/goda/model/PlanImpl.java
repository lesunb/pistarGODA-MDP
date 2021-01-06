package br.unb.cic.goda.model;

import br.unb.cic.pistar.model.PistarNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlanImpl implements Plan, Serializable {
	private static final long serialVersionUID = 1L;
	private String tA__sub_mode;
    private Integer tA__root_ngroups;
    private Integer tA__sub_block_index;
    private String tA__sub_par_set_index;
    private Integer tA__sub_seq_position;
    private Integer tA__sub_seq_number;
    private Goal isFulfilled = null;
    private String mode;
    private String attribute;
    private String selectRetry;
    private String creationProperty;
    private String invariantProperty;
    private String fulfillmentProperty;
    private String name;
    private List<Plan> endPlans = new ArrayList<>();
    private boolean isAndDecomposition = false;
    private boolean isOrDecomposition = false;
    private boolean isAndParalelDecomposition = false;
    private boolean isOrParalelDecomposition = false;
    private boolean isTryDecomposition = false;
    private boolean isRetryDecomposition = false;

    public PlanImpl(PistarNode pistarPlan) {
        this.name = pistarPlan.getText();
        if (pistarPlan.getCustomProperties() != null) {
            pistarPlan.getCustomProperties().forEach((key, value) -> {
                switch (key) {
                    case "attribute":
                        attribute = value;
                        break;
                    case "selectRetry":
                    	setSelectRetry(value);
                        isRetryDecomposition = true;
                        break;
                    case "creationProperty":
                        creationProperty = value;
                        break;
                    case "fulfillmentProperty":
                        fulfillmentProperty = value;
                        break;
                    case "invariantProperty":
                        invariantProperty = value;
                        break;
                    case "mode":
                        mode = value;
                        break;
                    case "TA_root_ngroups":
                        tA__root_ngroups = Integer.valueOf(value);
                        break;
                    case "TA_sub_block_index":
                        tA__sub_block_index = Integer.valueOf(value);
                        break;
                    case "TA_sub_mode":
                        tA__sub_mode = value;
                        break;
                    case "TA_sub_par_set_index":
                        tA__sub_par_set_index = value;
                        break;
                    case "TA_sub_seq_number":
                        tA__sub_seq_number = Integer.valueOf(value);
                        break;
                    case "TA_sub_seq_position":
                        tA__sub_seq_position = Integer.valueOf(value);
                        break;
                }
            });
        }
    }

    @Override
    public void addToEndPlans(Plan endPlan) {
        endPlans.add(endPlan);
    }

    @Override
    public List<Plan> getEndPlans() {
        return endPlans;
    }

    public String getNamePrefix() {
        return "Plan ";
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String gettA__sub_mode() {
        return tA__sub_mode;
    }

    public void settA__sub_mode(String tA__sub_mode) {
        this.tA__sub_mode = tA__sub_mode;
    }

    @Override
    public Integer gettA__root_ngroups() {
        return tA__root_ngroups;
    }

    public void settA__root_ngroups(Integer tA__root_ngroups) {
        this.tA__root_ngroups = tA__root_ngroups;
    }

    @Override
    public Integer gettA__sub_block_index() {
        return tA__sub_block_index;
    }

    public void settA__sub_block_index(Integer tA__sub_block_index) {
        this.tA__sub_block_index = tA__sub_block_index;
    }

    @Override
    public String gettA__sub_par_set_index() {
        return tA__sub_par_set_index;
    }

    public void settA__sub_par_set_index(String tA__sub_par_set_index) {
        this.tA__sub_par_set_index = tA__sub_par_set_index;
    }

    @Override
    public Integer gettA__sub_seq_position() {
        return tA__sub_seq_position;
    }

    public void settA__sub_seq_position(Integer tA__sub_seq_position) {
        this.tA__sub_seq_position = tA__sub_seq_position;
    }

    @Override
    public Integer gettA__sub_seq_number() {
        return tA__sub_seq_number;
    }

    public void settA__sub_seq_number(Integer tA__sub_seq_number) {
        this.tA__sub_seq_number = tA__sub_seq_number;
    }

    public Goal getIsFulfilled() {
        return isFulfilled;
    }

    public void setIsFulfilled(Goal isFulfilled) {
        this.isFulfilled = isFulfilled;
    }

    @Override
    public String getMode() {
        return mode;
    }

    @Override
    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String getAttribute() {
        return attribute;
    }

    @Override
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getCreationProperty() {
        return creationProperty;
    }

    public void setCreationProperty(String creationProperty) {
        this.creationProperty = creationProperty;
    }

    @Override
    public String getInvariantProperty() {
        return invariantProperty;
    }

    public void setInvariantProperty(String invariantProperty) {
        this.invariantProperty = invariantProperty;
    }

    @Override
    public String getFulfillmentProperty() {
        return fulfillmentProperty;
    }

    public void setFulfillmentProperty(String fulfillmentProperty) {
        this.fulfillmentProperty = fulfillmentProperty;
    }


	@Override
	public boolean isAndDecomposition() {
		return this.isAndDecomposition;
	}

	@Override
	public boolean isOrDecomposition() {
		return this.isOrDecomposition;
	}

	@Override
	public boolean isAndParalelDecomposition() {
		return this.isAndParalelDecomposition;
	}

	@Override
	public boolean isOrParalelDecomposition() {
		return this.isOrParalelDecomposition;
	}

	@Override
	public boolean isTryDecomposition() {
		return this.isTryDecomposition;
	}

	@Override
	public boolean isRetryDecomposition() {
		return this.isRetryDecomposition;
	}

	@Override
	public void setAndDecomposition(boolean andDecomposition) {
		this.isAndDecomposition = andDecomposition;
	}

	@Override
	public void setOrDecomposition(boolean orDecomposition) {
		this.isOrDecomposition = orDecomposition;
	}

	@Override
	public void setAndParalelDecomposition(boolean andPDecomposition) {
		this.isAndParalelDecomposition = andPDecomposition;
	}

	@Override
	public void setOrParalelDecomposition(boolean orPDecomposition) {
		this.isOrParalelDecomposition = orPDecomposition;
	}

	@Override
	public void setTryDecomposition(boolean tryDecomposition) {
		this.isTryDecomposition = tryDecomposition;
	}

	@Override
	public void setRetryDecomposition(boolean retryDecomposition) {
		this.isRetryDecomposition = retryDecomposition;
	}

	public String getSelectRetry() {
		return selectRetry;
	}

	public void setSelectRetry(String selectRetry) {
		this.selectRetry = selectRetry;
	}
}