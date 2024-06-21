package br.unb.cic.integration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.unb.cic.goda.model.ModelTypeEnum;
import br.unb.cic.modelling.models.PropertyModel;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class IntegrationController {
	@Autowired
	private IntegrationService service;

    @RequestMapping(value = "/")
    public ModelAndView  index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index.html");
        return modelAndView;
    }
	@RequestMapping(value = "/prism/MDP", method = RequestMethod.POST)
    public void prismMDP( @RequestParam(value = "content") String content) {
		this.service.executePrism(content, ModelTypeEnum.MDP.getTipo(), "src/main/resources/static/prism.zip");
    }
	
	@RequestMapping(value = "/prism/DTMC", method = RequestMethod.POST)
    public void prismDTMC( @RequestParam(value = "content") String content) {
		this.service.executePrism(content, ModelTypeEnum.DTMC.getTipo(), "src/main/resources/static/prism.zip");
    }
	
	@RequestMapping(value = "/param/DTMC", method = RequestMethod.POST)
    public void paramDTMC( @RequestParam(value = "content") String content) {
		this.service.executeParam(content, ModelTypeEnum.PARAM.getTipo(), true, "src/main/resources/static/param.zip");
    }

	@RequestMapping(value = "/param/MDP", method = RequestMethod.POST)
    public void paramMDP( @RequestParam(value = "content") String content) {
		this.service.executeParam(content, ModelTypeEnum.PARAM.getTipo(), true, "src/main/resources/static/param.zip");
    }
	
    @RequestMapping(value = "/epmc/DTMC", method = RequestMethod.POST)
    public void epmcDTMC(@RequestParam(value = "content") String content) {
    	this.service.executeParam(content, ModelTypeEnum.EPMC.getTipo(), false, "src/main/resources/static/epmc.zip");
    }
	
    @RequestMapping(value = "/epmc/MDP", method = RequestMethod.POST)
    public void epmcMDP(@RequestParam(value = "content") String content) {
    	this.service.executeParam(content, ModelTypeEnum.EPMC.getTipo(), false, "src/main/resources/static/epmc.zip");
    }

    @RequestMapping(value = "/getProperties", method = RequestMethod.GET)
    public  List<PropertyModel> getProperties(@RequestParam(value = "attribute") String attribute) {
    	return this.service.getProperties(attribute);
    }
    
}