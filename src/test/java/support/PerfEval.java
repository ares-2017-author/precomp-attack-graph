package support;

import com.github.jesg.dither.Dither;
import attackgraph.Graph;

import java.util.concurrent.TimeUnit;

public class PerfEval {

    public static void main2( String[] args ) throws InterruptedException {
        Graph graph = TestUtils.generateRandomGraph(2, 3, 2500, 3, 0.3, 3, 0.2, 0.7);
        graph.sample();
        synchronized (args) {
            args.wait(11000);
        }

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Attacksteps: "+graph.size());
        OutputUtils.verboseOff();
        int size = graph.size();
        int reduced_size = 0;
        int cpt = 0;
        boolean go = true;
        while (go) {
            size = graph.size();
            TimeWatch tm = TimeWatch.start();
            if (cpt > 0) OutputUtils.verboseOn();
            graph.reduce();
            long reduction_time = tm.time(TimeUnit.MILLISECONDS);
            reduced_size = graph.size();
            OutputUtils.verboseOn();
            OutputUtils.printVerbose("Attacksteps: "+graph.attackStepsAsSet().size());
            OutputUtils.printVerbose("Run time: "+reduction_time);
            OutputUtils.verboseOff();
            cpt++;
            go = false;
        }
        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Iterations: " + cpt);
    }

    public static void main3( String[] args) {

        CSVFileWriter CSVFileWriter = new CSVFileWriter("graph-experiments-3ways.csv");
        TimeWatch tm;
        Object[][] allTestData = getExperimentData();
        System.out.println("records: "+allTestData.length);
        int cpt = 1;
        for(Object[] testData : allTestData) {
            System.out.println("experiment #"+cpt);
            Graph graph = TestUtils.generateRandomGraph((int)testData[0], (int)testData[1], (int)testData[2],
                    (int)testData[3], (double)testData[4], (int)testData[5], (double)testData[6], (double)testData[7]);
            graph.sample();
            int real_size = graph.size();
            float mean_parents = graph.meanParentNbr();
            float mean_children = graph.meanChildrenNbr();
            int min_children = graph.minChildrenNbr();
            int max_children = graph.maxChildrenNbr();
            int min_parents = graph.minParentsNbr();
            int max_parents = graph.maxParentsNbr();
            tm = TimeWatch.start();
            graph.reduce();
            double reduce_time = tm.time(TimeUnit.MILLISECONDS);
            float reduction_ratio = ((float) real_size / graph.size());
            CSVFileWriter.newRecord((int)testData[0], (int)testData[1], real_size,
                    reduction_ratio, (int)testData[3], (double)testData[4], (int)testData[5], (double)testData[6], (double)testData[7],
                    graph.getGraphDensity(), graph.size(),reduce_time, min_children,max_children,mean_children,min_parents,max_parents,
                    mean_parents);
            cpt++;
        }
        CSVFileWriter.close();
    }

    /* Individual experiments */
    public static void main(String args[]) {
        CSVFileWriter CSVFileWriter = new CSVFileWriter("graph-experiments-indiv.csv");
        TimeWatch tm;
        int nEntrySteps = 3;
        int nExitSteps = 4;
        int maxAttackSteps = 500;
        int maxChildren = 6;
        double pBinomialChildren = 0.35;
        int maxOldParents = 2;
        double pBinomialOldParents = 0.5;
        double pMinAttackSteps = 0.65;

        /* **** ENTRY STEPS **** */
        for (int nEntry = 1; nEntry < 100; nEntry = nEntry + 4) {
            Graph graph = TestUtils.generateRandomGraph(nEntry, nExitSteps, maxAttackSteps,
                    maxChildren, pBinomialChildren, maxOldParents, pBinomialOldParents, pMinAttackSteps);
            graph.sample();
            int real_size = graph.size();
            float mean_parents = graph.meanParentNbr();
            float mean_children = graph.meanChildrenNbr();
            int min_children = graph.minChildrenNbr();
            int max_children = graph.maxChildrenNbr();
            int min_parents = graph.minParentsNbr();
            int max_parents = graph.maxParentsNbr();
            tm = TimeWatch.start();
            graph.reduce();
            double reduce_time = tm.time(TimeUnit.MILLISECONDS);
            float reduction_ratio = ((float) real_size / graph.size());
            CSVFileWriter.newRecord(nEntry, nExitSteps, real_size, reduction_ratio, maxChildren, pBinomialChildren, maxOldParents, pBinomialOldParents,
                    pMinAttackSteps, graph.getGraphDensity(), graph.size(), reduce_time, min_children,max_children,mean_children,min_parents,max_parents,
                    mean_parents);
        }

        /* **** EXIT STEPS **** */
        for (int nExit = 1; nExit < 100; nExit = nExit + 4) {
            Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExit, maxAttackSteps,
                    maxChildren, pBinomialChildren, maxOldParents, pBinomialOldParents, pMinAttackSteps);
            graph.sample();
            int real_size = graph.size();
            float mean_parents = graph.meanParentNbr();
            float mean_children = graph.meanChildrenNbr();
            int min_children = graph.minChildrenNbr();
            int max_children = graph.maxChildrenNbr();
            int min_parents = graph.minParentsNbr();
            int max_parents = graph.maxParentsNbr();
            tm = TimeWatch.start();
            graph.reduce();
            double reduce_time = tm.time(TimeUnit.MILLISECONDS);
            float reduction_ratio = ((float) real_size / graph.size());
            CSVFileWriter.newRecord(nExitSteps, nExit, real_size, reduction_ratio, maxChildren, pBinomialChildren, maxOldParents, pBinomialOldParents,
                    pMinAttackSteps, graph.getGraphDensity(), graph.size(), reduce_time, min_children,max_children,mean_children,min_parents,max_parents,
                    mean_parents);
        }

