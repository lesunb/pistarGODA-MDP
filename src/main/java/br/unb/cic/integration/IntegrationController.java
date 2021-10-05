package br.unb.cic.integration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.unb.cic.goda.model.ModelTypeEnum;
import br.unb.cic.modelling.models.PropertyModel;
import br.unb.cic.pistar.model.MutRoSe;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "*")
@Api(value = "API REST GODA")
public class IntegrationController {
	@Autowired
	private IntegrationService service;

	@ApiOperation(value = "Gera o modelo Prism MDP.")
	@RequestMapping(value = "/prism/MDP", method = RequestMethod.POST)
	public void prismMDP(@RequestParam(value = "content") String content) {
		this.service.executePrism(content, ModelTypeEnum.MDP.getTipo(), "src/main/webapp/prism.zip");
	}

	@ApiOperation(value = "Gera o modelo Prism DTMC.")
	@RequestMapping(value = "/prism/DTMC", method = RequestMethod.POST)
	public void prismDTMC(@RequestParam(value = "content") String content) {
		this.service.executePrism(content, ModelTypeEnum.DTMC.getTipo(), "src/main/webapp/prism.zip");
	}

	@ApiOperation(value = "Gera o modelo Param DTMC.")
	@RequestMapping(value = "/param/DTMC", method = RequestMethod.POST)
	public void paramDTMC(@RequestParam(value = "content") String content) {
		this.service.executeParam(content, ModelTypeEnum.PARAM.getTipo(), true, "src/main/webapp/param.zip");
	}

	@ApiOperation(value = "Gera o modelo param MDP.")
	@RequestMapping(value = "/param/MDP", method = RequestMethod.POST)
	public void paramMDP(@RequestParam(value = "content") String content) {
		this.service.executeParam(content, ModelTypeEnum.PARAM.getTipo(), true, "src/main/webapp/param.zip");
	}

	@ApiOperation(value = "Gera o modelo epmc DTMC.")
	@RequestMapping(value = "/epmc/DTMC", method = RequestMethod.POST)
	public void epmcDTMC(@RequestParam(value = "content") String content) {
		this.service.executeParam(content, ModelTypeEnum.EPMC.getTipo(), false, "src/main/webapp/epmc.zip");
	}

	@ApiOperation(value = "Gera o modelo epmc MDP.")
	@RequestMapping(value = "/epmc/MDP", method = RequestMethod.POST)
	public void epmcMDP(@RequestParam(value = "content") String content) {
		this.service.executeParam(content, ModelTypeEnum.EPMC.getTipo(), false, "src/main/webapp/epmc.zip");
	}

	@ApiOperation(value = "Recupera as propriedades de uma tarefa ou objetivo.")
	@RequestMapping(value = "/getProperties", method = RequestMethod.GET)
	public List<PropertyModel> getProperties(@RequestParam(value = "attribute") String attribute) {
		return this.service.getProperties(attribute);
	}

	@ApiOperation(value = "Executa a decomposição realizada pelo MutRoSe.")
	@RequestMapping(value = "/load/multrose", method = RequestMethod.POST)
	public String loadMultRoSe(MutRoSe content) {
		return this.service.generateBinMultRoSe(content);
	}

	@ApiOperation(value = "Recupera os logs do terminal para o PiStar.")
	@RequestMapping(value = "/load/terminal", method = RequestMethod.GET)
	public List<String> loadTerminal() {
		return this.service.loadTerminal();
	}

}