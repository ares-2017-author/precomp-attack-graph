package support;

import attackgraph.AttackStep;
import attackgraph.AttackStepMax;
import attackgraph.AttackStepMin;
import attackgraph.Graph;
import computation.GraphComputer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MaxSumComparator {

    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final Object [] FILE_HEADER = {"Number Of Entry Nodes","Number Of Exit Nodes",
            "Number of Attack-Steps","Max Number Of Children", "Probability of Binomial Children",
            "Max Number Of Parents", "Probability of Binomial Parents", "Probability of AttackStepMin",
            "Average % Difference Max1 / Sum (Mins With Max Ancestors)",
            "Average % Difference Max1 / Sum (All Mins)", "Average % Difference Max1 / Sum (Maxs)"};

    @Test
    public void testMaxNodeComparisonsA() throws IOException {
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
        try (FileWriter fileWriter = new FileWriter("test.csv");
             CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat)) {

            csvFilePrinter.printRecord(FILE_HEADER);

            int nEntrySteps = 1;
            int nExitSteps = 1;
            int maxAttackSteps = 50;
            int maxChildren = 3;
            double pBinomialChildren = 0.4;
            int maxOldParents = 3;
            double pBinomialOldParents = 0.2;
            double pMinAttackSteps = 0.5;

            int changingFactor = nEntrySteps;
            for (int i=0; i < 5; i++) {
                List results = genAndComputeGraph(changingFactor, nExitSteps, maxAttackSteps, maxChildren,
                        pBinomialChildren, maxOldParents, pBinomialOldParents, pMinAttackSteps);

                csvFilePrinter.printRecord(results);
                changingFactor++;
            }

            changingFactor = nExitSteps;
            for (int i=0; i < 5; i++) {
                List results = genAndComputeGraph(nEntrySteps, changingFactor, maxAttackSteps, maxChildren,
                        pBinomialChildren, maxOldParents, pBinomialOldParents, pMinAttackSteps);

                csvFilePrinter.printRecord(results);
                changingFactor++;
            }

            changingFactor = maxAttackSteps -30;
            for (int i=0; i < 30; i++) {
                List results = genAndComputeGraph(nEntrySteps, nExitSteps, changingFactor, maxChildren,
                        pBinomialChildren, maxOldParents, pBinomialOldParents, pMinAttackSteps);

                csvFilePrinter.printRecord(results);
                changingFactor += 10;
            }

            changingFactor = maxChildren -2;
            for (int i=0; i < 10; i++) {
                List results = genAndComputeGraph(nEntrySteps, nExitSteps, maxAttackSteps, changingFactor,
                        pBinomialChildren, maxOldParents, pBinomialOldParents, pMinAttackSteps);

                csvFilePrinter.printRecord(results);
                changingFactor ++;
            }

            changingFactor = maxOldParents -2;
            for (int i=0; i < 10; i++) {
                List results = genAndComputeGraph(nEntrySteps, nExitSteps, maxAttackSteps, maxChildren,
                        pBinomialChildren, changingFactor, pBinomialOldParents, pMinAttackSteps);

                csvFilePrinter.printRecord(results);
                changingFactor ++;
            }

            double changingFactor2 = 0;
            for (int i=0; i < 20; i++) {
                List results = genAndComputeGraph(nEntrySteps, nExitSteps, maxAttackSteps, maxChildren,
                        changingFactor2, maxOldParents, pBinomialOldParents, pMinAttackSteps);

                csvFilePrinter.printRecord(results);
                changingFactor2 += 0.05;
            }

            changingFactor2 = 0;
            for (int i=0; i < 20; i++) {
                List results = genAndComputeGraph(nEntrySteps, nExitSteps, maxAttackSteps, maxChildren,
                        pBinomialChildren, maxOldParents, changingFactor2, pMinAttackSteps);

                csvFilePrinter.printRecord(results);
                changingFactor2 += 0.05;
            }

            changingFactor2 = 0;
            for (int i=0; i < 20; i++) {
                List results = genAndComputeGraph(nEntrySteps, nExitSteps, maxAttackSteps, maxChildren,
                        pBinomialChildren, maxOldParents, pBinomialOldParents, changingFactor2);

                csvFilePrinter.printRecord(results);
                changingFactor2 += 0.05;
            }

        }

    }
    public List genAndComputeGraph(int nEntrySteps, int nExitSteps, int maxAttackSteps, int maxChildren,
                                       double pBinomialChildren, int maxOldParents, double pBinomialOldParents,
                                       double pMinAttackSteps) {

        Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExitSteps, maxAttackSteps, maxChildren, pBinomialChildren,
                maxOldParents, pBinomialOldParents, pMinAttackSteps);
        graph.sample();
        GraphComputer gc = new GraphComputer();
        HashMap<AttackStep, AttackStepResult>asResults = new HashMap<>();
        for (AttackStep as : graph.attackStepsAsList()) {
            asResults.put(as, new AttackStepResult(as));
        }

        gc.computeGraph(graph);
        for (AttackStep as : graph.attackStepsAsList()) {
            asResults.get(as).setMax1Ttc(as.getTtc());
        }

        graph.attackStepsAsList().stream().forEach(AttackStep::softReset);

        gc.computeGraph(graph);
        for (AttackStep as : graph.attackStepsAsList()) {
            asResults.get(as).setSumTtc(as.getTtc());
        }

        List results = new ArrayList<>();
        results.add(nEntrySteps);
        results.add(nExitSteps);
        results.add(maxAttackSteps);
        results.add(maxChildren);
        results.add(pBinomialChildren);
        results.add(maxOldParents);
        results.add(pBinomialOldParents);
        results.add(pMinAttackSteps);

        List<AttackStepResult> allMins = asResults.values().stream().filter(a -> !a.isMax()).collect(Collectors.toList());
        List<AttackStepResult> allMaxs = asResults.values().stream().filter(a -> a.isMax()).collect(Collectors.toList());

        List<AttackStepResult> minsWithAtLeastOneMaxParent = getMinsWithMaxParents(allMins);

        double minDiff = 0;
        double minMaxDiff = 0;
        double maxDiff = 0;

        for (AttackStepResult asr : allMins) {
            minDiff += asr.getMax1SumDifference();
        }
        for (AttackStepResult asr : minsWithAtLeastOneMaxParent) {
            minMaxDiff += asr.getMax1SumDifference();
        }

        for (AttackStepResult asr : allMaxs) {
            maxDiff += asr.getMax1SumDifference();
        }

        results.add(minMaxDiff / minsWithAtLeastOneMaxParent.size());
        results.add(minDiff / allMins.size());
        results.add(maxDiff / allMaxs.size());
