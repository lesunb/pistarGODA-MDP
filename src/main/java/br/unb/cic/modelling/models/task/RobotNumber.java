package br.unb.cic.modelling.models.task;

import java.util.ArrayList;
import java.util.List;

import br.unb.cic.modelling.enums.TypesAttributesEnum;
import br.unb.cic.modelling.models.PropertyModel;

public class RobotNumber extends PropertyModel{

	public RobotNumber() {
		this.setName("RobotNumber");
		this.setType(TypesAttributesEnum.OBJECT);
		this.setChecked(true);
		this.setChildrens();
	}
	
	private void setChildrens() {
		List<PropertyModel> childrens = new ArrayList<PropertyModel>();
		PropertyModel fixed = new PropertyModel();
		fixed.setType(TypesAttributesEnum.TEXT);
		fixed.setChecked(true);
		fixed.setName("Fixed");
		
		PropertyModel range = new PropertyModel();
		range.setType(TypesAttributesEnum.LIST);
		range.setName("range");
		range.setChecked(false);
		
		
		List<String> list = new ArrayList<String>();
		list.add("min");
		list.add("max");
		range.setList(list);
		
		
		childrens.add(fixed);
		childrens.add(range);
		
		this.setValue(fixed.getName());
		super.setChildrens(childrens);
	}
}
