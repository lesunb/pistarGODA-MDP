package br.unb.cic.modelling.models.goal;

import br.unb.cic.modelling.enums.TypesAttributesEnum;
import br.unb.cic.modelling.models.PropertyModel;

//- Temos dois atributos relacionados à variáveis: Monitors
//e Controls. No Monitors temos a declaração com o tipo sendo opcional, logo
//podemos ter apenas o nome da seguinte maneira: [Nome]
//Atualmente se declara as variáveis separadas por vírgulas, mas pode-se ir
//adicionando uma a uma de maneira mais organizada.

public class Monitors extends PropertyModel {
	
	public Monitors() {
		this.setName("Monitors");
		this.setType(TypesAttributesEnum.TEXT);
		
	}
}