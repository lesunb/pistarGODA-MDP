package br.unb.cic.pistar.model;

public class MutRoSe {
	private String model;
	private String hddl;
	private String config;
	private String world;

	public MutRoSe() {

	}

	public MutRoSe(String model, String hddl, String config, String world) {
		this.model = model;
		this.config = config;
		this.hddl = hddl;
		this.world = world;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getHddl() {
		return hddl;
	}

	public void setHddl(String hddl) {
		this.hddl = hddl;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}

}
