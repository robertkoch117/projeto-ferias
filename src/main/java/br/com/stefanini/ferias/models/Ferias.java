package br.com.stefanini.ferias.models;

import java.time.LocalDate;

public class Ferias {

	private String empresa;
	private String filial;
	private String colaborador;
	private String situacaoFuncional;
	private LocalDate dataSituacao;
	private String centroCusto;
	private String unidadeAdministrativa;
	private String atividade;
	private String situacaoFerias;
	private LocalDate inicioPeriodoAquisitivo;
	private LocalDate fimPeriodoAquisitivo;
	private LocalDate dataLimiteInicioFerias;
	private LocalDate dataLimiteProgramacaoFerias;
	private double saldo;
	private int faltas;
	private LocalDate saidaParc1;
	private LocalDate retornoParc1;
	private int numDiasParc1;
	private int diasAbonoParc1;
	private String opcao13SalParc1;
	private LocalDate saidaParc2;
	private LocalDate retornoParc2;
	private int numDiasParc2;
	private int diasAbonoParc2;
	private String opcao13SalParc2;
	private LocalDate saidaParc3;
	private LocalDate retornoParc3;
	private int numDiasParc3;
	private int diasAbonoParc3;

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getFilial() {
		return filial;
	}

	public void setFilial(String filial) {
		this.filial = filial;
	}

	public String getColaborador() {
		return colaborador;
	}

	public void setColaborador(String colaborador) {
		this.colaborador = colaborador;
	}

	public String getSituacaoFuncional() {
		return situacaoFuncional;
	}

	public void setSituacaoFuncional(String situacaoFuncional) {
		this.situacaoFuncional = situacaoFuncional;
	}

	public LocalDate getDataSituacao() {
		return dataSituacao;
	}

	public void setDataSituacao(LocalDate dataSituacao) {
		this.dataSituacao = dataSituacao;
	}

	public String getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(String centroCusto) {
		this.centroCusto = centroCusto;
	}

	public String getUnidadeAdministrativa() {
		return unidadeAdministrativa;
	}

	public void setUnidadeAdministrativa(String unidadeAdministrativa) {
		this.unidadeAdministrativa = unidadeAdministrativa;
	}

	public String getAtividade() {
		return atividade;
	}

	public void setAtividade(String atividade) {
		this.atividade = atividade;
	}

	public String getSituacaoFerias() {
		return situacaoFerias;
	}

	public void setSituacaoFerias(String situacaoFerias) {
		this.situacaoFerias = situacaoFerias;
	}

	public LocalDate getInicioPeriodoAquisitivo() {
		return inicioPeriodoAquisitivo;
	}

	public void setInicioPeriodoAquisitivo(LocalDate inicioPeriodoAquisitivo) {
		this.inicioPeriodoAquisitivo = inicioPeriodoAquisitivo;
	}

	public LocalDate getFimPeriodoAquisitivo() {
		return fimPeriodoAquisitivo;
	}

	public void setFimPeriodoAquisitivo(LocalDate fimPeriodoAquisitivo) {
		this.fimPeriodoAquisitivo = fimPeriodoAquisitivo;
	}

	public LocalDate getDataLimiteInicioFerias() {
		return dataLimiteInicioFerias;
	}

	public void setDataLimiteInicioFerias(LocalDate dataLimiteInicioFerias) {
		this.dataLimiteInicioFerias = dataLimiteInicioFerias;
	}

	public LocalDate getDataLimiteProgramacaoFerias() {
		return dataLimiteProgramacaoFerias;
	}

	public void setDataLimiteProgramacaoFerias(LocalDate dataLimiteProgramacaoFerias) {
		this.dataLimiteProgramacaoFerias = dataLimiteProgramacaoFerias;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public int getFaltas() {
		return faltas;
	}

	public void setFaltas(int faltas) {
		this.faltas = faltas;
	}

	public LocalDate getSaidaParc1() {
		return saidaParc1;
	}

	public void setSaidaParc1(LocalDate saidaParc1) {
		this.saidaParc1 = saidaParc1;
	}

	public LocalDate getRetornoParc1() {
		return retornoParc1;
	}

	public void setRetornoParc1(LocalDate retornoParc1) {
		this.retornoParc1 = retornoParc1;
	}

	public int getNumDiasParc1() {
		return numDiasParc1;
	}

	public void setNumDiasParc1(int numDiasParc1) {
		this.numDiasParc1 = numDiasParc1;
	}

	public int getDiasAbonoParc1() {
		return diasAbonoParc1;
	}

	public void setDiasAbonoParc1(int diasAbonoParc1) {
		this.diasAbonoParc1 = diasAbonoParc1;
	}

	public String getOpcao13SalParc1() {
		return opcao13SalParc1;
	}

	public void setOpcao13SalParc1(String opcao13SalParc1) {
		this.opcao13SalParc1 = opcao13SalParc1;
	}

	public LocalDate getSaidaParc2() {
		return saidaParc2;
	}

	public void setSaidaParc2(LocalDate saidaParc2) {
		this.saidaParc2 = saidaParc2;
	}

	public LocalDate getRetornoParc2() {
		return retornoParc2;
	}

	public void setRetornoParc2(LocalDate retornoParc2) {
		this.retornoParc2 = retornoParc2;
	}

	public int getNumDiasParc2() {
		return numDiasParc2;
	}

	public void setNumDiasParc2(int numDiasParc2) {
		this.numDiasParc2 = numDiasParc2;
	}

	public int getDiasAbonoParc2() {
		return diasAbonoParc2;
	}

	public void setDiasAbonoParc2(int diasAbonoParc2) {
		this.diasAbonoParc2 = diasAbonoParc2;
	}

	public String getOpcao13SalParc2() {
		return opcao13SalParc2;
	}

	public void setOpcao13SalParc2(String opcao13SalParc2) {
		this.opcao13SalParc2 = opcao13SalParc2;
	}

	public LocalDate getSaidaParc3() {
		return saidaParc3;
	}

	public void setSaidaParc3(LocalDate saidaParc3) {
		this.saidaParc3 = saidaParc3;
	}

	public LocalDate getRetornoParc3() {
		return retornoParc3;
	}

	public void setRetornoParc3(LocalDate retornoParc3) {
		this.retornoParc3 = retornoParc3;
	}

	public int getNumDiasParc3() {
		return numDiasParc3;
	}

	public void setNumDiasParc3(int numDiasParc3) {
		this.numDiasParc3 = numDiasParc3;
	}

	public int getDiasAbonoParc3() {
		return diasAbonoParc3;
	}

	public void setDiasAbonoParc3(int diasAbonoParc3) {
		this.diasAbonoParc3 = diasAbonoParc3;
	}

	public String getOpcao13SalParc3() {
		return opcao13SalParc3;
	}

	public void setOpcao13SalParc3(String opcao13SalParc3) {
		this.opcao13SalParc3 = opcao13SalParc3;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public String getSindicato() {
		return sindicato;
	}

	public void setSindicato(String sindicato) {
		this.sindicato = sindicato;
	}

	public String getCodigoSindicato() {
		return codigoSindicato;
	}

	public void setCodigoSindicato(String codigoSindicato) {
		this.codigoSindicato = codigoSindicato;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	private String opcao13SalParc3;
	private String observacoes;
	private String sindicato;
	private String codigoSindicato;
	private String usuario;

}
