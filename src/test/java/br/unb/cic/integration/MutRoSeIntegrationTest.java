package br.unb.cic.integration;

import static org.junit.jupiter.api.Assertions.assertThrows;
//import static io.restassured.module.mockmvc.RestAssuredMockMvc.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.unb.cic.goda.model.ModelTypeEnum;
import br.unb.cic.modelling.Properties;
import br.unb.cic.modelling.enums.AttributesEnum;
import br.unb.cic.pistar.model.MutRoSe;

@WebAppConfiguration
@SuppressWarnings("unused")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Application.class })
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

	private String objectToJString(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String result = objectMapper.writeValueAsString(obj);

		return result;
	}

	@Test
	public void getTerminalLogsTest() throws Exception {
		try {
			mockMvc.perform(get("/load/terminal")).andDo(print()).andDo(print()).andExpect(status().isOk()).andReturn();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

	}

	@Test
	public void getTaskPropertiesTest() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(get("/load/properties?attribute={attribute}", AttributesEnum.TASK.getAttr())).andDo(print())
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String result = mvcResult.getResponse().getContentAsString();
		String properties = this.objectToJString(Properties.getTasksProperties());

		Assert.assertEquals(result, properties);

	}

	@Test
	public void getGoalPropertiesTest() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(get("/load/properties?attribute={attribute}", AttributesEnum.GOAL.getAttr())).andDo(print())
				.andExpect(status().isOk()).andReturn();

		String result = mvcResult.getResponse().getContentAsString();
		String properties = this.objectToJString(Properties.getGoalsProperties());

		Assert.assertEquals(result, properties);

	}

	@Test
	public void executePrismMDPTest1() throws Exception {
		String content = getContent("BSN.txt");
		try {
			MvcResult mvcResult = mockMvc.perform(post("/prism/MDP").param("content", content))
					.andExpect(status().isOk()).andDo(print()).andReturn();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void executePrismMDPTest2() throws Exception {
		String content = getContent("Test4.txt");
		try {
			MvcResult mvcResult = mockMvc.perform(post("/prism/MDP").param("content", content))
					.andExpect(status().isOk()).andDo(print()).andReturn();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void executePrismMDPTest3() throws Exception {
		String content = getContent("Test5.txt");
		try {
			MvcResult mvcResult = mockMvc.perform(post("/prism/MDP").param("content", content))
					.andExpect(status().isOk()).andDo(print()).andReturn();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void executePrismMDPTest4() throws Exception {
		String content = getContent("Test6.txt");
		try {
			RuntimeException exception = assertThrows(RuntimeException.class, () -> service.executePrism(content, ModelTypeEnum.MDP.getTipo(), "src/main/webapp/prism.zip"));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void executePrismMDPTest5() throws Exception {
		String content = getContent("Test7.txt");
		try {
			MvcResult mvcResult = mockMvc.perform(post("/prism/MDP").param("content", content))
					.andExpect(status().isOk()).andDo(print()).andReturn();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void executePrismMDPTest6() throws Exception {
		String content = getContent("Test8.txt");
		try {
			RuntimeException exception = assertThrows(RuntimeException.class, () -> service.executePrism(content, ModelTypeEnum.MDP.getTipo(), "src/main/webapp/prism.zip"));
			
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void executePrismMDPTest7() throws Exception {
		String content = getContent("model.txt");
		try {
			MvcResult mvcResult = mockMvc.perform(post("/prism/MDP").param("content", content))
					.andExpect(status().isOk()).andDo(print()).andReturn();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void executeParamMDPTest1() throws Exception {
		String content = getContent("BSN.txt");
		try {
			MvcResult mvcResult = mockMvc.perform(post("/param/MDP").param("content", content))
					.andExpect(status().isOk()).andDo(print()).andReturn();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void executeParamMDPTest2() throws Exception {
		String content = getContent("Test4.txt");
		try {
			MvcResult mvcResult = mockMvc.perform(post("/param/MDP").param("content", content))
					.andExpect(status().isOk()).andDo(print()).andReturn();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void executeParamMDPTest3() throws Exception {
		String content = getContent("Test5.txt");
		try {
			MvcResult mvcResult = mockMvc.perform(post("/param/MDP").param("content", content))
					.andExpect(status().isOk()).andDo(print()).andReturn();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void executeParamMDPTest4() throws Exception {
		String content = getContent("Test6.txt");
		try {
			MvcResult mvcResult = mockMvc.perform(post("/param/MDP").param("content", content))
					.andExpect(status().isOk()).andDo(print()).andReturn();	
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void executeParamMDPTest5() throws Exception {
		String content = getContent("Test7.txt");
		try {
			MvcResult mvcResult = mockMvc.perform(post("/param/MDP").param("content", content))
					.andExpect(status().isOk()).andDo(print()).andReturn();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void executeParamMDPTest6() throws Exception {
		String content = getContent("Test8.txt");
		try {
			MvcResult mvcResult = mockMvc.perform(post("/param/MDP").param("content", content))
					.andExpect(status().isOk()).andDo(print()).andReturn();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void executeParamMDPTest7() throws Exception {
		String content = getContent("model.txt");
		
		try {
			MvcResult mvcResult = mockMvc.perform(post("/param/MDP").param("content", content))
					.andExpect(status().isOk()).andDo(print()).andReturn();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

//	@Test
//	public void executeMutRoSeTest() throws Exception {
//		String model = getContent("model.txt");
//		String configJson = getContent("configFile.json");
//		String configHddl = getContent("configHddl.hddl");
//		String world = getContent("worldKnowledge.xml");
//
//		MutRoSe content = new MutRoSe(model, configHddl, configJson, world);
//		try {
//			MvcResult mvcResult = mockMvc.perform(post("/load/multrose", content))
//					.andExpect(status().isOk())
//					.andDo(print()).andReturn();
//		} catch (Exception e) {
//			Assert.fail(e.getMessage());
//		}
//	}

	@Test
	public void executeMutRoSeTest1() throws Exception {
		String model = getContent("model.txt");
		String configHddl = getContent("configHddl.hddl");
		String world = getContent("worldKnowledge.xml");

		MutRoSe content = new MutRoSe(model, configHddl, null, world);
		RuntimeException exception = assertThrows(RuntimeException.class, () -> service.generateBinMultRoSe(content));
	}

	@Test
	public void executeMutRoSeTest2() throws Exception {
		String model = getContent("model.txt");
		String configJson = getContent("configFile.json");
		String configHddl = getContent("configHddl.hddl");

		MutRoSe content = new MutRoSe(model, configHddl, configJson, null);
		RuntimeException exception = assertThrows(RuntimeException.class, () -> service.generateBinMultRoSe(content));

	}

	@Test
	public void executeMutRoSeTest3() throws Exception {
		String model = getContent("model.txt");
		String configJson = getContent("configFile.json");
		String world = getContent("worldKnowledge.xml");

		MutRoSe content = new MutRoSe(model, null, configJson, world);
		RuntimeException exception = assertThrows(RuntimeException.class, () -> service.generateBinMultRoSe(content));

	}
}
