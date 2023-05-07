package it.prova.gestionesatelliti.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "satellite")
public class Satellite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank(message="{satellite.denominazione.notblank}")
	@Column(name = "denominazione")
	private String denominazione;

	@NotBlank(message="{satellite.codice.notblank}")
	@Column(name = "codice")
	private String codice;

	@Column(name = "datalancio")
	private LocalDate dataLancio;

	@Column(name = "datarientro")
	private LocalDate dataRientro;

	@Column(name = "stato")
	@Enumerated(EnumType.STRING)
	private StatoSatellite stato;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public LocalDate getDataLancio() {
		return dataLancio;
	}

	public void setDataLancio(LocalDate dataLancio) {
		this.dataLancio = dataLancio;
	}

	public LocalDate getDataRientro() {
		return dataRientro;
	}

	public void setDataRientro(LocalDate dataRientro) {
		this.dataRientro = dataRientro;
	}

	public StatoSatellite getStato() {
		return stato;
	}

	public void setStato(StatoSatellite stato) {
		this.stato = stato;
	}

}