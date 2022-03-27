package br.unb.cic.goda.rtgoretoprism.generator.goda.producer;

import br.unb.cic.goda.model.Actor;
import br.unb.cic.goda.model.GeneralEntity;
import br.unb.cic.goda.model.Goal;
import br.unb.cic.goda.model.Plan;
import br.unb.cic.goda.rtgoretoprism.generator.CodeGenerationException;
import br.unb.cic.goda.rtgoretoprism.generator.goda.parser.CostParser;
import br.unb.cic.goda.rtgoretoprism.generator.goda.parser.RTParser;
import br.unb.cic.goda.rtgoretoprism.generator.goda.writer.PrismWriter;
import br.unb.cic.goda.rtgoretoprism.generator.kl.AgentDefinition;
import br.unb.cic.goda.rtgoretoprism.model.kl.Const;
import br.unb.cic.goda.rtgoretoprism.model.kl.GoalContainer;
import br.unb.cic.goda.rtgoretoprism.model.kl.PlanContainer;
import br.unb.cic.goda.rtgoretoprism.model.kl.RTContainer;
import br.unb.cic.goda.rtgoretoprism.util.FileUtility;
import br.unb.cic.goda.rtgoretoprism.util.kl.TroposNavigator;

import java.io.IOException;
import java.util.*;

public class RTGoreProducer {

	private TroposNavigator tn; 
	private String inputFolder;
	private String outputFolder;
	private Set<Actor> allActors;
	private Set<Goal> allGoals;
	private Integer prevMax = 0;
	private Integer timeSlotMax = 1;
	private List<String> successTry;
	
	/** memory for the parsed RT regex */
	private List<String> rtDMGoals;
	private Map<String, Object[]> rtRetryGoals;
	private Map<String, String[]> rtTryGoals;
	private Map<String, Boolean[]> rtSortedGoals;
	

	public RTGoreProducer(Set<Actor> allActors, Set<Goal> allGoals, String in, String out) {

		tn = new TroposNavigator();

		this.inputFolder = in;
		this.outputFolder = out;
		this.allActors = allActors;
		this.allGoals = allGoals;

		this.successTry = new ArrayList<String>();
		this.rtDMGoals = new ArrayList<String>();
		this.rtRetryGoals = new TreeMap<>();
		this.rtSortedGoals = new TreeMap<>();
        this.rtTryGoals = new TreeMap<>();
	}

	/**
	 * Run the process by which the Jadex source code is generated for the
	 * specified Tropos (system) actors
	 * 
	 * @throws CodeGenerationException 
	 * @throws IOException 
	 */
	public AgentDefinition run() throws CodeGenerationException, IOException {
		System.out.println("Starting PRISM Model Generation Process (Knowledge Level)" );
		System.out.println("\tTemplate Input Folder: " + inputFolder );
		System.out.println("\tOutput Folder: " + outputFolder );

		long startTime = new Date().getTime();
		AgentDefinition ad = null;

		for( Actor a : allActors ) {
			System.out.println( "Generating MDP model for: " + a.getName() );

			//generate the AgentDefinition object for the current actor
			ad = new AgentDefinition( a );

			// analyse all root goals
			for( Goal rootgoal : tn.getRootGoals(a) ) {
				Const type = Const.ACHIEVE; 
				Const request = Const.NONE;
				
				//create the goalcontainer for this one
				GoalContainer gc = ad.createGoal(rootgoal, type);
				gc.setRequest(request);

				//add to the root goal list
				ad.addRootGoal(gc);
				addGoal(rootgoal, gc, ad, false);
			}		
			List<Plan> planList = a.getPlanList();

			PrismWriter writer = new PrismWriter(ad, planList, inputFolder, outputFolder, false);
			writer.writeModel();

			//Generate pctl formulas
			generatePctlFormulas(ad);
		}
		System.out.println( "MDP model created in " + (new Date().getTime() - startTime) + "ms.");
		return ad;
	}

