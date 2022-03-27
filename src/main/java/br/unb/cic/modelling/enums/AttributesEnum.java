package br.unb.cic.modelling.enums;

public enum AttributesEnum {
	GOAL("GOAL"),
	TASK("TASK");
	
	private String attr;
	
	AttributesEnum(String attr){
		this.attr = attr;
	}

	public boolean equals(String attr) {
		return this.attr.equals(attr);
	}


	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}
}
