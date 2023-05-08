package it.prova.gestionesatelliti.service;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.prova.gestionesatelliti.model.Satellite;

public interface ValidateService {

	Boolean validateInsertEdit(Satellite satellite, BindingResult result, RedirectAttributes redirectAttrs);
	
	Boolean validateDelete(Satellite satellite, RedirectAttributes redirectAttrs);

}
