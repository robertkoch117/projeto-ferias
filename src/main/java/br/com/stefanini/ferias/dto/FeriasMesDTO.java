package br.com.stefanini.ferias.dto;

import java.io.Serializable;
import java.util.Map;

public class FeriasMesDTO implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3579474448663923626L;
	
	private String mesAno;
	
    private Map<String, Integer> profissionais;
    
    private int totalDias;

    public String getMesAno() {
        return mesAno;
    }

    public void setMesAno(String mesAno) {
        this.mesAno = mesAno;
    }

    public Map<String, Integer> getProfissionais() {
        return profissionais;
    }

    public void setProfissionais(Map<String, Integer> profissionais) {
        this.profissionais = profissionais;
    }

	public int getTotalDias() {
		return totalDias;
	}

	public void setTotalDias(int totalDias) {
		this.totalDias = totalDias;
	}
}