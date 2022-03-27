package br.unb.cic.modelling.models.task;

import br.unb.cic.modelling.enums.TypesAttributesEnum;
import br.unb.cic.modelling.models.PropertyModel;

public class Parameters extends PropertyModel{

	public Parameters() {
		this.setName("Parameters");
		this.setType(TypesAttributesEnum.TEXT);
	}
}
