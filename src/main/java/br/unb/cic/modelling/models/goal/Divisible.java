package br.unb.cic.modelling.models.goal;

import br.unb.cic.modelling.enums.TypesAttributesEnum;
import br.unb.cic.modelling.models.PropertyModel;

//Group e Divisible - Por serem dois atributos booleanos, basta ter uma caixa
//para marcar e desmarcar cada um desses dois atributos. O valor padrão dos
//dois é True, ou seja, as caixas já são inicializadas marcadas. Caso Group
//seja False, o atributo Divisible não faz diferença, mas não se faz necessário
//nenhum tratamento especial disso se for muito complexo.
public class Divisible extends PropertyModel {

	public Divisible() {
		this.setChecked(true);
		this.setName("Divisible");
		this.setType(TypesAttributesEnum.CHECKBOX);
		this.setValue("true");
		this.setHide(false);
	}
}
