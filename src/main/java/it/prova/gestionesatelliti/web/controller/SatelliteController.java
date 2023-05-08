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
import it.prova.gestionesatelliti.service.ValidateService;

@Controller
@RequestMapping(value = "/satellite")
public class SatelliteController {

	@Autowired
	private SatelliteService satelliteService;
	@Autowired
	private ValidateService validateService;

	@GetMapping
	public ModelAndView listAll() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.listAllElements();
		
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}
	
	@GetMapping("/emergenze")
	public ModelAndView emergenze(RedirectAttributes redirectAttrs) {
		
		ModelAndView mv = new ModelAndView();
		List<Satellite> ListaAll = satelliteService.listAllElements();
		int valoriListAll = ListaAll.size();
		List<Satellite> ListaEmergenze = satelliteService.listaEmergenze();
		int valoriListEmergenze = ListaEmergenze.size();
		
		if (valoriListAll<=0 ) {
			mv.setViewName("redirect:/satellite");
			redirectAttrs.addFlashAttribute("errorMessage", "Nessun elemento nel DB");
			return mv;
		} else {
			if (valoriListEmergenze<=0) {
				mv.setViewName("redirect:/satellite");
				redirectAttrs.addFlashAttribute("errorMessage", "Nessun satellite in orbita");
				return mv;
			}
		}
		
		mv.addObject("all_list_attribute", valoriListAll);
		mv.addObject("emergenze_list_attribute", valoriListAll);
		mv.setViewName("satellite/emergenze");
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

		
		if (validateService.validateInsertEdit(satellite, result, redirectAttrs) == true)
			return "satellite/insert";

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
	public String delete(@PathVariable(required = true) Long idSatellite, Model model,RedirectAttributes redirectAttributes) {
		
		Satellite satelliteUploadDelete = satelliteService.caricaSingoloElemento(idSatellite);
		
		if (validateService.validateDelete(satelliteUploadDelete, redirectAttributes) == true)
			return "redirect:/satellite";
		

		
		model.addAttribute("delete_satellite_attr",satelliteUploadDelete);
		return "satellite/delete";
	}

	@PostMapping("/elimina")
	public String elimina(@Valid @ModelAttribute("elimina_satellite_id") Satellite satellite, BindingResult result,
			RedirectAttributes redirectAttrs) {

//		if(satellite.getDataLancio() != null) {  //lanci futuri sono a null..quindi va semplificato
//			if (! (satellite.getStato() == StatoSatellite.DISATTIVATO) ) {
//				result.rejectValue("stato", "satellite.stato.notRemovable", "void message!");
//				return "satellite/insert";
//			}
//			
//		} else {
//			redirectAttrs.addFlashAttribute("dataLancio", "satellite.dataRientro.notRemovable");
//			return "redirect:/satellite/insert";
//		}
		
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

		if (validateService.validateInsertEdit(satellite, result, redirectAttrs) == true)
			return "satellite/edit";
		
		satelliteService.aggiorna(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}
	
	@PostMapping("/lancia")
	public String lancia(@Valid @ModelAttribute("lancia_satellite_id") Satellite satellite, BindingResult result,
	RedirectAttributes redirectAttrs) {
		satelliteService.aggiornaDataLancio(satellite.getId(), LocalDate.now(), satellite.getStato().IN_MOVIMENTO);
		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}
	
	@PostMapping("/rientra")
	public String rientra(@Valid @ModelAttribute("rientra_satellite_id") Satellite satellite, BindingResult result,
	RedirectAttributes redirectAttrs) {
		satelliteService.aggiornaDataRientro(satellite.getId(), LocalDate.now(), satellite.getStato().FISSO);
		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}
	
	@PostMapping("/editEmergenze")
	public String editEmergenze(ModelMap model, RedirectAttributes redirectAttrs ) {
		satelliteService.aggiornaEmergenze();
		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
		
	}
}
