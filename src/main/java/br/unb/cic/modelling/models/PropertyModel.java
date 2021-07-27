package br.unb.cic.modelling.models;

import java.util.ArrayList;
import java.util.List;

import br.unb.cic.modelling.enums.TypesAttributesEnum;

public class PropertyModel {
	private String name;
	private String value = "";
	private boolean checked = false;
	private TypesAttributesEnum type;
	private List<PropertyModel> childrens = new ArrayList<PropertyModel>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<PropertyModel> getChildrens() {
		return childrens;
	}
	public void setChildrens(List<PropertyModel> childrens) {
		this.childrens = childrens;
	}
	public TypesAttributesEnum getType() {
		return type;
	}
	public void setType(TypesAttributesEnum type) {
		this.type = type;
		// o padrao para checkbox vai ser falso
		if(this.type.equals(TypesAttributesEnum.BOOLEAN)) {
			this.value = "false";
		}
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean getChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
}
