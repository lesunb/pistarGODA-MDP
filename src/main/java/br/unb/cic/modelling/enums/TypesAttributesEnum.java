package br.unb.cic.modelling.enums;

public enum TypesAttributesEnum {
	BOOLEAN("BOOLEAN"),
	CHECKBOX("CHECKBOX"),
	RADIO_BUTTON("RADIO_BUTTON"),
	EXPRESSION("EXPRESSION"),
	LIST("LIST"),
	OBJECT("OBJECT"),
	OBJECT_SELECTABLE("OBJECT_SELECTABLE"),
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
