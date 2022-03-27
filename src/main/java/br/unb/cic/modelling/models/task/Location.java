package br.unb.cic.modelling.models.task;

import br.unb.cic.modelling.enums.TypesAttributesEnum;
import br.unb.cic.modelling.models.PropertyModel;

public class Location extends PropertyModel{

	public Location() {
		this.setName("Location");
		this.setType(TypesAttributesEnum.TEXT);
	}
}
