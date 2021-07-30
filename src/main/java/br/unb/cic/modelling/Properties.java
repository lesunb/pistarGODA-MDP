package br.unb.cic.modelling;

import java.util.ArrayList;
import java.util.List;

import br.unb.cic.modelling.models.PropertyModel;
import br.unb.cic.modelling.models.goal.*;
import br.unb.cic.modelling.models.task.*;

public  class Properties {
	public  static List<PropertyModel> getGoalsProperties(){
		 List<PropertyModel> properties = new ArrayList<PropertyModel>();

		// properties.add(new Divisible());
		 properties.add(new GoalType());
		 properties.add(new Context());
		 properties.add(new Group());
		 properties.add(new Monitors());
		 properties.add(new Controls());
		 
		 return properties;
	}

	public static List<PropertyModel> getTasksProperties(){
		 List<PropertyModel> properties = new ArrayList<PropertyModel>();

		 properties.add(new Location());
		 properties.add(new Parameters());
		 properties.add(new RobotNumber());
		 
		 return properties;
	}
}
