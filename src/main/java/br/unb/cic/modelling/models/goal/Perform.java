package br.unb.cic.modelling.models.goal;

import br.unb.cic.modelling.enums.TypesAttributesEnum;
import br.unb.cic.modelling.models.PropertyModel;

public class Perform extends PropertyModel {
	
	public Perform() {
		this.setType(TypesAttributesEnum.BOOLEAN);
		this.setName("Perform");
		this.setValue("true");
		this.setChecked(true);
		this.setHide(true);
	}

}
