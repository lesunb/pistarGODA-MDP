package br.unb.cic.modelling.models.goal;

import java.util.ArrayList;
import java.util.List;

import br.unb.cic.modelling.enums.TypesAttributesEnum;
import br.unb.cic.modelling.models.PropertyModel;

//Context - Atualmente esse atributo se chama CreationCondition mas seria
//interessante se fosse Context. Esse atributo tem um tipo, podendo ser
//Trigger ou Condition.
//a. No caso de um tipo Trigger, temos apenas uma lista de eventos (que
//são apenas nomes). Atualmente essa lista é separada por vírgulas
//mas pode-se pensar em uma maneira mais organizada.
//b. No caso de um tipo condition, temos uma condição que será apenas
//uma caixa de texto. O programa de decomposição que utilizará esse
//modelo cuidará de realizar as verificações necessárias a respeito
//dessa condição
public class Context extends PropertyModel{

	public Context() {
		this.setName("Context");
		this.setType(TypesAttributesEnum.OBJECT);
		this.setChildrens();
		
	}
	
	private void setChildrens() {
		List<PropertyModel> childrens = new ArrayList<PropertyModel>();
		PropertyModel trigger = new PropertyModel();
		trigger.setType(TypesAttributesEnum.TEXT);
		trigger.setName("Trigger");
		trigger.setChecked(true);

		PropertyModel condition = new PropertyModel();
		condition.setType(TypesAttributesEnum.TEXT);
		condition.setName("Condition");

		childrens.add(trigger);
		childrens.add(condition);
		this.setValue(trigger.getName());
		
		super.setChildrens(childrens);
	}
}
