package br.com.stefanini.ferias.models;

import java.time.LocalDate;

public class Periodo {
	private LocalDate inicio;
	private LocalDate fim;

	public LocalDate getInicio() {
		return inicio;
	}

	public void setInicio(LocalDate inicio) {
		this.inicio = inicio;
	}

	public LocalDate getFim() {
		return fim;
	}

	public void setFim(LocalDate fim) {
		this.fim = fim;
	}
}
