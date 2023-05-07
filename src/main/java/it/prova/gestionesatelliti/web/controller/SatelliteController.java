package it.prova.gestionesatelliti.web.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;
import it.prova.gestionesatelliti.service.SatelliteService;

@Controller
@RequestMapping(value = "/satellite")
public class SatelliteController {

	@Autowired
	private SatelliteService satelliteService;

	@GetMapping
	public ModelAndView listAll() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.listAllElements();
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	@GetMapping("/findByDenominazione")   // un primo modo ri richiamare 1
	public ModelAndView findByDenominazione() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.findByDenominazione("a");
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}
	
	@GetMapping("/disattivatiNonRientrati")
	public String listByNoDataRientroAndStato( ModelMap model) {         
		List<Satellite> results = satelliteService.findBySenzaDataRientroEDisattivati(StatoSatellite.DISATTIVATO);
		model.addAttribute("satellite_list_attribute", results);
		return "satellite/list";
	}
	
	@GetMapping("/nonDisattivatiNonRientratiDa")
	public String listByLanciatiDaNonDisattivati( ModelMap model) {         
		LocalDate dateBound = LocalDate.now().minusYears(2L);
		List<Satellite> results = satelliteService.findByDataRientroMinoreDiEStatoNotDisattivato(dateBound, StatoSatellite.DISATTIVATO);
		model.addAttribute("satellite_list_attribute", results);
		return "satellite/list";
	}
	
	@GetMapping("/fissiNonRientratiDa")
	public String listByLanciatiDaEFissi( ModelMap model) {         
		LocalDate dateBound = LocalDate.now().minusYears(10L);
		List<Satellite> results = satelliteService.findByDataRientroMinoreDiEStatoNotDisattivato(dateBound, StatoSatellite.DISATTIVATO);
		model.addAttribute("satellite_list_attribute", results);
		return "satellite/list";
	}
	

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@GetMapping("/search")
	public String search() {
		return "satellite/search";
	}

	@PostMapping("/list")
	public String listByExample(Satellite example, ModelMap model) {         //posso usare questo senza example per liste 2
		List<Satellite> results = satelliteService.findByExample(example);
		model.addAttribute("satellite_list_attribute", results);
		return "satellite/list";
	}

	@GetMapping("/insert")
	public String create(Model model) {
		model.addAttribute("insert_satellite_attr", new Satellite());
		return "satellite/insert";
	}

	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("insert_satellite_attr") Satellite satellite, BindingResult result,
			RedirectAttributes redirectAttrs) {

		if (result.hasErrors())
			return "satellite/insert";

		if (satellite.getDataLancio() != null) {

			// blocco stabile 1 - data di lancio dopo oggi
			if (satellite.getDataLancio().isAfter(LocalDate.now())) {

				if (satellite.getDataRientro() != null) {
					if (satellite.getDataRientro().isBefore(satellite.getDataLancio())) {
						result.rejectValue("dataRientro", "satellite.dataRientro.rientroAfter", "void message!");
						if (satellite.getStato() != null) {
							result.rejectValue("stato", "satellite.stato.null", "void message!");
						}
						return "satellite/insert";
					} else {
						if (satellite.getStato() != null) {
							result.rejectValue("stato", "satellite.stato.null", "void message!");
							return "satellite/insert";
						}
					}
				} else {
					if (satellite.getStato() != null) {
						result.rejectValue("stato", "satellite.stato.null", "void message!");
						return "satellite/insert";
					}
				}
				// end blocco 1 - inizio blocco 2 - data lancio prima di oggi
			} else {
				if (satellite.getDataRientro() == null) {
					if (satellite.getStato() == null) {
						result.rejectValue("stato", "satellite.stato.notNull", "void message!"); // forse non puo essere
																									// disabilitato

						return "satellite/insert";
					}
				} else { // dataRientro non null 
					if (satellite.getDataRientro().isBefore(LocalDate.now())) {
						if (satellite.getDataRientro().isBefore(satellite.getDataLancio())) {
							result.rejectValue("dataRientro", "satellite.dataRientro.rientroAfter", "void message!");
							if (! (satellite.getStato() == StatoSatellite.DISATTIVATO) ) {
								result.rejectValue("stato", "satellite.stato.notDisabilitato", "void message!");
							}
							return "satellite/insert";
						} else {
							if (! (satellite.getStato() == StatoSatellite.DISATTIVATO)) {
								result.rejectValue("stato", "satellite.stato.notDisabilitato", "void message!");
								return "satellite/insert";
							}
						}
					} else {
						if (satellite.getDataRientro().isBefore(satellite.getDataLancio())) {   //scontata non si verifica mai.
							result.rejectValue("dataRientro", "satellite.dataRientro.rientroAfter", "void message!");
							if (satellite.getStato() != null) {
								result.rejectValue("stato", "satellite.stato.notNull", "void message!");
							}
							return "satellite/insert";
						} else {
							if (satellite.getStato() == null) {
								result.rejectValue("stato", "satellite.stato.notNull", "void message!");
								return "satellite/insert";
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
				return "satellite/insert";
			}
			if (satellite.getStato() != null) {
				result.rejectValue("stato", "satellite.stato.null", "void message!");
				return "satellite/insert";
			}
		}

		satelliteService.inserisciNuovo(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}

	@GetMapping("/show/{idSatellite}")
	public String show(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("show_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/show";
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@GetMapping("/delete/{idSatellite}")
	public String delete(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("delete_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/delete";
	}

	@PostMapping("/elimina")
	public String elimina(@Valid @ModelAttribute("elimina_satellite_id") Satellite satellite, BindingResult result,
			RedirectAttributes redirectAttrs) {

//		if (result.hasErrors())
//			return "impiegato/insert";

		satelliteService.rimuovi(satellite.getId());

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@GetMapping("/edit/{idSatellite}")
	public String edit(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("edit_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/edit";
	}

	@PostMapping("/update")
	public String update(@Valid @ModelAttribute("edit_satellite_attr") Satellite satellite, BindingResult result,
			RedirectAttributes redirectAttrs) {

		if (result.hasErrors())
			return "satellite/edit";

		satelliteService.aggiorna(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}
}