	private void generatePctlFormulas(AgentDefinition ad) throws IOException {

		StringBuilder pmax = new StringBuilder("Pmax=? [ F \"success\" ]");
		StringBuilder pmin = new StringBuilder("Pmin=? [ F \"success\" ]");
		StringBuilder rmax = new StringBuilder("R{\"cost\"}max=? [ F \"success\" ]");
		StringBuilder rmin = new StringBuilder("R{\"cost\"}min=? [ F \"success\" ]");

		FileUtility.writeFile(pmax.toString(), outputFolder + "/ReachabilityMax.pctl");
		FileUtility.writeFile(pmin.toString(), outputFolder + "/ReachabilityMin.pctl");
		FileUtility.writeFile(rmax.toString(), outputFolder + "/CostMax.pctl");
		FileUtility.writeFile(rmin.toString(), outputFolder + "/CostMin.pctl");
	}

	private void addGoal(Goal g, GoalContainer gc, final AgentDefinition ad, boolean included) throws IOException {

		included = included || allGoals.isEmpty() || allGoals.contains(g);
		gc.setIncluded(included);
		
		String rtRegex = gc.getRtRegex();
		boolean dmRT = false;
		dmRT = storeRegexResults(gc.getUid(), rtRegex, gc.getDecomposition());

		List<Goal> declist = g.getDecompositionList();
		sortIntentionalElements(declist);
		if (g.isAndDecomposition())
			gc.createDecomposition(Const.AND);			
		else if (g.isOrDecomposition())
			gc.createDecomposition(Const.OR);	

		if (dmRT) gc.setDecisionMaking(this.rtDMGoals);

        iterateGoals(ad, gc, declist, included);
        iterateRts(gc, gc.getDecompGoals());
        iterateMeansEnds(g, gc, ad, included);
        iterateRts(gc, gc.getDecompPlans());
		
		if (gc.isDecisionMaking()) {
			storeDecisionMakingNodes(gc);
			if (gc.getRoot() != null) {
				gc.getRoot().setTimeSlot(this.timeSlotMax);
				gc.getRoot().setPrevTimeSlot(this.prevMax);	
			}
		}

		if (gc.getClearElId().contains("X")) {
			gc.setOptional(true);
			PlanContainer unknownPlan = new PlanContainer((Plan) gc);
			unknownPlan.setElId("TX");
			unknownPlan.setTimeSlot(gc.getTimeSlot());
			unknownPlan.setPrevTimeSlot(gc.getPrevTimeSlot());
			unknownPlan.setOptional(true);
			gc.addMERealPlan(unknownPlan);
		}
	}

	private void iterateGoals(AgentDefinition ad, GoalContainer gc, List<Goal> decList, boolean include) throws IOException{

		gc.setRootTimeSlot(gc.getTimeSlot());

		for (Goal dec : decList) {
			boolean newgoal = !ad.containsGoal(dec);
			boolean parDec = false;

			GoalContainer deccont = ad.createGoal(dec, Const.ACHIEVE);
			gc.addDecomp(deccont);
			
			if (gc.isDecisionMaking()) {
				deccont.setPrevTimeSlot(gc.getPrevTimeSlot()+1);
				deccont.setTimeSlot(gc.getTimeSlot()+1);
			}
			else {
				deccont.setPrevTimeSlot(gc.getPrevTimeSlot());
				deccont.setTimeSlot(gc.getTimeSlot());
				if (rtSortedGoals.containsKey(deccont.getElId())) {
                    Boolean[] decDeltaPathTime = rtSortedGoals.get(deccont.getElId());
                    if (decDeltaPathTime[0]) {
                    	 parDec = true;
                    	 deccont.setPrevTimeSlot(gc.getPrevTimeSlot()-1);
                    	 deccont.setTimeSlot(gc.getTimeSlot()-1);
                    }
				}
			}
			
            if (rtRetryGoals.containsKey(deccont.getElId())) {
                Object[] retry = rtRetryGoals.get(deccont.getElId());
                Const cardType = (Const) retry[0];
                deccont.setCardType(cardType);
            }
			
			//deccont.addFulfillmentConditions(gc.getFulfillmentConditions());
			if (newgoal){
				addGoal(dec, deccont, ad, include);	
				
				if (gc.isDecisionMaking()) {
					this.prevMax = deccont.getPrevTimeSlot()+1;
					this.timeSlotMax = deccont.getTimeSlot()+1;
				}
				else if (deccont.isDecisionMaking()) {
					gc.setTimeSlot(this.timeSlotMax);
					gc.setPrevTimeSlot(this.prevMax);
				}
				else if (!parDec) {
					gc.setTimeSlot(deccont.getTimeSlot());
					gc.setPrevTimeSlot(deccont.getPrevTimeSlot());
				}
			}	
		}
	}

