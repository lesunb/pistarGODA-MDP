package br.unb.cic.integration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.unb.cic.goda.model.Model;
import br.unb.cic.goda.model.ModelTypeEnum;
import br.unb.cic.modelling.models.PropertyModel;
import br.unb.cic.pistar.model.MutRoSe;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "API REST GODA")
public class IntegrationController {
	@Autowired
	private IntegrationService service;

	@ApiOperation(value = "Gera o modelo Prism MDP.")
	@RequestMapping(value = "/prism/MDP", method = RequestMethod.POST)
	public String prismMDP(@RequestBody Model content) {
		return this.service.executePrism(content, ModelTypeEnum.MDP.getTipo(), "src/main/webapp/prism.zip");
	}

	@ApiOperation(value = "Gera o modelo Prism DTMC.")
	@RequestMapping(value = "/prism/DTMC", method = RequestMethod.POST)
	public String prismDTMC(@RequestBody Model content) {
		return this.service.executePrism(content, ModelTypeEnum.DTMC.getTipo(), "src/main/webapp/prism.zip");
	}

	@ApiOperation(value = "Gera o modelo Param.")
	@RequestMapping(value = "/param", method = RequestMethod.POST)
	public String paramDTMC(@RequestBody Model content) {
		return this.service.executeParam(content, ModelTypeEnum.PARAM.getTipo(), true, "src/main/webapp/param.zip");
	}
	
	@ApiOperation(value = "Gera o modelo epmc.")
	@RequestMapping(value = "/epmc", method = RequestMethod.POST)
	public String epmcDTMC(@RequestBody Model content) {
		return this.service.executeParam(content, ModelTypeEnum.EPMC.getTipo(), false, "src/main/webapp/epmc.zip");
	}

//	@ApiOperation(value = "Gera o modelo param MDP.")
//	@RequestMapping(value = "/param/MDP", method = RequestMethod.POST)
//	public String paramMDP(@RequestBody Model content) {
//		return this.service.executeParam(content, ModelTypeEnum.PARAM.getTipo(), true, "src/main/webapp/param.zip");
//	}

//	@ApiOperation(value = "Gera o modelo epmc MDP.")
//	@RequestMapping(value = "/epmc/MDP", method = RequestMethod.POST)
//	public String epmcMDP(@RequestBody Model content) {
//		return this.service.executeParam(content, ModelTypeEnum.EPMC.getTipo(), false, "src/main/webapp/epmc.zip");
//	}

	@ApiOperation(value = "Recupera as propriedades de uma tarefa ou objetivo.")
	@RequestMapping(value = "/load/properties", method = RequestMethod.GET)
	public List<PropertyModel> getProperties(@RequestParam(value = "attribute") String attribute) {
		return this.service.getProperties(attribute);
	}

	@ApiOperation(value = "Executa a decomposição realizada pelo MutRoSe.")
	@RequestMapping(value = "/load/mutrose", method = RequestMethod.POST)
	public String loadMultRoSe(MutRoSe content) {
		return this.service.generateBinMultRoSe(content);
	}

	@ApiOperation(value = "Recupera os logs do terminal para o PiStar.")
	@RequestMapping(value = "/load/terminal", method = RequestMethod.GET)
	public List<String> loadTerminal() {
		return this.service.loadTerminal();
	}

}