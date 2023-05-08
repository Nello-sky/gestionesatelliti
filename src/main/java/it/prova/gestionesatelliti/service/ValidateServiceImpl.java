package it.prova.gestionesatelliti.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;

@Service
public class ValidateServiceImpl implements ValidateService {

	@Override
	public Boolean validateInsertEdit(Satellite satellite, BindingResult result, RedirectAttributes redirectAttrs) {
		if (satellite.getDataLancio() != null) {

			// blocco stabile 1 - data di lancio dopo oggi
			if (satellite.getDataLancio().isAfter(LocalDate.now())) {

				if (satellite.getDataRientro() != null) {
					if (satellite.getDataRientro().isBefore(satellite.getDataLancio())) {
						result.rejectValue("dataRientro", "satellite.dataRientro.rientroAfter", "void message!");
						if (satellite.getStato() != null) {
							result.rejectValue("stato", "satellite.stato.null", "void message!");
						}
						return true;
					} else {
						if (satellite.getStato() != null) {
							result.rejectValue("stato", "satellite.stato.null", "void message!");
							return true;
						}
					}
				} else {
					if (satellite.getStato() != null) {
						result.rejectValue("stato", "satellite.stato.null", "void message!");
						return true;
					}
				}
				// end blocco 1 - inizio blocco 2 - data lancio prima di oggi
			} else {
				if (satellite.getDataRientro() == null) {
					if (satellite.getStato() == null) {
						result.rejectValue("stato", "satellite.stato.notNull", "void message!"); // forse non puo essere
																									// disabilitato

						return true;
					}
				} else { // dataRientro non null
					if (satellite.getDataRientro().isBefore(LocalDate.now())) {
						if (satellite.getDataRientro().isBefore(satellite.getDataLancio())) {
							result.rejectValue("dataRientro", "satellite.dataRientro.rientroAfter", "void message!");
							if (!(satellite.getStato() == StatoSatellite.DISATTIVATO)) {
								result.rejectValue("stato", "satellite.stato.notDisabilitato", "void message!");
							}
							return true;
						} else {
							if (!(satellite.getStato() == StatoSatellite.DISATTIVATO)) {
								result.rejectValue("stato", "satellite.stato.notDisabilitato", "void message!");
								return true;
							}
						}
					} else {
						if (satellite.getDataRientro().isBefore(satellite.getDataLancio())) { // scontata non si
																								// verifica mai.
							result.rejectValue("dataRientro", "satellite.dataRientro.rientroAfter", "void message!");
							if (satellite.getStato() != null) {
								result.rejectValue("stato", "satellite.stato.notNull", "void message!");
							}
							return true;
						} else {
							if (satellite.getStato() == null) {
								result.rejectValue("stato", "satellite.stato.notNull", "void message!");
								return true;
							}
						}
					}
				}
			} // end blocco 2

		} else { // blocco last stabile se dataLancioNull
			if (satellite.getDataRientro() != null) {
				result.rejectValue("dataRientro", "satellite.dataRientro.s", "void message!");
				result.rejectValue("dataLancio", "satellite.dataLancio.s", "void message!");
				if (satellite.getStato() != null) {
					result.rejectValue("stato", "satellite.stato.null", "void message!");
				}
				return true;
			}
			if (satellite.getStato() != null) {
				result.rejectValue("stato", "satellite.stato.null", "void message!");
				return true;
			}
		}
		return false;
	}

	@Override
	public Boolean validateDelete(Satellite satelliteUploadDelete, RedirectAttributes redirectAttrs) {
		
		if(satelliteUploadDelete.getDataLancio() == null) {
			redirectAttrs.addFlashAttribute("errorMessage",
					"Impossibile eliminare il satellite, deve avere una data di lancio");
			return true;
		} else {
			if (satelliteUploadDelete.getDataLancio().isBefore(LocalDate.now()) && satelliteUploadDelete.getStato() != StatoSatellite.DISATTIVATO) {
				redirectAttrs.addFlashAttribute("errorMessage",
						"Impossibile eliminare il satellite, deve essere disattivato");
				return true;
			}
			if (satelliteUploadDelete.getDataLancio().isAfter(LocalDate.now())) {
				redirectAttrs.addFlashAttribute("errorMessage",
						"Impossibile eliminare il satellite, deve essere ancora lanciato");
				return true;
			}
		}
		return false;
	}

}
