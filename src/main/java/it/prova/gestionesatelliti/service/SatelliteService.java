package it.prova.gestionesatelliti.service;

import java.time.LocalDate;
import java.util.List;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;

public interface SatelliteService {

	List<Satellite> listAllElements();
	
	Satellite caricaSingoloElemento(Long id);
	
	void aggiorna(Satellite satelliteInstance);
	
	void inserisciNuovo(Satellite satelliteInstance);
	
	void rimuovi(Long idSatellite);
	
	List<Satellite> findByExample(Satellite example);
	
	//esercizi
	
	List<Satellite> findByDenominazione(String denominazioneTerm);
	
	List<Satellite> findBySenzaDataRientroEDisattivati(StatoSatellite statoInput);
	
	List<Satellite> findByDataRientroMinoreDiEStatoNotDisattivato(LocalDate dataInput, StatoSatellite statoInput);
	
	List<Satellite> cercaByDataLancioLessThanEqualAndStato(LocalDate dataInput, StatoSatellite statoInput);
	
	// extra buttons
	
	void aggiornaDataLancio(Long id, LocalDate dataLancioInput, StatoSatellite statoInput);
	
	void aggiornaDataRientro(Long id, LocalDate dataRientroInput, StatoSatellite statoInput);
	
	List<Satellite> listaEmergenze();
	
	void aggiornaEmergenze();
}
