package br.unb.cic.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
//import static io.restassured.module.mockmvc.RestAssuredMockMvc.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.unb.cic.goda.model.Model;
import br.unb.cic.goda.model.ModelTypeEnum;
import br.unb.cic.goda.utils.GodaUtils;
import br.unb.cic.modelling.Properties;
import br.unb.cic.modelling.enums.AttributesEnum;

@WebAppConfiguration
@SuppressWarnings("unused")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Application.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MutRoSeIntegrationTest {

	private MockMvc mockMvc;

	@Autowired
	private IntegrationService service;

	@Autowired
	private IntegrationController controller;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	private String getContent(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get("src/main/resources/testFiles/MutRoSe/" + path)));
	}

	@Test
	public void Teste13() throws Exception {
		try {
			mockMvc.perform(get("/load/terminal")).andDo(print()).andDo(print()).andExpect(status().isOk()).andReturn();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

	}

	@Test
	public void Teste12() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(get("/load/properties?attribute={attribute}", AttributesEnum.TASK.getAttr())).andDo(print())
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String result = mvcResult.getResponse().getContentAsString();
		String properties = GodaUtils.objectToString(Properties.getTasksProperties());

		Assert.assertEquals(result, properties);

	}

	@Test
	public void Teste11() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(get("/load/properties?attribute={attribute}", AttributesEnum.GOAL.getAttr())).andDo(print())
				.andExpect(status().isOk()).andReturn();

		String result = mvcResult.getResponse().getContentAsString();
		String properties = GodaUtils.objectToString(Properties.getGoalsProperties());

		Assert.assertEquals(result, properties);

	}

	@Test
	public void Teste01() throws Exception {
		Model content = new Model(getContent("BSN.txt"));
		
		try {
			String result = service.executePrism(content, ModelTypeEnum.MDP.getTipo(), "src/main/webapp/prism.zip");
			assertNotNull(result);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void Teste02() throws Exception {
	Model content = new Model(getContent("Test2.txt"));
		
		try {
			String result = service.executePrism(content, ModelTypeEnum.MDP.getTipo(), "src/main/webapp/prism.zip");
			assertNotNull(result);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void Teste03() throws Exception {
		Model content = new Model(getContent("Test3.txt"));
		
		try {
			String result = service.executePrism(content, ModelTypeEnum.MDP.getTipo(), "src/main/webapp/prism.zip");
			assertNotNull(result);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void Teste04() throws Exception {
		Model content = new Model(getContent("Test4.txt"));
		
		try {
			String result = service.executePrism(content, ModelTypeEnum.MDP.getTipo(), "src/main/webapp/prism.zip");
			assertNotNull(result);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void Teste05() throws Exception {
		Model content = new Model(getContent("Test5.txt"));
		
		try {
			String result = service.executePrism(content, ModelTypeEnum.MDP.getTipo(), "src/main/webapp/prism.zip");
			assertNotNull(result);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}


	@Test
	public void Teste06() throws Exception {
		Model content = new Model(getContent("Test6.txt"));
		try {
			RuntimeException exception = assertThrows(RuntimeException.class,
					() -> service.executePrism(content, ModelTypeEnum.MDP.getTipo(), "src/main/webapp/prism.zip"));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void Teste07() throws Exception {
		Model content = new Model(getContent("Test7.txt"));
		
		try {
			String result = service.executePrism(content, ModelTypeEnum.MDP.getTipo(), "src/main/webapp/prism.zip");
			assertNotNull(result);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	
	@Test
	public void Teste08() throws Exception {
		Model content = new Model(getContent("Test8.txt"));
		try {
			RuntimeException exception = assertThrows(RuntimeException.class,
					() -> service.executePrism(content, ModelTypeEnum.MDP.getTipo(), "src/main/webapp/prism.zip"));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void Teste09() throws Exception {
		Model content = new Model(getContent("Test9.txt"));
		
		try {
			RuntimeException exception = assertThrows(RuntimeException.class,
					() -> service.executePrism(content, ModelTypeEnum.MDP.getTipo(), "src/main/webapp/prism.zip"));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void Teste10() throws Exception {
		Model content = new Model(getContent("Test10.txt"));
		
		try {
			RuntimeException exception = assertThrows(RuntimeException.class,
					() -> service.executePrism(content, ModelTypeEnum.MDP.getTipo(), "src/main/webapp/prism.zip"));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}


	@Test
	public void executeMutRoSeTest() throws Exception {
		String model = getContent("model.txt");
		String configJson = getContent("configFile.json");
		String configHddl = getContent("configHddl.hddl");
		String world = getContent("worldKnowledge.xml");

		MutRoSe content = new MutRoSe(model, configHddl, configJson, world);
		try {
			String result = service.generateBinMultRoSe(content);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void executeMutRoSeTest0() throws Exception {
		String model = getContent("model.txt");
		String configJson = getContent("configFile.json");
		String configHddl = getContent("configHddl.hddl");
		String world = getContent("worldKnowledge.xml");

		MutRoSe content = new MutRoSe(model, configHddl, configJson, world);
		try {
			String result = service.generateBinMultRoSe(content);
			assertNotNull(result);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void executeMutRoSeTest1() throws Exception {
		String model = getContent("model.txt");
		String configJson = getContent("configFile.json");
		String configHddl = getContent("configHddl.hddl");
		String world = getContent("worldKnowledge.xml");

		MutRoSe content = new MutRoSe(model, configHddl, configJson, world);
		try {
			String result = service.generateBinMultRoSe(content);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void executeMutRoSeTest2() throws Exception {
		String model = getContent("model.txt");
		String configHddl = getContent("configHddl.hddl");
		String world = getContent("worldKnowledge.xml");

		MutRoSe content = new MutRoSe(model, configHddl, null, world);
		RuntimeException exception = assertThrows(RuntimeException.class, () -> service.generateBinMultRoSe(content));
	}
//
	@Test
	public void executeMutRoSeTest3() throws Exception {
		String model = getContent("model.txt");
		String configJson = getContent("configFile.json");
		String configHddl = getContent("configHddl.hddl");

		MutRoSe content = new MutRoSe(model, configHddl, configJson, null);
		RuntimeException exception = assertThrows(RuntimeException.class, () -> service.generateBinMultRoSe(content));

	}

	@Test
	public void executeMutRoSeTest4() throws Exception {
		String model = getContent("model.txt");
		String configJson = getContent("configFile.json");
		String world = getContent("worldKnowledge.xml");

		MutRoSe content = new MutRoSe(model, null, configJson, world);
		RuntimeException exception = assertThrows(RuntimeException.class, () -> service.generateBinMultRoSe(content));

	}

	@Test
	public void executeMutRoSeTest5() throws Exception {
		String model = getContent("model.txt");
		String configJson = getContent("configFileError.json");
		String world = getContent("worldKnowledge.xml");
		String configHddl = getContent("configHddl.hddl");

		MutRoSe content = new MutRoSe(model, configHddl, configJson, world);
		RuntimeException exception = assertThrows(RuntimeException.class, () -> service.generateBinMultRoSe(content));

	}
}
