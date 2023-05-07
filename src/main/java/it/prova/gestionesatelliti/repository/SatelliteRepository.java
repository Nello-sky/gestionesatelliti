package it.prova.gestionesatelliti.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;

public interface SatelliteRepository extends CrudRepository<Satellite, Long>,JpaSpecificationExecutor<Satellite> {

	
	List<Satellite> findByDenominazioneIgnoreCaseContaining(String denominazioneTerm);
	
//	 Lanciati da più di due anni (fornisce una lista di satelliti lanciati da più di due anni che non sono disattivati);
	List<Satellite> findByDataLancioLessThanEqualAndStatoNot(LocalDate dataInput, StatoSatellite statoInput);
	
//	 Disattivati ma non rientrati (che sono stati disattivati ma con data rientro ancora a null);   IgnoreCaseContaining
	List<Satellite> findByDataRientroNullAndStato(StatoSatellite statoInput);
	
//	Rimasti in orbita 10 anni ma che ora sono fissi;
	List<Satellite> findByDataLancioAndStato(LocalDate dataInput, StatoSatellite statoInput);
	
	
}