        /* **** Graph Size **** */
        for (int maxSteps = 100; maxSteps < 13000; maxSteps = maxSteps * 2) {
            Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExitSteps, maxSteps,
                    maxChildren, pBinomialChildren, maxOldParents, pBinomialOldParents, pMinAttackSteps);
            graph.sample();
            int real_size = graph.size();
            float mean_parents = graph.meanParentNbr();
            float mean_children = graph.meanChildrenNbr();
            int min_children = graph.minChildrenNbr();
            int max_children = graph.maxChildrenNbr();
            int min_parents = graph.minParentsNbr();
            int max_parents = graph.maxParentsNbr();
            tm = TimeWatch.start();
            graph.reduce();
            double reduce_time = tm.time(TimeUnit.MILLISECONDS);
            float reduction_ratio = ((float) real_size / graph.size());
            CSVFileWriter.newRecord(nEntrySteps, nExitSteps, real_size, reduction_ratio, maxChildren, pBinomialChildren, maxOldParents, pBinomialOldParents,
                    pMinAttackSteps, graph.getGraphDensity(), graph.size(), reduce_time,min_children,max_children,mean_children,min_parents,max_parents,
                    mean_parents);
        }

        /* **** NbrChildren **** */
        for (double probaChild = 0.1; probaChild <= 1.0; probaChild = probaChild + .4) {
            for (int nbChildren = 1; nbChildren < 15 ; nbChildren++) {
                Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExitSteps, maxAttackSteps,
                        nbChildren, probaChild, maxOldParents, pBinomialOldParents, pMinAttackSteps);
                graph.sample();
                int real_size = graph.size();
                float mean_parents = graph.meanParentNbr();
                float mean_children = graph.meanChildrenNbr();
                int min_children = graph.minChildrenNbr();
                int max_children = graph.maxChildrenNbr();
                int min_parents = graph.minParentsNbr();
                int max_parents = graph.maxParentsNbr();
                tm = TimeWatch.start();
                graph.reduce();
                double reduce_time = tm.time(TimeUnit.MILLISECONDS);
                float reduction_ratio = ((float) real_size / graph.size());
                CSVFileWriter.newRecord(nEntrySteps, nExitSteps, real_size, reduction_ratio, nbChildren, probaChild, maxOldParents, pBinomialOldParents,
                        pMinAttackSteps, graph.getGraphDensity(), graph.size(), reduce_time, min_children, max_children, mean_children, min_parents, max_parents,
                        mean_parents);
            }
        }

        /* **** NbrParents **** */
        for (double probaParent = 0.1; probaParent <= 1.0; probaParent = probaParent + .4) {
            for (int nbParentsn = 1; nbParentsn < 15 ; nbParentsn++) {
                Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExitSteps, maxAttackSteps,
                        maxChildren, pBinomialChildren, nbParentsn, probaParent, pMinAttackSteps);
                graph.sample();
                int real_size = graph.size();
                float mean_parents = graph.meanParentNbr();
                float mean_children = graph.meanChildrenNbr();
                int min_children = graph.minChildrenNbr();
                int max_children = graph.maxChildrenNbr();
                int min_parents = graph.minParentsNbr();
                int max_parents = graph.maxParentsNbr();
                tm = TimeWatch.start();
                graph.reduce();
                double reduce_time = tm.time(TimeUnit.MILLISECONDS);
                float reduction_ratio = ((float) real_size / graph.size());
                CSVFileWriter.newRecord(nEntrySteps, nExitSteps, real_size, reduction_ratio, maxChildren, pBinomialChildren, nbParentsn, probaParent,
                        pMinAttackSteps, graph.getGraphDensity(), graph.size(), reduce_time, min_children, max_children, mean_children, min_parents, max_parents,
                        mean_parents);
            }
        }

        /* **** OR/AND ratio **** */
        for (double orAndRatio = 0.0; orAndRatio <= 1.0; orAndRatio = orAndRatio + .05) {
            Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExitSteps, maxAttackSteps,
                    maxChildren, pBinomialChildren, maxOldParents, pBinomialOldParents, orAndRatio);
            graph.sample();
            int real_size = graph.size();
            float mean_parents = graph.meanParentNbr();
            float mean_children = graph.meanChildrenNbr();
            int min_children = graph.minChildrenNbr();
            int max_children = graph.maxChildrenNbr();
            int min_parents = graph.minParentsNbr();
            int max_parents = graph.maxParentsNbr();
            tm = TimeWatch.start();
            graph.reduce();
            double reduce_time = tm.time(TimeUnit.MILLISECONDS);
            float reduction_ratio = ((float) real_size / graph.size());
            CSVFileWriter.newRecord(nEntrySteps, nExitSteps, real_size, reduction_ratio, maxChildren, pBinomialChildren, maxOldParents, pBinomialOldParents,
                    orAndRatio, graph.getGraphDensity(), graph.size(), reduce_time, min_children, max_children, mean_children, min_parents, max_parents,
                    mean_parents);
        }

    }

    /* Individual Size */
    public static void main8(String args[]) {
        CSVFileWriter CSVFileWriter = new CSVFileWriter("graph-experiments-children.csv");
        TimeWatch tm;
        int nEntrySteps = 3;
        int nExitSteps = 4;
        int maxAttackSteps = 500;
        int maxChildren = 7;
        double pBinomialChildren = 0.6;
        int maxOldParents = 1;
        double pBinomialOldParents = 0.4;
        double pMinAttackSteps = 0.65;

        /* **** NbrChildren **** */
        for (double probaChild = 0.1; probaChild <= 1.0; probaChild = probaChild + .4) {
            for (int nbChildren = 1; nbChildren < 15 ; nbChildren++) {
                Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExitSteps, maxAttackSteps,
                        nbChildren, probaChild, maxOldParents, probaChild, pMinAttackSteps);
                graph.sample();
                int real_size = graph.size();
                float mean_parents = graph.meanParentNbr();
                float mean_children = graph.meanChildrenNbr();
                int min_children = graph.minChildrenNbr();
                int max_children = graph.maxChildrenNbr();
                int min_parents = graph.minParentsNbr();
                int max_parents = graph.maxParentsNbr();
                tm = TimeWatch.start();
                graph.reduce();
                double reduce_time = tm.time(TimeUnit.MILLISECONDS);
                float reduction_ratio = ((float) real_size / graph.size());
                CSVFileWriter.newRecord(nEntrySteps, nExitSteps, real_size, reduction_ratio, nbChildren, probaChild, maxOldParents, pBinomialOldParents,
                        pMinAttackSteps, graph.getGraphDensity(), graph.size(), reduce_time, min_children, max_children, mean_children, min_parents, max_parents,
                        mean_parents);
            }
        }

    }

    public static void main4(String args[]) {
        for(float i = 0; i < 10; i++) System.out.print((i/10)+", ");
    }

    private static Object[][] getExperimentData() {
        return Dither.ipog(3,new Object[][] {
                new Object[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
                        25}, // 0. Number of entry steps
                new Object[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
                        25}, // 1. Number of Exit Steps
                new Object[] { 50, 100,200,500,750,1000}, //2. Total number of attack steps
                new Object[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18}, // 3. Max Number of Children
                new Object[] { 0.1, 0.5, 1.0}, // 4. Binomial probability of children for each node
                 new Object[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18}, // 5. Max Number of parents
                new Object[] { 0.1, 0.5, 1.0}, // 6. Binomial probability of parent for each node
                new Object[] { 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0 }} // 7. Proportion of OR nodes
        );
    }
}
/*
attackgraph.**
computation.**
support.**
 */