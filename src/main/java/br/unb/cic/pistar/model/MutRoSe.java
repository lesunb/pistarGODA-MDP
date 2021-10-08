package br.unb.cic.pistar.model;

public class MutRoSe {
	private String modelFile;
	private String hddlFile;
	private String configFile;
	private String worldFile;

	public MutRoSe() {

	}

	public MutRoSe(String model, String hddl, String config, String world) {
		this.modelFile = model;
		this.configFile = config;
		this.hddlFile = hddl;
		this.worldFile = world;
	}

	public String getModel() {
		return modelFile;
	}

	public void setModel(String model) {
		this.modelFile = model;
	}

	public String getHddl() {
		return hddlFile;
	}

	public void setHddl(String hddl) {
		this.hddlFile = hddl;
	}

	public String getConfig() {
		return configFile;
	}

	public void setConfig(String config) {
		this.configFile = config;
	}

	public String getWorld() {
		return worldFile;
	}

	public void setWorld(String world) {
		this.worldFile = world;
	}

}
