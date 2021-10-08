package br.unb.cic.goda.model;

import br.unb.cic.goda.utils.GodaUtils;

public class Model {
	private Object content;
	
	public Model() {}

	public Model(Object content) {
		this.content = content;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public String getContentJson() {
		Object contentModel = this.content;
		if (contentModel instanceof String) {
			return ((String) contentModel);
		} else {
			return GodaUtils.objectToString(contentModel);
		}

	}

}
