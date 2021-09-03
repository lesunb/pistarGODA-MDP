package br.unb.cic.modelling.models.goal;

import java.util.ArrayList;
import java.util.List;

import br.unb.cic.modelling.enums.TypesAttributesEnum;
import br.unb.cic.modelling.models.PropertyModel;

//GoalType - Esse atributo define tipos de objetivo, onde temos três: Achieve,
//Query e Perform. A ideia é ter um dropdown onde o usuário pode escolher
//um dos tipos, sendo o Perform o tipo padrão (default)
//a. Achieve: Caso uma Goal do tipo Achieve seja escolhida, a opção do
//atributo AchieveCondition deve aparecer. As especificidades
//relacionadas à isto serão descritas posteriormente.
//b. Query: Caso uma Goal do tipo Query seja escolhida, a opção do
//atributo QueriedProperty deve aparecer. As especificidades
//relacionadas à isto serão descritas posteriormente
public class GoalType extends PropertyModel {
	
	public GoalType() {
		this.setChecked(true);
		this.setName("GoalType");
		this.setType(TypesAttributesEnum.OBJECT);
		this.setChildrens();
	}
	
	private void setChildrens() {
		
		List<PropertyModel> childrens = new ArrayList<PropertyModel>();
		
		PropertyModel achieve = new AchieveCondition();
		PropertyModel query = new Query();
		PropertyModel perform = new Perform();
		this.setValue(perform.getName());

		childrens.add(query);
		childrens.add(achieve);
		childrens.add(perform);
		
		
		super.setChildrens(childrens);
	}
}