	private void addPlan(Plan p, PlanContainer pc, final AgentDefinition ad) throws IOException {

		List<Plan> decList = p.getEndPlans();
        sortIntentionalElements(decList);
        if (p.isAndDecomposition())
        	pc.createDecomposition(Const.AND);
        else
        	pc.createDecomposition(Const.OR);
		
		boolean dmRT = false;
		if (!decList.isEmpty()){
			dmRT = storeRegexResults(pc.getUid(), pc.getRtRegex(), pc.getDecomposition());
		}
		else { //P is a leaf-task
			pc.setCostRegex(pc.getRtRegex());
			pc.setRtRegex(null);
			storeCostResults(pc);
		}

		if (dmRT) pc.setDecisionMaking(this.rtDMGoals);

        iteratePlans(ad, pc, decList);
        iterateRts(pc, pc.getDecompPlans());
		if (pc.isDecisionMaking()) {
			storeDecisionMakingNodes(pc);
			if (pc.getRoot() != null) {
				pc.getRoot().setTimeSlot(this.timeSlotMax);
				pc.getRoot().setPrevTimeSlot(this.prevMax);	
			}
		}

		if (pc.getClearElId().contains("X")) pc.setOptional(true);
	}

	private void iterateRts(RTContainer gc, List<? extends RTContainer> rts) {
		for (RTContainer dec : rts) {
			String elId = dec.getElId();
			LinkedList<RTContainer> decPlans = RTContainer.fowardMeansEnd(dec, new LinkedList<>());

			if (rtRetryGoals.containsKey(elId)) {
				Object[] card = rtRetryGoals.get(elId);
				Const cardType = (Const) card[0];
				Integer cardNumber = (Integer) card[1];
				for (RTContainer decPlan : decPlans) {
					decPlan.setCardType(cardType);
					decPlan.setCardNumber(cardNumber);
				}
			}
			if (rtTryGoals.get(elId) != null) {
                String[] tryGoals = rtTryGoals.get(elId);
                if (tryGoals[0] != null) {
                    RTContainer successPlan = gc.getDecompElement(tryGoals[0]);
                    LinkedList<RTContainer> decSucessPlans = RTContainer.fowardMeansEnd(successPlan, new LinkedList<>());
                    for (RTContainer decPlan : decPlans) {
                        decPlan.setTrySuccess(successPlan);
                    }
                    for (RTContainer decSucessPlan : decSucessPlans) {
                        decSucessPlan.setTryOriginal(dec);
                        decSucessPlan.setSuccessTry(true);
                    }
                }
                if (tryGoals[1] != null) {
                    RTContainer failurePlan = gc.getDecompElement(tryGoals[1]);
                    LinkedList<RTContainer> decFailurePlans = RTContainer.fowardMeansEnd(failurePlan, new LinkedList<>());
                    for (RTContainer decPlan : decPlans) {
                        decPlan.setTryFailure(failurePlan);
                    }
                    for (RTContainer decFailurePlan : decFailurePlans) {
                        decFailurePlan.setTryOriginal(dec);
                        decFailurePlan.setSuccessTry(false);
                    }
                }
            }
		}
	}

