package br.unb.cic.modelling.models.goal;

import br.unb.cic.modelling.enums.TypesAttributesEnum;
import br.unb.cic.modelling.models.PropertyModel;

//QueriedProperty - Esse atributo surge quando temos uma Goal do tipo
//Query. Ele só tem um formato, que consiste em um select statement da
//linguagem OCL cuja sintaxe é:
//[Queried_Var]->select([Query_Var]:[Query_Var_type] | [Condition])

//Nesse caso temos:
//● Query_Var: O nome de uma variável. Por padrão temos a variável
//“world_db”, que é uma variável especial que não precisa ser
//declarada. De resto, temos variáveis monitoradas aqui ou até
//atributos das mesmas na forma [VAR].[ATTR]).
//● Queried_Var: O nome de uma variável. Ela não precisa ser declarada
//em nenhum outro local.
//● Queried_Var_type: O tipo da variável [Queried_Var]. É obrigatório
//declarar o tipo nesse caso
//● Condition: Uma condição, que é colocada em uma caixa de texto
//Quaisquer verificações serão feitas pela ferramenta de decomposição.
//Devemos ter os 4 atributos a serem declarados no caso de ter uma
//QueriedProperty que serão chamados: “QueryVar”, “QueriedVar”,
//“QueriedVarType” e “QueriedCondition”. Talvez o QueriedVarType possa ser
//um sub-atributo do atributo “QueriedVar”, o que é possível desde que no
//JSON tenhamos algo no formato [VAR] : [TYPE]
public class Query extends PropertyModel {
	
	public Query() {
		this.setName("Query");
		this.setType(TypesAttributesEnum.RADIO_BUTTON);
		
		PropertyModel query = new QueriedProperty();
		this.getChildrens().add(query);

		this.setValue(query.getName());
	}
}