package br.unb.cic.modelling.models.goal;

import br.unb.cic.modelling.enums.TypesAttributesEnum;
import br.unb.cic.modelling.models.PropertyModel;

//- Temos dois atributos relacionados à variáveis: Monitors
//e Controls. No Controls temos a declaração de um variável da seguinte
//forma:		[Nome] : [Tipo]

public class Controls extends PropertyModel {
	
	public Controls() {
		this.setName("Controls");
		this.setType(TypesAttributesEnum.TEXT);
		
	}
}