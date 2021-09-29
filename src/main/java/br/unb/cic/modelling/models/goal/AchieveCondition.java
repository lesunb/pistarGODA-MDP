package br.unb.cic.modelling.models.goal;

import java.util.ArrayList;
import java.util.List;

import br.unb.cic.modelling.enums.TypesAttributesEnum;
import br.unb.cic.modelling.models.PropertyModel;

//AchieveCondition - Esse atributo que surge quando temos uma Goal do tipo
//Achieve pode ter ser de duas maneiras. Deve-se ter uma opção de selecionar se ela é Universal ou não.

//a. Caso não seja Universal, apenas deve-se ter uma simples caixa de
//texto onde o usuário irá escrever uma condição. Tal condição será
//similar à condição que pode-se ter no atributo Context.
//b. No caso que é Universal, temos uma expressão OCL do tipo forAll.
//Tal expressão tem o seguinte formato:
//	[Iterated_Var]->forAll([Iteration_Var] | [Condition])

//Nesse caso temos:
//● Iterated_Var: O nome de uma variável, que deve ter sido
//declarada na lista Monitors.
//● Iteration_Var: O nome de uma variável, que deve ter sido
//declarada na lista de variáveis Controls.
//● Condition: Uma condição, que é um texto declarado da
//mesma maneira que no caso de não ser Universal.
public class AchieveCondition extends PropertyModel {
	
	public AchieveCondition() {
		this.setName("Achieve");
		this.setType(TypesAttributesEnum.OBJECT);
		this.setChildrens();
	}
	
	private void setChildrens() {
		
		List<PropertyModel> childrens = new ArrayList<PropertyModel>();
		PropertyModel universal = new PropertyModel();
		universal.setType(TypesAttributesEnum.TEXT);
		universal.setName("UniversalAchieveCondition");
		universal.setChecked(false);

		StringBuilder expression = new StringBuilder();
		expression.append("$Iterated Var$->forAll($Iterated Var$ | $condition$)");
		universal.setPlaceholder(expression.toString());
		
		PropertyModel nonUniversal = new PropertyModel();
		nonUniversal.setType(TypesAttributesEnum.EXPRESSION);
		nonUniversal.setName("AchieveCondition");
		nonUniversal.setPlaceholder("Condition");
		nonUniversal.setChecked(true);
		
		
		childrens.add(universal);
		childrens.add(nonUniversal);
		
		this.setValue(universal.getName());
		super.setChildrens(childrens);
	}
}
