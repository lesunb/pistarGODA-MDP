package br.unb.cic.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.unb.cic.goda.model.ModelTypeEnum;

@RestController
public class IntegrationController {
	@Autowired
	private IntegrationService service;

	@RequestMapping(value = "/prism/MDP", method = RequestMethod.POST)
    public void prismMDP( @RequestParam(value = "content") String content) {
		this.service.executePrism(content, ModelTypeEnum.MDP.getTipo(), "src/main/webapp/prism.zip");
    }
	
	@RequestMapping(value = "/prism/DTMC", method = RequestMethod.POST)
    public void prismDTMC( @RequestParam(value = "content") String content) {
		this.service.executePrism(content, ModelTypeEnum.DTMC.getTipo(), "src/main/webapp/prism.zip");
    }
	
	@RequestMapping(value = "/param/DTMC", method = RequestMethod.POST)
    public void paramDTMC( @RequestParam(value = "content") String content) {
		this.service.executeParam(content, ModelTypeEnum.PARAM.getTipo(), true, "src/main/webapp/param.zip");
    }
	
    @RequestMapping(value = "/epmc/DTMC", method = RequestMethod.POST)
    public void epmcDTMC(@RequestParam(value = "content") String content) {
    	this.service.executeParam(content, ModelTypeEnum.EPMC.getTipo(), false, "src/main/webapp/epmc.zip");
    }

	@RequestMapping(value = "/param/MDP", method = RequestMethod.POST)
    public void paramMDP( @RequestParam(value = "content") String content) {
		this.service.executeParam(content, ModelTypeEnum.PARAM.getTipo(), true, "src/main/webapp/param.zip");
    }
	
    @RequestMapping(value = "/epmc/MDP", method = RequestMethod.POST)
    public void epmcMDP(@RequestParam(value = "content") String content) {
    	this.service.executeParam(content, ModelTypeEnum.EPMC.getTipo(), false, "src/main/webapp/epmc.zip");
    }

    @RequestMapping(value = "/formula/reliability", method = RequestMethod.GET)
    public @ResponseBody String getReliabilityFormulaTree(@RequestParam(value = "id") String id, @RequestParam(value = "goal") String goal) {
	    return this.service.getReliabilityFormulaTree(id, goal);
    }

    @RequestMapping(value = "/formula/cost", method = RequestMethod.GET)
    public @ResponseBody String getCostFormulaTree(@RequestParam(value = "id") String id, @RequestParam(value = "goal") String goal) {
	    return this.service.getCostFormulaTree(id, goal);
    }
}