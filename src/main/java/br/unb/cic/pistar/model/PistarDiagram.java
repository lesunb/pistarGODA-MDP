package br.unb.cic.pistar.model;

import java.math.BigDecimal;

public class PistarDiagram {

    private BigDecimal width;
    private BigDecimal height;
    private String name;
    private Object customProperties;

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

	public Object getCustomProperties() {
		return customProperties;
	}

	public void setCustomProperties(Object customProperties) {
		this.customProperties = customProperties;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
