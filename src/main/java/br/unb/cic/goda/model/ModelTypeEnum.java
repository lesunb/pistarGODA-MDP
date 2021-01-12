package br.unb.cic.goda.model;

public enum ModelTypeEnum {
	DTMC(1, "DTMC"), 
	MDP(2, "MDP"),
	PARAM(3, "PARAM"),
	EPMC(4, "EPMC");

    private final Integer codigo;
    private final String tipo;
    
    ModelTypeEnum(Integer codigo, String tipo){
        this.codigo = codigo;
        this.tipo = tipo;
    }
    
    public Integer getCodigo(){
        return this.codigo;
    }
    
	public String getTipo() {
		return tipo;
	}
    
}	
