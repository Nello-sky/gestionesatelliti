package it.prova.gestionesatelliti.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

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
	
	// modifico singolo campo..ci sono metodi piu sensati per non scriverne mille di questi
	@Modifying
	@Query("update Satellite s set s.dataLancio = :dataLancioNow, s.stato = :statoMove where s.id = :id") //setting diretto?
	void updateDataLancio(@Param(value = "id") long id, @Param(value = "dataLancioNow") LocalDate dataLancioInput, @Param(value = "statoMove") StatoSatellite statoMove);
	
	@Modifying
	@Query("update Satellite s set s.dataRientro = :dataRientroNow, s.stato = :statoDis where s.id = :id")
	void updateDataRientro(@Param(value = "id") long id, @Param(value = "dataRientroNow") LocalDate dataRientroInput,@Param(value = "statoDis") StatoSatellite statoDis);
	
	//disattivaTutti
	List<Satellite> findAllByDataLancioNotNullAndStatoNotNullAndStatoNotLike(StatoSatellite statoInput);
	
	@Modifying
	@Query("update Satellite s set s.dataRientro = :dataRientroNow, s.stato = :statoDis where (s.dataLancio is not null and s.stato not like 'FISSO') or (s.dataLancio is not null and s.stato not like 'IN_MOVIMENTO')")
	void updateEmergenze(@Param(value = "dataRientroNow") LocalDate dataRientroInput,@Param(value = "statoDis") StatoSatellite statoDis);
}