//        for (AttackStepResult asr : asResults.values()) {
//            System.out.println(asr.as.getName() + ": " + asr.max1Ttc + " / " + asr.sumTtc);
//        }
        return results;
    }

    private List<AttackStepResult> getMinsWithMaxParents(List<AttackStepResult> allMins) {
        List<AttackStepResult> minsWithoutMaxParent = new ArrayList<>();
        for (AttackStepResult asr : allMins) {
            if (((AttackStepMin)asr.getAttackStep()).hasAMaxDecisiveAncestor())
                minsWithoutMaxParent.add(asr);
        }
        return minsWithoutMaxParent;
    }


    class AttackStepResult {
        AttackStep as;
        double max0Ttc;
        double max1Ttc;
        double sumTtc;
        boolean isMax;

        AttackStepResult (AttackStep as) {
            this.as = as;
            if (as instanceof AttackStepMax) isMax = true;
            else isMax = false;
        }

        public double getMax0Ttc() {
            return max0Ttc;
        }

        public void setMax0Ttc(double max0Ttc) {
            this.max0Ttc = max0Ttc;
        }

        public double getMax1Ttc() {
            return max1Ttc;
        }

        public void setMax1Ttc(double max1Ttc) {
            this.max1Ttc = max1Ttc;
        }

        public double getSumTtc() {
            return sumTtc;
        }

        public void setSumTtc(double sumTtc) {
            this.sumTtc = sumTtc;
        }

        public boolean isMax() {
            return isMax;
        }

        public double getMax1SumDifference() {
            if (max1Ttc == sumTtc) return 0;
            else return 100-(max1Ttc*100/sumTtc);
        }

        public AttackStep getAttackStep() {
            return as;
        }
    }
}
