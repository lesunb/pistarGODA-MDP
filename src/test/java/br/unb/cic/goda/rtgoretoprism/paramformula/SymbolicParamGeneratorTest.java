package br.unb.cic.goda.rtgoretoprism.paramformula;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


public class SymbolicParamGeneratorTest {

	
	SymbolicParamGenerator symbolic = new SymbolicParamGenerator();
	 Map<String, String> ctxInformation;

	@Before
	public void setup() {
		ctxInformation = new HashMap<String, String>();
	}

	
	@Test
	public void testContextFreeAndReliability() {
		String[] ids = {"T1", "T2"};
		String formula = symbolic.getAndReliability(ids, ctxInformation).toString();
		
		assertEq("( T1 * T2  )", formula);
	}
	
	@Test
	public void testContextDependentAndReliability() {
		String[] ids = {"T1", "T2"};
		ctxInformation.put("T1", "CTX_1");
		String formula = symbolic.getAndReliability(ids, ctxInformation).toString();
		
		assertEq("(  CTX_T1 * T1 * T2 )", formula);
	}
	
	
	@Test
	public void testContextDependentOrDMReliability() {
		
		String[] ids = {"T1", "T2"};
		ctxInformation.put("T1", "CTX_1");
		ctxInformation.put("T2", "CTX_2");
		String formula = symbolic.getOrReliability(ids, ctxInformation).toString();
		
		assertEq("(1-(1-CTX_T1*T1)*(1-CTX_T2*T2))", formula);
	}
	
	
	@Test
	public void testDMCost() {
		
		String[] ids = {"T1", "T2"};
		ctxInformation.put("T1", "CTX_1");
		ctxInformation.put("T2", "CTX_2");
		String formula = symbolic.getDMCost(ids, ctxInformation, false).toString();
		
		assertEq("(CTX_T1*T1 + CTX_T2*T2)", formula);
	}
	
	@Test
	public void testTryReliabilitytN1N2N3() {
		String[] ids = {"T1", "T2", "T3"};
		ctxInformation.put("T1", "CTX_1");
		ctxInformation.put("T2", "CTX_2");
		ctxInformation.put("T3", "CTX_3");
		
		String formula = symbolic.getTryReliability(ids, ctxInformation, false).toString();
		
		assertEq("(CTX_T1*T1 * CTX_T2*T2 + (1 - CTX_T1*T1)*CTX_T3*T3 )", formula);
		
	}
	
	
	
	@Test
	public void testTryCostEPMCN1N2N3() {
		String[] ids = {"T1", "T2", "T3"};
		ctxInformation.put("T1", "CTX_1");
		ctxInformation.put("T2", "CTX_2");
		ctxInformation.put("T3", "CTX_3");
		
		String formula = symbolic.getTryCost(ids, ctxInformation, false).toString();
		
		assertEq("(T1 + CTX_T1*R_T1*T2 + (1 - CTX_T1*R_T1) * T3 )", formula);
		
	}
	
	@Test
	public void testTryCostEPMCN1N2skip() {
		String[] ids = {"T1", "T2", "skip"};
		ctxInformation.put("T1", "CTX_1");
		ctxInformation.put("T2", "CTX_2");
		
		String formula = symbolic.getTryCost(ids, ctxInformation, false).toString();
		
		assertEq("(T1 + CTX_T1*R_T1*T2)", formula);
		
	}
	
	
	@Test
	public void testTryCostEPMCN1skipN3() {
		String[] ids = {"T1", "skip", "T3"};
		ctxInformation.put("T1", "CTX_1");
		ctxInformation.put("T3", "CTX_3");
		
		String formula = symbolic.getTryCost(ids, ctxInformation, false).toString();
		
		assertEq("(T1 + (1 - CTX_T1*R_T1) * T3)", formula);
	}
		
	
	/* assert equivalent formulas, ignoring blank spaces */
	private void assertEq(String str1, String str2) {
		assertEquals(str1.replaceAll("\\s",""), str2.replaceAll("\\s",""));
	}

	
}
