package br.unb.cic.modelling.models.goal;

import java.util.ArrayList;
import java.util.List;

import br.unb.cic.modelling.enums.TypesAttributesEnum;
import br.unb.cic.modelling.models.PropertyModel;

//Group e Divisible - Por serem dois atributos booleanos, basta ter uma caixa
//para marcar e desmarcar cada um desses dois atributos. O valor padrão dos
//dois é True, ou seja, as caixas já são inicializadas marcadas. Caso Group
//seja False, o atributo Divisible não faz diferença, mas não se faz necessário
//nenhum tratamento especial disso se for muito complexo.
public class Group extends PropertyModel {

	public Group() {
		this.setChecked(true);
		this.setName("Group");
		this.setType(TypesAttributesEnum.OBJECT_NULLABLE);
		this.setValue("true");
		this.setChecked(true);
		this.setChildrens();
		
	}
	
	private void setChildrens() {
		List<PropertyModel> childrens = new ArrayList<PropertyModel>();
		PropertyModel div = new Divisible();
		div.setType(TypesAttributesEnum.CHECKBOX);
		div.setChecked(true);
		div.setValue("true");
		childrens.add(div);
		
		super.setChildrens(childrens);
	}
}
