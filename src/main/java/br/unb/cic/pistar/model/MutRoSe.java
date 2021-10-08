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

	public String getModelFile() {
		return modelFile;
	}

	public void setModelFile(String modelFile) {
		this.modelFile = modelFile;
	}

	public String getHddlFile() {
		return hddlFile;
	}

	public void setHddlFile(String hddlFile) {
		this.hddlFile = hddlFile;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public String getWorldFile() {
		return worldFile;
	}

	public void setWorldFile(String worldFile) {
		this.worldFile = worldFile;
	}

}
