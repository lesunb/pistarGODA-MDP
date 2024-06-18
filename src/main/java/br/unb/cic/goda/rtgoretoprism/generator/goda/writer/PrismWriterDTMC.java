package br.unb.cic.goda.rtgoretoprism.generator.goda.writer;

import br.unb.cic.goda.model.Plan;
import br.unb.cic.goda.rtgoretoprism.generator.CodeGenerationException;
import br.unb.cic.goda.rtgoretoprism.generator.goda.parser.CtxParser;
import br.unb.cic.goda.rtgoretoprism.generator.kl.AgentDefinition;
import br.unb.cic.goda.rtgoretoprism.model.ctx.ContextCondition;
import br.unb.cic.goda.rtgoretoprism.model.ctx.CtxSymbols;
import br.unb.cic.goda.rtgoretoprism.model.kl.Const;
import br.unb.cic.goda.rtgoretoprism.model.kl.GoalContainer;
import br.unb.cic.goda.rtgoretoprism.model.kl.PlanContainer;
import br.unb.cic.goda.rtgoretoprism.model.kl.RTContainer;
import br.unb.cic.goda.rtgoretoprism.util.PathLocation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class PrismWriterDTMC {

    private static final String MODULE_NAME_TAG = "$MODULE_NAME$";
    private static final String NO_ERROR_TAG = "$NO_ERROR$";
    private static final String TIME_SLOT_TAG = "$TIME_SLOT";
    private static final String PREV_TIME_SLOT_TAG = "$PREV_TIME_SLOT";
    private static final String GID_TAG = "$GID$";
    private static final String DEFAULT_VAL = "$DEFAULT_VAL$";
    private static final String PREV_GID_TAG = "$PREV_GID$";
    private static final String GOAL_MODULES_TAG = "$GOAL_MODULES$";
    private static final String SKIPPED_TAG = "$SKIPPED$";
    private static final String NOT_SKIPPED_TAG = "$NOT_SKIPPED$";
    private static final String DEC_HEADER_TAG = "$DEC_HEADER$";
    private static final String DEC_TYPE_TAG = "$DEC_TYPE$";
    private static final String MAX_TRIES_TAG = "$MAX_TRIES$";
    private static final String MAX_RETRIES_TAG = "$MAX_RETRIES$";
    private static final String CARD_N_TAG = "$CARD_N$";
    private static final String CTX_CONDITION_TAG = "$CTX_CONDITION$";
    private static final String CTX_EFFECT_TAG = "$CTX_EFFECT$";
    private static final String CONST_PARAM_TAG = "$CONST_PARAM$";
    private static final String PARAMS_BASH_TAG = "$PARAMS_BASH$";
    private static final String REPLACE_BASH_TAG = "$REPLACE_BASH$";

    private final String constOrParam;
    private final String TEMPLATE_PRISM_BASE_PATH = "PRISM/";

    private String templateInputBaseFolder;
    private String inputPRISMFolder;
    private String basicOutputFolder;
    private String basicAgentPackage;
    private String header, body, evalBash;
    private String noErrorFormula = "";
    private StringBuilder planModules = new StringBuilder();
    private String evalFormulaParams = "";
    private String evalFormulaReplace = "";

    /**
     * PRISM patterns
     */
    private String leafGoalPattern;
    private String andDecPattern;
    private String xorDecPattern;
    private String xorDecHeaderPattern;
    private String xorNotSkippedPattern;
    private String seqRenamePattern;
    private String intlRenamePattern;
    private String trySDecPattern;
    private String tryFDecPattern;
    private String optDecPattern;
    private String optHeaderPattern;
    private String seqCardPattern;
    private String intlCardPattern;
    private String rtryCardPattern;
    private String ctxGoalPattern;
    private String ctxTaskPattern;
    private String prevFailurePattern;

    private AgentDefinition ad;
    private List<Plan> capabilityPlanList;

    private Map<String, String> ctxVars;

    public PrismWriterDTMC(AgentDefinition ad, List<Plan> capPlan, String input, String output) {
        this.ad = ad;
        this.capabilityPlanList = capPlan;
        this.templateInputBaseFolder = input + "/";
        this.inputPRISMFolder = templateInputBaseFolder + TEMPLATE_PRISM_BASE_PATH;
        this.basicOutputFolder = output + "/";
        this.basicAgentPackage = PathLocation.BASIC_AGENT_PACKAGE_PREFIX + ad.getAgentName();
        this.constOrParam = "const";
        this.ctxVars = new TreeMap<>();
    }

    public void writeModel() throws CodeGenerationException, IOException {
        String utilPkgName = basicAgentPackage + PathLocation.UTIL_KL_PKG;
        String prismInputFolder = inputPRISMFolder;
        String planOutputFolder = basicOutputFolder + "plans" + "/";
        String planPkgName = basicAgentPackage + ".plans";

        header = ManageWriter.readFileAsString(prismInputFolder + "modelheader.pm");
        body = ManageWriter.readFileAsString(prismInputFolder + "modelbody.pm");
        evalBash = ManageWriter.readFileAsString(prismInputFolder + "eval_formula.sh");

        writeAnOutputDir(basicOutputFolder);
        PrintWriter modelFile = ManageWriter.createFile(ad.getAgentName() + ".pm", basicOutputFolder);
        PrintWriter evalBashFile = ManageWriter.createFile("eval_formula.sh", basicOutputFolder);
        writePrismModel(prismInputFolder, ad.rootlist, planOutputFolder, basicAgentPackage, utilPkgName, planPkgName);
        printModel(modelFile);
        printEvalBash(evalBashFile);
    }

    private void writePrismModel(String input, LinkedList<GoalContainer> rootGoals,
                                 String planOutputFolder, String pkgName, String utilPkgName, String planPkgName) throws CodeGenerationException, IOException {
        leafGoalPattern = ManageWriter.readFileAsString(input + "pattern_leafgoal.pm");
        andDecPattern = ManageWriter.readFileAsString(input + "pattern_and.pm");
        xorDecPattern = ManageWriter.readFileAsString(input + "pattern_xor.pm");
        xorDecHeaderPattern = ManageWriter.readFileAsString(input + "pattern_xor_header.pm");
        xorNotSkippedPattern = ManageWriter.readFileAsString(input + "pattern_skip_not_xor.pm");
        seqRenamePattern = ManageWriter.readFileAsString(input + "pattern_seq_rename.pm");
        intlRenamePattern = ManageWriter.readFileAsString(input + "pattern_intl_rename.pm");
        trySDecPattern = ManageWriter.readFileAsString(input + "pattern_try_success.pm");
        tryFDecPattern = ManageWriter.readFileAsString(input + "pattern_try_fail.pm");
        optDecPattern = ManageWriter.readFileAsString(input + "pattern_opt.pm");
        optHeaderPattern = ManageWriter.readFileAsString(input + "pattern_opt_header.pm");
        seqCardPattern = ManageWriter.readFileAsString(input + "pattern_card_seq.pm");
        intlCardPattern = ManageWriter.readFileAsString(input + "pattern_card_intl.pm");
        rtryCardPattern = ManageWriter.readFileAsString(input + "pattern_card_retry.pm");
        ctxGoalPattern = ManageWriter.readFileAsString(input + "pattern_ctx_goal.pm");
        ctxTaskPattern = ManageWriter.readFileAsString(input + "pattern_ctx_task.pm");
        prevFailurePattern = ManageWriter.readFileAsString(input + "pattern_prev_failure.pm");
//        Collections.sort(rootGoals);
        for (GoalContainer root : rootGoals) {
            writeElement(root, leafGoalPattern, null);
            StringBuilder sbCtxVars = new StringBuilder();
            for (String ctx : ctxVars.keySet())
                sbCtxVars.append(constOrParam + " " + ctxVars.get(ctx) + " " + ctx + ";\n");
            planModules = planModules.append(sbCtxVars.toString());
        }
        cleanPlanModules();
    }

    private String[] writeElement(RTContainer root, String pattern, String prevFormula) throws IOException {
        String operator = root.getDecomposition() == Const.AND ? " & " : " | ";
        if (!root.getDecompGoals().isEmpty()) {
            StringBuilder goalFormula = new StringBuilder();
            String prevGoalFormula = prevFormula;
            int prevTimeSlot = root.getDecompGoals().get(0).getRootTimeSlot();

            for (GoalContainer gc : root.getDecompGoals()) {
                String currentFormula;
                if (prevTimeSlot < gc.getRootTimeSlot())
                    currentFormula = prevGoalFormula;
                else
                    currentFormula = prevFormula;
                writeElement(gc, pattern, currentFormula);
                if (gc.isIncluded())
                    prevGoalFormula = gc.getClearElId();
                if (prevGoalFormula != null)
                    goalFormula.append(prevGoalFormula + operator);
            }
            if (prevGoalFormula != null)
                goalFormula.replace(goalFormula.lastIndexOf(operator), goalFormula.length(), "");
            if (root.isIncluded())
                planModules = planModules.append("\nformula " + root.getClearElId() + " = " + goalFormula + ";\n");

            return new String[]{root.getClearElId(), goalFormula.toString()};

        } else if (!root.getDecompPlans().isEmpty()) {
            StringBuilder taskFormula = new StringBuilder();
            String prevTaskFormula = prevFormula;
            for (PlanContainer pc : root.getDecompPlans()) {
                String childFormula = writeElement(pc, pattern, prevTaskFormula)[1];
                if (!childFormula.isEmpty())
                    taskFormula.append("(" + childFormula + ")" + operator);
            }
            if (taskFormula.length() > 0)
                taskFormula.replace(taskFormula.lastIndexOf(operator), taskFormula.length(), "");
            if (root instanceof GoalContainer)
                planModules = planModules.append("\nformula " + root.getClearElId() + " = " + taskFormula + ";\n");
            return new String[]{root.getClearElId(), taskFormula.toString()};
        } else if (root instanceof PlanContainer) {
            return writePrismModule(root, pattern, prevFormula);
        }
        return new String[]{"", ""};
    }

    @SuppressWarnings("unchecked")
    private String[] writePrismModule(RTContainer root, String singlePattern, String prevFormula) throws IOException {
        singlePattern = new String(singlePattern);
        String seqCardPattern = new String(this.seqCardPattern),
                intlCardPattern = new String(this.intlCardPattern),
                andDecPattern = new String(this.andDecPattern),
                xorDecPattern = new String(this.xorDecPattern),
                xorDecHeaderPattern = new String(this.xorDecHeaderPattern),
                trySDecPattern = new String(this.trySDecPattern),
                tryFDecPattern = new String(this.tryFDecPattern),
                optHeaderPattern = new String(this.optHeaderPattern),
                optDecPattern = new String(this.optDecPattern);
        PlanContainer plan = (PlanContainer) root;
        String planModule;
        StringBuilder planFormula = new StringBuilder();
        if (plan.getCardNumber() > 1) {
            StringBuilder renames = new StringBuilder();
            if (plan.getCardType() == Const.SEQ) {
                for (int i = 2; i <= plan.getCardNumber(); i++) {
                    String seqRename = new String(seqRenamePattern);
                    seqRename = seqRename.replace(CARD_N_TAG, i + "");
                    renames.append(seqRename);
                }
                seqCardPattern = seqCardPattern.replace("$SEQ_RENAMES$", renames);
                planModule = seqCardPattern.replace(MODULE_NAME_TAG, plan.getClearElName());
            } else if (plan.getCardType() == Const.INT){
                for (int i = 2; i <= plan.getCardNumber(); i++) {
                    String intlRename = new String(intlRenamePattern);
                    intlRename = intlRename.replace(CARD_N_TAG, i + "");
                    renames.append(intlRename);
                }
                intlCardPattern = intlCardPattern.replace("$SEQ_RENAMES$", renames);
                planModule = intlCardPattern.replace(MODULE_NAME_TAG, plan.getClearElName());
            } else {
                planModule = rtryCardPattern.replace(MODULE_NAME_TAG, plan.getClearElName());
            }
        } else
            planModule = singlePattern.replace(MODULE_NAME_TAG, plan.getClearElName());
        StringBuilder sbHeader = new StringBuilder();
        StringBuilder sbType = new StringBuilder();
        if ((plan.getTryOriginal() != null || plan.getTrySuccess() != null || plan.getTryFailure() != null) ||
                (!plan.getAlternatives().isEmpty() || !plan.getFirstAlternatives().isEmpty()) ||
                (plan.isOptional())) {
            if (plan.getTryOriginal() != null || plan.getTrySuccess() != null || plan.getTryFailure() != null) {
                if (plan.getTrySuccess() != null || plan.getTryFailure() != null) {
                    if (plan.getAlternatives().isEmpty() && plan.getFirstAlternatives().isEmpty())
                        sbType.append(andDecPattern);
                    appendTryToNoErrorFormula(plan);
                    processPlanFormula(plan, planFormula, Const.TRY);
                } else if (plan.isSuccessTry()) {
                    PlanContainer tryPlan = (PlanContainer) plan.getTryOriginal();
                    trySDecPattern = trySDecPattern.replace(PREV_GID_TAG, tryPlan.getClearElId());
                    sbType.append(trySDecPattern);
                    processPlanFormula(plan, planFormula, Const.TRY_S);
                } else {
                    PlanContainer tryPlan = (PlanContainer) plan.getTryOriginal();
                    tryFDecPattern = tryFDecPattern.replace(PREV_GID_TAG, tryPlan.getClearElId());
                    sbType.append(tryFDecPattern);
                    processPlanFormula(plan, planFormula, Const.TRY_F);
                }
            }
            if (!plan.getAlternatives().isEmpty() || !plan.getFirstAlternatives().isEmpty()) {
                String xorNotSkippeds = new String();
                StringBuilder xorHeaders = new StringBuilder();
                String xorNotSkipped = new String(xorNotSkippedPattern);
                if (false && constOrParam.equals("param")) {
                    String xorVar = new String(xorDecHeaderPattern);
                    evalFormulaParams += "XOR_" + plan.getClearElId() + "=\"0\";\n";
                    evalFormulaReplace += " -e \"s/XOR_" + plan.getClearElId() + "/$XOR_" + plan.getClearElId() + "/g\"";
                    xorHeaders.append(xorVar.replace(GID_TAG, plan.getClearElId()));
                    sbHeader.append(xorHeaders);
                    xorNotSkippeds = xorNotSkippeds.concat(xorNotSkipped.replace(GID_TAG, plan.getClearElId()) + "*");
                } else {
                    if (!plan.getAlternatives().isEmpty()) {
                        for (RTContainer altFirst : plan.getAlternatives().keySet()) {
                            String xorVar = new String(xorDecHeaderPattern);
                            evalFormulaParams += "XOR_" + altFirst.getClearElId() + "=\"0\";\n";
                            evalFormulaReplace += " -e \"s/XOR_" + altFirst.getClearElId() + "/$XOR_" + altFirst.getClearElId() + "/g\"";
                            xorHeaders.append(xorVar.replace(GID_TAG, altFirst.getClearElId()));
                            xorNotSkippeds = xorNotSkipped.replace(GID_TAG, altFirst.getClearElId());

                            LinkedList<RTContainer> alts = plan.getAlternatives().get(altFirst);
                            for (RTContainer alt : alts) {
                                xorVar = new String(xorDecHeaderPattern);
                                evalFormulaParams += "XOR_" + alt.getClearElId() + "=\"0\";\n";
                                evalFormulaReplace += " -e \"s/XOR_" + alt.getClearElId() + "/$XOR_" + alt.getClearElId() + "/g\"";
                                xorHeaders.append(xorVar.replace(GID_TAG, alt.getClearElId()));
                            }
                        }
                        sbHeader.append(xorHeaders);
                        processPlanFormula(plan, planFormula, Const.XOR);
                    }
                    if (!plan.getFirstAlternatives().isEmpty()) {
                        for (RTContainer firstAlt : plan.getFirstAlternatives()) {
                            for (RTContainer alt : firstAlt.getAlternatives().get(firstAlt)) {
                                if (alt.equals(plan) || equalsRoot(alt,plan)) {
                                    xorNotSkipped = new String(xorNotSkippedPattern);
                                    xorNotSkippeds = xorNotSkipped.replace(GID_TAG, alt.getClearElId());
                                }
                            }
                        }
                        processPlanFormula(plan, planFormula, Const.XOR);
                    }
                }
                xorNotSkippeds = xorNotSkippeds.trim();
                xorNotSkippeds = xorNotSkippeds.replaceAll("[\n]", "");
                xorDecPattern = xorDecPattern.replace(NOT_SKIPPED_TAG, xorNotSkippeds);
                sbType.append(xorDecPattern.replace(SKIPPED_TAG, "(1 - " + xorNotSkippeds + ")"));
            }
            if (plan.isOptional()) {
                sbHeader.append(optHeaderPattern);
                sbType.append(optDecPattern);
                noErrorFormula += " & s" + plan.getClearElId() + " < 4";
                evalFormulaParams += "OPT_" + plan.getClearElId() + "=\"1\";\n";
                evalFormulaReplace += " -e \"s/OPT_" + plan.getClearElId() + "/$OPT_" + plan.getClearElId() + "/g\"";
                processPlanFormula(plan, planFormula, Const.OPT);
            }
        } else {
            sbType.append(andDecPattern + "\n\n");
            noErrorFormula += " & s" + plan.getClearElId() + " < 4";
            processPlanFormula(plan, planFormula, plan.getRoot().getDecomposition());
        }

        evalFormulaParams += "rTask" + plan.getClearElId() + "=\"0.999\";\n";
        evalFormulaReplace += " -e \"s/rTask" + plan.getClearElId() + "/$rTask" + plan.getClearElId() + "/g\"";
        planModule = planModule.replace(DEC_HEADER_TAG, sbHeader.toString() + "\n");
        planModule = planModule.replace(DEC_TYPE_TAG, sbType.toString());
        if (constOrParam.equals("const") &&
                (!plan.getFulfillmentConditions().isEmpty() ||
                        !plan.getAdoptionConditions().isEmpty())) {
            StringBuilder fulfillmentContition = new StringBuilder(),
                    adoptionContition = new StringBuilder(),
                    ctxEffect = new StringBuilder();
            if (!plan.getFulfillmentConditions().isEmpty()) {
                for (String ctxCondition : plan.getFulfillmentConditions()) {
                    Object[] parsedCtxs = CtxParser.parseRegex(ctxCondition);
                    List<ContextCondition> ctxConditions = (List<ContextCondition>) parsedCtxs[0];
                    addCtxVar(ctxConditions);
                    if ((CtxSymbols) parsedCtxs[2] == CtxSymbols.COND) {
                        fulfillmentContition.append(fulfillmentContition.length() > 0 ? " & " : "")
                                .append(parsedCtxs[1]);
                    } else {
                        adoptionContition.append(adoptionContition.length() > 0 ? " & " : "")
                                .append(parsedCtxs[1]);
                    }
                }
                if (fulfillmentContition.length() > 0) {
                    String ctxGoalPattern = new String(this.ctxGoalPattern);
                    ctxGoalPattern = ctxGoalPattern.replace(CTX_CONDITION_TAG, "(" + fulfillmentContition.toString() + ")" + " &");
                    ctxEffect.append(ctxGoalPattern);
                }
                if (adoptionContition.length() > 0) {
                    if (fulfillmentContition.length() > 0) {
                        ctxEffect.append("\n\t");
                        fulfillmentContition.append(" & ");
                    }
                    String ctxTaskPattern = new String(this.ctxTaskPattern);
                    ctxTaskPattern = ctxTaskPattern.replace(CTX_CONDITION_TAG, "(" + adoptionContition.toString() + ")" + " &");
                    ctxEffect.append(ctxTaskPattern);
                }
            }
            planModule = planModule.replace(CTX_EFFECT_TAG, ctxEffect.toString());
            planModule = planModule.replace(CTX_CONDITION_TAG, "(" + fulfillmentContition.append(adoptionContition).toString() + ")" + " &");
        } else {
            planModule = planModule.replace(CTX_EFFECT_TAG, "");
            planModule = planModule.replace(CTX_CONDITION_TAG, "");
        }
        //Prev Failure Guard Condition
        planModule = planModule.replace("$PREV_EFFECT$", buildPrevFailureFormula(prevFormula));
        //Prev Success Guard Condition
        planModule = planModule.replace("$PREV_SUCCESS$", buildPrevSuccessFormula(prevFormula, plan));
        Integer prevTimePath = plan.getPrevTimePath();
        Integer timePath = plan.getTimePath();
        Integer timeSlot = plan.getTimeSlot()-1;
        if (plan.getCardType().equals(Const.SEQ))
            timeSlot -= plan.getCardNumber() - 1;
        for (int i = plan.getCardNumber(); i >= 0; i--) {
            planModule = planModule.replace(PREV_TIME_SLOT_TAG + (i > 1 ? "_N" + i : "") + "$", prevTimePath + "_" + (timeSlot - 1 + i) + "");
            planModule = planModule.replace(TIME_SLOT_TAG + (i > 1 ? "_N" + i : "") + "$", timePath + "_" + (timeSlot + i) + "");
        }
        planModule = planModule.replace(GID_TAG, plan.getClearElId());
        if (constOrParam.equals("const"))
            planModule = planModule.replace(DEFAULT_VAL, "=0.99");
        else
            planModule = planModule.replace(DEFAULT_VAL, "");
        planModule = planModule.replace(CONST_PARAM_TAG, constOrParam);
        planModule = planModule.replace(MAX_TRIES_TAG, plan.getCardNumber() + 1 + "");
        planModule = planModule.replace(MAX_RETRIES_TAG, plan.getCardNumber() + "");
        planModules = planModules.append(planModule);
        return new String[]{plan.getClearElId(), planFormula.toString()};
    }

    /*Check if plan descends from alt*/
    private boolean equalsRoot(RTContainer alt, PlanContainer plan) {

        RTContainer root = plan.getRoot();
        while (!root.equals(this.ad.getRootGoalList().get(0))) {
            if (alt.equals(root)) return true;
            root = root.getRoot();
        }

        if (alt.equals(root)) return true;
        return false;
    }

    private void addCtxVar(List<ContextCondition> ctxs) {
        for (ContextCondition ctxCondition : ctxs)
            ctxVars.put(ctxCondition.getVar(), ctxCondition.getOp() == CtxSymbols.BOOL ? "bool" :
                    ctxCondition.getOp() == CtxSymbols.DOUBLE ? "double" : "int");
    }

    private void processPlanFormula(PlanContainer plan, StringBuilder planFormula, Const decType) throws IOException {
        String op = planFormula.length() == 0 ? "" : " & ";
        switch (decType) {
            case OR:
                planFormula.append(buildAndOrSuccessFormula(plan, planFormula, decType));
                break;
            case AND:
                planFormula.append(buildAndOrSuccessFormula(plan, planFormula, decType));
                break;
            case XOR:
                planFormula.append(buildXorSuccessFormula(plan, planFormula));
                break;
            case TRY:
                planFormula.append(buildTryOriginalFormula(plan, planFormula, decType, false));
                break;
            case TRY_S:
                break;
            case TRY_F:
                break;
            case OPT:
                planFormula.append(buildOptFormula(plan, planFormula));
                break;
            default:
                planFormula.append(op + "(s" + plan.getClearElId() + "=2)");
        }
    }

    private String buildAndOrSuccessFormula(RTContainer plan, StringBuilder planFormula, Const decType) throws IOException {
        String op = planFormula.length() == 0 ? "" : " & ";
        switch (decType) {
            case AND:
                return op + "(s" + plan.getClearElId() + "=2)" + buildContextSuccessFormula(plan);
            case OR:
                return op + "(s" + plan.getClearElId() + "=2)" + buildContextSuccessFormula(plan);
            default:
                return "";
        }
    }

    private String buildOptFormula(RTContainer plan, StringBuilder planFormula) throws IOException {
        String op = planFormula.length() == 0 ? "" : " & ";
        return op + "(s" + plan.getClearElId() + "=2 | s" + plan.getClearElId() + "=3)"
                + buildContextSuccessFormula(plan);
    }

    private String buildTryOriginalFormula(RTContainer plan, StringBuilder planFormula, Const decType, boolean inv) throws IOException {
        String op = planFormula.length() == 0 ? "" : " & ";
        return op + "("
                + "(s" + plan.getClearElId() + "=2 & "
                + buildTrySuccessFailureFormula(plan.getTrySuccess(), planFormula, Const.TRY_S, false)
                + ") | "
                + "(s" + plan.getClearElId() + "=4 & "
                + buildTrySuccessFailureFormula(plan.getTryFailure(), planFormula, Const.TRY_F, false)
                + ")"
                + buildContextSuccessFormula(plan)
                + ")";
    }

    private String buildTrySuccessFailureFormula(RTContainer plan, StringBuilder planFormula, Const decType, boolean inv) throws IOException {
        switch (decType) {
            case TRY_S:
                return plan != null ? "s" + plan.getClearElId() + "=" + (!inv ? "2" : "3") : (!inv ? "true" : "true");
            case TRY_F:
                return plan != null ? "s" + plan.getClearElId() + "=2" : "false";
            default:
                return "";
        }
    }

    private String buildXorSuccessFormula(PlanContainer plan, StringBuilder planFormula) throws IOException{
        StringBuilder sb = new StringBuilder();
        sb.append("s" + plan.getClearElId() + "=2 & ");

        if (!plan.getAlternatives().isEmpty()) {
            for(RTContainer altFirst: plan.getAlternatives().keySet())
                for(RTContainer alt : plan.getAlternatives().get(altFirst))
                    for(RTContainer decAlt : RTContainer.fowardMeansEnd(alt, new LinkedList<RTContainer>()))
                        sb.append("s" + decAlt.getClearElId() + "=3 & ");
        }

        if (!plan.getFirstAlternatives().isEmpty()) {
            for(RTContainer firstAlt: plan.getFirstAlternatives()){
                //Append the first node of alternatives
                for(RTContainer decAlt : RTContainer.fowardMeansEnd(firstAlt, new LinkedList<RTContainer>()))
                    if (!decAlt.equals(plan))
                        sb.append("s" + decAlt.getClearElId() + "=3 & ");

                //Append other nodes
                for(RTContainer alt : firstAlt.getAlternatives().get(firstAlt))
                    for(RTContainer decAlt : RTContainer.fowardMeansEnd(alt, new LinkedList<RTContainer>()))
                        if (!decAlt.equals(plan))
                            sb.append("s" + decAlt.getClearElId() + "=3 & ");
            }
        }

        sb.replace(sb.lastIndexOf(" & "), sb.length(), "");
        return sb.toString();
    }

    private String buildContextSuccessFormula(RTContainer plan) throws IOException {
        if (this.constOrParam.equals("param"))
            return "";
        StringBuilder sb = new StringBuilder();
        for (String ctxCondition : plan.getFulfillmentConditions())
            sb.append(sb.length() > 0 ? " | " : "(").append("!(" + CtxParser.parseRegex(ctxCondition)[1] + ")");
        if (sb.length() > 0)
            sb.insert(0, " | (s" + plan.getClearElId() + "=3 & ").append("))");
        return sb.toString();
    }

    private String buildPrevFailureFormula(String prevFormula) {
        if (prevFormula == null)
            return "";
        return new String(this.prevFailurePattern);
    }

    private String buildPrevSuccessFormula(String prevFormula, PlanContainer plan) {
        if (prevFormula == null)
            return "";
        StringBuilder sb = new StringBuilder("(" + prevFormula);
        for (RTContainer altFirst : plan.getAlternatives().keySet()) {
            for (RTContainer decAlt : RTContainer.fowardMeansEnd(altFirst, new LinkedList<RTContainer>()))
                if (!decAlt.equals(plan))
                    sb.append(" | s" + decAlt.getClearElId() + "=3");
                else
                    break;
        }
        for (RTContainer firstAlt : plan.getFirstAlternatives()) {
            for (RTContainer alt : firstAlt.getAlternatives().get(firstAlt))
                for (RTContainer decAlt : RTContainer.fowardMeansEnd(alt, new LinkedList<>()))
                    if (!decAlt.equals(plan)) {
                        sb.append(" | s" + decAlt.getClearElId() + "=3");
                    } else
                        break;
        }
        return sb.append(") & ").toString();
    }

    private void appendTryToNoErrorFormula(PlanContainer plan) {
        noErrorFormula += " & (s" + plan.getClearElId() + " < 4 | (true ";
        if (plan.getTrySuccess() != null) {
            RTContainer trySucessPlan = plan.getTrySuccess();
            noErrorFormula += " & s" + trySucessPlan.getClearElId() + " < 4";
        }
        if (plan.getTryFailure() != null) {
            RTContainer tryFailurePlan = plan.getTryFailure();
            noErrorFormula += " & s" + tryFailurePlan.getClearElId() + " < 4";
        }
        noErrorFormula += "))";
    }

    private void writeAnOutputDir(String output) throws CodeGenerationException {
        File dir = new File(output);
        if (!dir.exists() && !dir.mkdirs()) {
            String msg = "Error: Can't create directory \"" + dir + "\"!";
            System.out.println(msg);
            throw new CodeGenerationException(msg);
        }
    }

    private void printModel(PrintWriter adf) {
        header = header.replace(NO_ERROR_TAG, noErrorFormula);
        body = body.replace(GOAL_MODULES_TAG, planModules);
        ManageWriter.printModel(adf, header, body);
    }

    private void printEvalBash(PrintWriter pw) {
        evalBash = evalBash.replace(PARAMS_BASH_TAG, evalFormulaParams);
        evalBash = evalBash.replace(REPLACE_BASH_TAG, evalFormulaReplace);
        ManageWriter.printModel(pw, evalBash);
    }

    /*Remove multiple line breaks*/
    private void cleanPlanModules() {
        String planModule = planModules.toString();
        planModules = planModules.delete(0, planModules.length()-1);
        planModule = planModule.replaceAll("[\t\r\n][\t\r\n]+[\n]+", "\n\n");
        planModules = planModules.append(planModule);
    }
}