	private void storeDecisionMakingNodes(RTContainer pc) {

		List<String> idNodes = pc.getDecisionMaking();
		LinkedList<RTContainer> nodes = new LinkedList<RTContainer>();

		for (String id : idNodes) {
			nodes.add(pc.getDecompElement(id));
		}
		pc.setDecisionNodes(nodes);
	}

	private void iteratePlans(AgentDefinition ad, PlanContainer pc, List<Plan> decList) throws IOException{

		for (Plan dec : decList) {
			boolean newplan = !ad.containsPlan(dec);
			boolean parDec = false;
			
			PlanContainer deccont = ad.createPlan(dec);
			pc.addDecomp(deccont);
			
			if (pc.isDecisionMaking()) {
				deccont.setPrevTimeSlot(pc.getPrevTimeSlot()+1);
				deccont.setTimeSlot(pc.getTimeSlot()+1);
			}
			else {
				deccont.setPrevTimeSlot(pc.getPrevTimeSlot());
				deccont.setTimeSlot(pc.getTimeSlot());
				if (rtSortedGoals.containsKey(deccont.getElId())) {
                    Boolean[] decDeltaPathTime = rtSortedGoals.get(deccont.getElId());
                    if (decDeltaPathTime[0]) {
                    	 parDec = true;
                    	 deccont.setPrevTimeSlot(pc.getPrevTimeSlot()-1);
                    	 deccont.setTimeSlot(pc.getTimeSlot()-1);
                    }
				}
			}

			if (rtRetryGoals.containsKey(deccont.getElId())) {
                Object[] retry = rtRetryGoals.get(deccont.getElId());
                Const cardType = (Const) retry[0];
                deccont.setCardType(cardType);
            }
			
			//deccont.addFulfillmentConditions(pc.getFulfillmentConditions());
			if (newplan){
				addPlan(dec, deccont, ad);
				if (pc.isDecisionMaking()) {
					if (deccont.getDecompPlans().isEmpty()) {
						this.prevMax = (deccont.getPrevTimeSlot()+1)>this.prevMax?deccont.getPrevTimeSlot()+1:this.prevMax;
						this.timeSlotMax = (deccont.getTimeSlot()+1)>this.timeSlotMax?deccont.getTimeSlot()+1:this.timeSlotMax;
					}
					else {
						this.prevMax = (deccont.getPrevTimeSlot())>this.prevMax?deccont.getPrevTimeSlot():this.prevMax;
						this.timeSlotMax = (deccont.getTimeSlot())>this.timeSlotMax?deccont.getTimeSlot():this.timeSlotMax;	
					}
				}
				else if (deccont.getDecompPlans().isEmpty()) {
					String id = deccont.getElId();
					if (rtTryGoals.containsKey(id)) {
						Object[] rtTry = rtTryGoals.get(id);
						this.successTry.add((String) rtTry[0]);
						
						pc.setPrevTimeSlot(deccont.getPrevTimeSlot());
						pc.setTimeSlot(deccont.getTimeSlot());
					}
					else if (this.successTry.contains(id)) {
						pc.setPrevTimeSlot(deccont.getPrevTimeSlot());
						pc.setTimeSlot(deccont.getTimeSlot());
					}
					else {
						pc.setPrevTimeSlot(deccont.getPrevTimeSlot()+1);
						pc.setTimeSlot(deccont.getTimeSlot()+1);	
					}
				}
				else if (!parDec){
					pc.setPrevTimeSlot(deccont.getPrevTimeSlot());
					pc.setTimeSlot(deccont.getTimeSlot());
				}
			}
		}
	}

