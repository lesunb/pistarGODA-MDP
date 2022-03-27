package br.unb.cic.integration;

import static br.unb.cic.goda.testutils.AssertUtils.assertEq;
import static br.unb.cic.integration.Controller.transformToTao4meEntities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.unb.cic.goda.model.Actor;
import br.unb.cic.goda.model.Goal;
import br.unb.cic.goda.rtgoretoprism.action.RunParamAction;
import br.unb.cic.goda.rtgoretoprism.generator.goda.producer.PARAMProducer;
import br.unb.cic.goda.rtgoretoprism.generator.goda.producer.PARAMProducer.Formulas;
import br.unb.cic.pistar.model.PistarModel;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PARAMProducerTest {
	@Test
	public void formula_DM() throws Exception {
		Formulas formulas = readModelAndGetFormulas("formulas/dm.json", false);

		assertEq("(1-(1-CTX_G0_T1*G0_T1)*(1-CTX_G0_T2*G0_T2))", formulas.reliability);
		assertEq("(CTX_G0_T1*W_G0_T1+CTX_G0_T2*W_G0_T2)", formulas.cost);
				
	}
	
	@Test
	public void formula_simple_and() throws Exception {
		Formulas formulas = readModelAndGetFormulas("formulas/simple_seq_and.json", false);

		assertEq("(G0_T1*G0_T2)", formulas.reliability);
		assertEq("(W_G0_T1+(G0_T1)*W_G0_T2)", formulas.cost);
	}

	@Test
	public void formula_simple_or() throws Exception {
		Formulas formulas = readModelAndGetFormulas("formulas/simple_seq_or.json", false);

		assertEq("(1 - (1-G0_T1)*(1-G0_T2))", formulas.reliability);
		assertEq("(W_G0_T1+((1-G0_T1))*W_G0_T2)", formulas.cost);
	}
	
	@Test
	public void formula_retry() throws Exception {
		Formulas formulas = readModelAndGetFormulas("formulas/retry.json", false);

		assertEq("(G0_T1+G0_T1*(1-G0_T1)^1+G0_T1*(1-G0_T1)^2+G0_T1*(1-G0_T1)^3)", formulas.reliability);
		assertEq("(W_G0_T1+W_G0_T1*(1-G0_T1)^1+W_G0_T1*(1-G0_T1)^2+W_G0_T1*(1-G0_T1)^3)", formulas.cost);
				
	}
	
	@Test
	public void formula_try_with_N1N2N3() throws Exception {
		Formulas formulas = readModelAndGetFormulas("formulas/try_3_params.json", false);

		assertEq("(G0_T1*G0_T2+(1-G0_T1)*G0_T3)", formulas.reliability);
		assertEq("(W_G0_T1 + G0_T1 * W_G0_T2 + (1-G0_T1)*W_G0_T3)", formulas.cost);
		
		
	}
	
	@Test
	public void formula_try_with_N1N2skip() throws Exception {
		Formulas formulas = readModelAndGetFormulas("formulas/try_n1_n2_skip.json", false);

		assertEq("(G0_T1*G0_T2 + (1-G0_T1))", formulas.reliability);
		assertEq("(W_G0_T1 + G0_T1*W_G0_T2)", formulas.cost);
				
	}
	
	@Test
	public void formula_try_with_N1skipN3() throws Exception {
		Formulas formulas = readModelAndGetFormulas("formulas/try_n1_skip_n3.json", false);

		assertEq("(G0_T1 + (1-G0_T1)*G0_T3)", formulas.reliability);
		assertEq("(W_G0_T1 + (1 - G0_T1)*W_G0_T3)", formulas.cost);
				
	}
	

	
	@Test
	public void testCase1() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test1.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}
	
	@Test
	public void testCase2() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test2.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}
	
	@Test
	public void testCase3() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test3.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase4() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test4.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase5() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test5.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase6() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test6.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase7() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test7.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase8() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test8.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase9() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test9.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase10() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test10.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase11() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test11.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase12() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test12.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase13() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test13.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase14() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test14.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase15() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test15.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase16() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test16.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase17() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test17.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase18() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test18.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase19() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test19.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase20() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test20.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase21() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test21.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase22() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test22.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase23() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test23.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase24() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test24.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase25() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test25.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase26() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test26.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase27() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test27.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase28() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test28.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase29() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test29.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase30() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test30.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase31() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test31.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase32() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test32.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase33() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test33.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase34() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test34.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	@Test
	public void testCase35() throws Exception {
		Formulas formulas = readModelAndGetFormulas("Test35.txt", false);

		assertEq("", formulas.reliability);
		assertEq("", formulas.cost);
	}

	private String getContent(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get("src/main/resources/testFiles/" + path)));
	}

	
	private Formulas readModelAndGetFormulas(String caseFileName, boolean isParam) throws Exception {
		String content = getContent(caseFileName);

		Gson gson = new GsonBuilder().create();
		PistarModel model = gson.fromJson(content, PistarModel.class);
		Set<Actor> selectedActors = new HashSet<>();
		Set<Goal> selectedGoals = new HashSet<>();
		transformToTao4meEntities(model, selectedActors, selectedGoals);

		String sourceFolder = "src/main/resources/TemplateInput";
		String targetFolder = "dtmc";
		String toolsFolder = "tools";
		Formulas formula;
		try {
			new RunParamAction(selectedActors, selectedGoals, false).run();

			
			PARAMProducer producer = new PARAMProducer(selectedActors, selectedGoals, isParam, sourceFolder, targetFolder,
					toolsFolder);

			Actor actor = (Actor) selectedActors.toArray()[0];

			formula = producer.generateFormulas(actor);
			
		} catch (Exception e) {
			System.err.println("Error generating parametric formula for file " + caseFileName);
			System.out.println(content);
			throw e;
		}

		return formula;
	}

}
