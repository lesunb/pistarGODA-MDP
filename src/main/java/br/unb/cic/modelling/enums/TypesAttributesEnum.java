package br.unb.cic.modelling.enums;

public enum TypesAttributesEnum {
	CHECKBOX("CHECKBOX"),
	EXPRESSION("EXPRESSION"),
	LIST("LIST"),
	OBJECT("OBJECT"),
	TEXT("TEXT");
	
	private String attr;
	
	TypesAttributesEnum(String attr){
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