	private void iterateMeansEnds(Goal g, GoalContainer gc, final AgentDefinition ad, boolean included) throws IOException{

		if (included && !g.getMeansToAnEndPlans().isEmpty()){
			List<Plan> melist = g.getMeansToAnEndPlans();
			sortIntentionalElements(melist);
			gc.createDecomposition(Const.ME);
			
			for (Plan p : melist) {
				boolean newplan = !ad.containsPlan(p);
				boolean parDec = false;
				
				PlanContainer pc = ad.createPlan(p);
				gc.addMERealPlan(pc);

				pc.setPrevTimeSlot(gc.getPrevTimeSlot());
				pc.setTimeSlot(gc.getTimeSlot());
				
				if (rtSortedGoals.containsKey(pc.getElId())) {
                    Boolean[] decDeltaPathTime = rtSortedGoals.get(pc.getElId());
                    if (decDeltaPathTime[0]) {
                    	 parDec = true;
                    	 pc.setPrevTimeSlot(gc.getPrevTimeSlot()-1);
                    	 pc.setTimeSlot(gc.getTimeSlot()-1);
                    }
				}

				if (rtRetryGoals.containsKey(pc.getElId())) {
	                Object[] retry = rtRetryGoals.get(pc.getElId());
	                Const cardType = (Const) retry[0];
	                pc.setCardType(cardType);
	            }
				//pc.addFulfillmentConditions(gc.getFulfillmentConditions());
				
				if (newplan){
					addPlan(p, pc, ad);					
					if (pc.getDecompPlans().isEmpty()) {
						gc.setPrevTimeSlot(pc.getPrevTimeSlot()+1);
						gc.setTimeSlot(pc.getTimeSlot()+1);
					}
					else if (pc.isDecisionMaking()) {
						gc.setPrevTimeSlot(this.prevMax);
						gc.setTimeSlot(this.timeSlotMax);
					}
					else if (!parDec){
						gc.setPrevTimeSlot(pc.getPrevTimeSlot());
						gc.setTimeSlot(pc.getTimeSlot());
					}
				}
			}			
		}
	}

	@SuppressWarnings("unchecked")
	private boolean storeRegexResults(String uid, String rtRegex, Const decType) throws IOException {
		if(rtRegex != null){
			Object [] res = RTParser.parseRegex(uid, rtRegex + '\n', decType, false);
			rtDMGoals.addAll((List<String>) res [2]);
			rtRetryGoals.putAll((Map<String, Object[]>) res[3]);
			rtTryGoals.putAll((Map<String, String[]>) res[4]);
			rtSortedGoals.putAll((Map<String, Boolean[]>) res[5]);

			List<String> dmList = (List<String>) res[2];
			if (!dmList.isEmpty()) return true;
		}
		return false;
	}

	private void storeCostResults(PlanContainer pc) throws IOException {
		if (pc.getCostRegex() != null) {
			Object [] res = CostParser.parseRegex(pc.getCostRegex());
			pc.setCostValue((String) res[0]);
			pc.setCostVariable((String) res[1]);
		}
	}

	private void sortIntentionalElements(List<? extends GeneralEntity> elements){

		Collections.sort(elements, new Comparator<GeneralEntity>() {
			@Override
			public int compare(GeneralEntity gA, GeneralEntity gB) {
				String idA = AgentDefinition.parseElId(gA.getName()).replaceAll("[TG]", "");
				String idB = AgentDefinition.parseElId(gB.getName()).replaceAll("[TG]", "");
				String idAStringPart = idA.replaceAll("\\d", "");
				String idBStringPart = idB.replaceAll("\\d", "");

				if(idAStringPart.equalsIgnoreCase(idBStringPart))
				{
					return extractInt(idA) - extractInt(idB);
				}
				return idA.compareToIgnoreCase(idB);
			}
		});
	}

	int extractInt(String s) {
		String num = s.replaceAll("\\D", "");
		return num.isEmpty() ? 0 : Integer.parseInt(num);
	}
}
