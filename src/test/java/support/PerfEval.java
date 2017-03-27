package support;

import attackgraph.Graph;
import com.github.jesg.dither.Dither;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class PerfEval {

    public static void main7(String[] args) throws InterruptedException {
        OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(2, 3, 2500, 3, 0.7, 2, 0.3, 1, 0.15, 2, 0.45, 0.7);
        OutputUtils.verboseOff();
        graph.sample();
//        synchronized (args) {
//            args.wait(11000);
//        }

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Attacksteps: " + graph.size());
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
            OutputUtils.printVerbose("Attacksteps: " + graph.attackStepsAsSet().size());
            OutputUtils.printVerbose("Run time: " + reduction_time + "ms.");
            OutputUtils.verboseOff();
            cpt++;
            go = false;
        }
        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Iterations: " + cpt);
    }

    public static void main9(String[] args) {

        CSVFileWriter csvFileWriter = new CSVFileWriter("graph-experiments-3ways-parallel.csv");
        Object[][] allTestData = getExperimentData();
        System.out.println("records: " + allTestData.length);
        final AtomicInteger cpt = new AtomicInteger(1);
        Arrays.stream(allTestData).parallel().forEach(testData -> {
 //       for (Object[] testData : allTestData) {
            System.out.println("experiment #" + cpt.get());
            Graph graph = TestUtils.generateRandomGraph((int) testData[0], (int) testData[1], (int) testData[2],
                    (int) testData[3], (double) testData[4], (int) testData[5], (double) testData[6], (int) testData[7],
                    (double) testData[8], (int) testData[9], (double) testData[10], (double) testData[11]);
            graph.sample();
            int real_size = graph.size();
            int min_children = graph.minChildrenNbr();
            int max_children = graph.maxChildrenNbr();
            float mean_children = graph.meanChildrenNbr();
            float mean_tree = graph.getMeanTreeEdges();
            float mean_forward = graph.getMeanForwardEdges();
            float mean_back = graph.getMeanBackEdges();
            float mean_cross = graph.getMeanCrossEdges();
            float graph_density = graph.getGraphDensity();
            TimeWatch tm = TimeWatch.start();
            graph.reduce();
            double reduce_time = tm.time(TimeUnit.MILLISECONDS);
            float reduction_ratio = ((float) real_size / graph.size());
            csvFileWriter.printNewRecord((int) testData[0], (int) testData[1], real_size,
                    reduction_ratio, (int) testData[3], (double) testData[4], (int) testData[5], (double) testData[6], (int) testData[7],
                    (double) testData[8], (int) testData[9], (double) testData[10], (double) testData[11],
                    graph_density, graph.size(), reduce_time, min_children, max_children, mean_children,
                    mean_tree,mean_forward,mean_back,mean_cross);
            cpt.incrementAndGet();
        });
        csvFileWriter.close();
    }

    /* Individual experiments */
    public static void main4(String args[]) {
        CSVFileWriter CSVFileWriter = new CSVFileWriter("graph-experiments-indiv-nosize.csv");
        TimeWatch tm;
        int nEntrySteps = 4;
        int nExitSteps = 5;
        int maxAttackSteps = 500;
        int nBinomialChildren = 4;
        double pBinomialChildren = 0.7;
        int nBinomialForwardEdges = 1;
        double pBinomialForwardEdges = 0.3;
        int nBinomialCrossEdges = 1;
        double pBinomialCrossEdges = 0.15;
        int nBinomialBackEdges = 1;
        double pBinomialBackEdges = 0.1;
        double pMinAttackSteps = 0.65;

        /* **** ENTRY STEPS **** */
        for (int nEntry = 1; nEntry < 100; nEntry = nEntry + 2) {
            Graph graph = TestUtils.generateRandomGraph(nEntry, nExitSteps, maxAttackSteps,
                    nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges, pMinAttackSteps);
            graph.sample();
            int real_size = graph.size();
            int min_children = graph.minChildrenNbr();
            int max_children = graph.maxChildrenNbr();
            float mean_children = graph.meanChildrenNbr();
            float mean_tree = graph.getMeanTreeEdges();
            float mean_forward = graph.getMeanForwardEdges();
            float mean_back = graph.getMeanBackEdges();
            float mean_cross = graph.getMeanCrossEdges();
            float graph_density = graph.getGraphDensity();

            tm = TimeWatch.start();
            graph.reduce();
            double reduce_time = tm.time(TimeUnit.MILLISECONDS);
            float reduction_ratio = ((float) real_size / graph.size());
            CSVFileWriter.printNewRecord(nEntry, nExitSteps, real_size, reduction_ratio, nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges,
                    nBinomialBackEdges,pBinomialBackEdges,nBinomialCrossEdges,pBinomialCrossEdges,
                    pMinAttackSteps, graph_density, graph.size(), reduce_time,min_children, max_children, mean_children, mean_tree,
                    mean_forward, mean_back, mean_cross);
        }

        /* **** EXIT STEPS **** */
        for (int nExit = 1; nExit < 100; nExit = nExit + 2) {
            Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExit, maxAttackSteps,
                    nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges, pMinAttackSteps);
            graph.sample();
            int real_size = graph.size();
            int min_children = graph.minChildrenNbr();
            int max_children = graph.maxChildrenNbr();
            float mean_children = graph.meanChildrenNbr();
            float mean_tree = graph.getMeanTreeEdges();
            float mean_forward = graph.getMeanForwardEdges();
            float mean_back = graph.getMeanBackEdges();
            float mean_cross = graph.getMeanCrossEdges();
            float graph_density = graph.getGraphDensity();
            tm = TimeWatch.start();
            graph.reduce();
            double reduce_time = tm.time(TimeUnit.MILLISECONDS);
            float reduction_ratio = ((float) real_size / graph.size());
            CSVFileWriter.printNewRecord(nEntrySteps, nExit, real_size, reduction_ratio, nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges,
                    nBinomialBackEdges,pBinomialBackEdges,nBinomialCrossEdges,pBinomialCrossEdges,
                    pMinAttackSteps, graph_density, graph.size(), reduce_time,min_children, max_children, mean_children, mean_tree,
                    mean_forward, mean_back, mean_cross);
        }

        /* **** Graph Size **** */
   /*     for (int maxSteps = 100; maxSteps < 13000; maxSteps = maxSteps * 2) {
            Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExitSteps, maxSteps,
                    maxChildren, pBinomialChildren, maxOldParents, pBinomialForwardEdges, pMinAttackSteps);
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
            CSVFileWriter.printNewRecord(nEntrySteps, nExitSteps, real_size, reduction_ratio, maxChildren, pBinomialChildren, maxOldParents, pBinomialForwardEdges,
                    pMinAttackSteps, graph_density, graph.size(), reduce_time, min_children, max_children, mean_children, min_parents, max_parents,
                    mean_parents);
        }*/

        /* **** DirectChildren **** */
        for (double probaChild = 0.1; probaChild <= 1.0; probaChild = probaChild + .4) {
            for (int nbChildren = 1; nbChildren < 10; nbChildren++) {
                Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExitSteps, maxAttackSteps,
                        nbChildren, probaChild, nBinomialForwardEdges, pBinomialForwardEdges, pMinAttackSteps);
                graph.sample();
                int real_size = graph.size();
                int min_children = graph.minChildrenNbr();
                int max_children = graph.maxChildrenNbr();
                float mean_children = graph.meanChildrenNbr();
                float mean_tree = graph.getMeanTreeEdges();
                float mean_forward = graph.getMeanForwardEdges();
                float mean_back = graph.getMeanBackEdges();
                float mean_cross = graph.getMeanCrossEdges();
                float graph_density = graph.getGraphDensity();
                tm = TimeWatch.start();
                graph.reduce();
                double reduce_time = tm.time(TimeUnit.MILLISECONDS);
                float reduction_ratio = ((float) real_size / graph.size());
                CSVFileWriter.printNewRecord(nEntrySteps, nExitSteps, real_size, reduction_ratio, nbChildren, probaChild, nBinomialForwardEdges, pBinomialForwardEdges,
                        nBinomialBackEdges,pBinomialBackEdges,nBinomialCrossEdges,pBinomialCrossEdges,
                        pMinAttackSteps, graph_density, graph.size(), reduce_time,min_children, max_children, mean_children, mean_tree,
                        mean_forward, mean_back, mean_cross);
            }
        }

        /* **** ForwardEdges **** */
        for (double pBinomialFW = 0.1; pBinomialFW <= 1.0; pBinomialFW = pBinomialFW + .4) {
            for (int nBinomialFW = 1; nBinomialFW < 8; nBinomialFW++) {
                Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExitSteps, maxAttackSteps,
                        nBinomialChildren, pBinomialChildren, nBinomialFW, pBinomialFW, pMinAttackSteps);
                graph.sample();
                int real_size = graph.size();
                int min_children = graph.minChildrenNbr();
                int max_children = graph.maxChildrenNbr();
                float mean_children = graph.meanChildrenNbr();
                float mean_tree = graph.getMeanTreeEdges();
                float mean_forward = graph.getMeanForwardEdges();
                float mean_back = graph.getMeanBackEdges();
                float mean_cross = graph.getMeanCrossEdges();
                float graph_density = graph.getGraphDensity();
                tm = TimeWatch.start();
                graph.reduce();
                double reduce_time = tm.time(TimeUnit.MILLISECONDS);
                float reduction_ratio = ((float) real_size / graph.size());
                CSVFileWriter.printNewRecord(nEntrySteps, nExitSteps, real_size, reduction_ratio, nBinomialChildren, pBinomialChildren, nBinomialFW, pBinomialFW,
                        nBinomialBackEdges,pBinomialBackEdges,nBinomialCrossEdges,pBinomialCrossEdges,
                        pMinAttackSteps, graph_density, graph.size(), reduce_time,min_children, max_children, mean_children, mean_tree,
                        mean_forward, mean_back, mean_cross);
            }
        }

        /* **** BackEdges **** */
        for (double pBinomialBW = 0.1; pBinomialBW <= 1.0; pBinomialBW = pBinomialBW + .4) {
            for (int nBinomialBW = 1; nBinomialBW < 8; nBinomialBW++) {
                Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExitSteps, maxAttackSteps,
                        nBinomialChildren, pBinomialChildren, nBinomialBW, pBinomialBW, pMinAttackSteps);
                graph.sample();
                int real_size = graph.size();
                int min_children = graph.minChildrenNbr();
                int max_children = graph.maxChildrenNbr();
                float mean_children = graph.meanChildrenNbr();
                float mean_tree = graph.getMeanTreeEdges();
                float mean_forward = graph.getMeanForwardEdges();
                float mean_back = graph.getMeanBackEdges();
                float mean_cross = graph.getMeanCrossEdges();
                float graph_density = graph.getGraphDensity();
                tm = TimeWatch.start();
                graph.reduce();
                double reduce_time = tm.time(TimeUnit.MILLISECONDS);
                float reduction_ratio = ((float) real_size / graph.size());
                CSVFileWriter.printNewRecord(nEntrySteps, nExitSteps, real_size, reduction_ratio, nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges,
                        nBinomialBW,pBinomialBW,nBinomialCrossEdges,pBinomialCrossEdges,
                        pMinAttackSteps, graph_density, graph.size(), reduce_time,min_children, max_children, mean_children, mean_tree,
                        mean_forward, mean_back, mean_cross);
            }
        }

        /* **** CrossEdges **** */
        for (double pBinomialCW = 0.1; pBinomialCW <= 1.0; pBinomialCW = pBinomialCW + .4) {
            for (int nBinomialCW = 1; nBinomialCW < 8; nBinomialCW++) {
                Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExitSteps, maxAttackSteps,
                        nBinomialChildren, pBinomialChildren, nBinomialCW, pBinomialCW, pMinAttackSteps);
                graph.sample();
                int real_size = graph.size();
                int min_children = graph.minChildrenNbr();
                int max_children = graph.maxChildrenNbr();
                float mean_children = graph.meanChildrenNbr();
                float mean_tree = graph.getMeanTreeEdges();
                float mean_forward = graph.getMeanForwardEdges();
                float mean_back = graph.getMeanBackEdges();
                float mean_cross = graph.getMeanCrossEdges();
                float graph_density = graph.getGraphDensity();
                tm = TimeWatch.start();
                graph.reduce();
                double reduce_time = tm.time(TimeUnit.MILLISECONDS);
                float reduction_ratio = ((float) real_size / graph.size());
                CSVFileWriter.printNewRecord(nEntrySteps, nExitSteps, real_size, reduction_ratio, nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges,
                        nBinomialBackEdges,pBinomialBackEdges,nBinomialCW,pBinomialCW,
                        pMinAttackSteps, graph_density, graph.size(), reduce_time,min_children, max_children, mean_children, mean_tree,
                        mean_forward, mean_back, mean_cross);
            }
        }

        /* **** OR/AND ratio **** */
        for (double orAndRatio = 0.0; orAndRatio <= 1.01; orAndRatio = orAndRatio + .025) {
            Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExitSteps, maxAttackSteps,
                    nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges, orAndRatio);
            graph.sample();
            int real_size = graph.size();
            int min_children = graph.minChildrenNbr();
            int max_children = graph.maxChildrenNbr();
            float mean_children = graph.meanChildrenNbr();
            float mean_tree = graph.getMeanTreeEdges();
            float mean_forward = graph.getMeanForwardEdges();
            float mean_back = graph.getMeanBackEdges();
            float mean_cross = graph.getMeanCrossEdges();
            float graph_density = graph.getGraphDensity();
            tm = TimeWatch.start();
            graph.reduce();
            double reduce_time = tm.time(TimeUnit.MILLISECONDS);
            float reduction_ratio = ((float) real_size / graph.size());
            CSVFileWriter.printNewRecord(nEntrySteps, nExitSteps, real_size, reduction_ratio, nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges,
                    nBinomialBackEdges,pBinomialBackEdges,nBinomialCrossEdges,pBinomialCrossEdges,
                    orAndRatio, graph.getGraphDensity(), graph.size(), reduce_time,min_children, max_children, mean_children, mean_tree,
                    mean_forward, mean_back, mean_cross);
        }

    }

    /* Individual Size */
    public static void main2(String args[]) {
        CSVFileWriter csvFileWriter = new CSVFileWriter("graph-experiments-children.csv");
        int nEntrySteps = 4;
        int nExitSteps = 5;
        int maxAttackSteps = 500;
        int nBinomialTreeEdges = 4;
        double pBinomialTreeEdges = 0.5;
        int nBinomialForwardEdges = 1;
        double pBinomialForwardEdges = 0.25;
        int nBinomialCrossEdges = 3;
        double pBinomialCrossEdges = 0.03;
        int nBinomialBackEdges = 3;
        double pBinomialBackEdges = 0.03;
        double pMinAttackSteps = 0.65;

        ExecutorService executor = Executors.newFixedThreadPool(4);

        /* **** NbrChildren **** */
        IntStream.range(1, 20).forEach(nbChildren -> {
                    Runnable task = () -> {
                        Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExitSteps, maxAttackSteps,
                                nbChildren, pBinomialTreeEdges, nBinomialForwardEdges, pBinomialForwardEdges, nBinomialCrossEdges, pBinomialCrossEdges, nBinomialBackEdges, pBinomialBackEdges, pMinAttackSteps);
                        graph.sample();
                        int real_size = graph.size();
                        int min_children = graph.minChildrenNbr();
                        int max_children = graph.maxChildrenNbr();
                        float mean_children = graph.meanChildrenNbr();
                        float mean_tree = graph.getMeanTreeEdges();
                        float mean_forward = graph.getMeanForwardEdges();
                        float mean_back = graph.getMeanBackEdges();
                        float mean_cross = graph.getMeanCrossEdges();
                        float graph_density = graph.getGraphDensity();
                        TimeWatch tm = TimeWatch.start();
                        graph.reduce();
                        double reduce_time = tm.time(TimeUnit.MILLISECONDS);
                        float reduction_ratio = ((float) real_size / graph.size());
                        csvFileWriter.addNewRecord(nEntrySteps, nExitSteps, real_size, reduction_ratio, nbChildren, pBinomialTreeEdges, nBinomialForwardEdges, pBinomialForwardEdges,
                                nBinomialBackEdges,pBinomialBackEdges,nBinomialCrossEdges,pBinomialCrossEdges,
                                pMinAttackSteps, graph_density, graph.size(), reduce_time,min_children, max_children, mean_children, mean_tree,
                                mean_forward, mean_back, mean_cross);
                    };
                    executor.submit(task);
        });
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        csvFileWriter.printAllRecords();
        csvFileWriter.close();
    }


    public static void main(String args[]) {
        for (int i = 1; i < 60; i=i+2) System.out.print(i + ", ");
    }

    public static void main3(String[] args) {
        int nEntrySteps = 4;
        int nExitSteps = 5;
        int maxAttackSteps = 500;
        int maxChildren = 20;
        double pBinomialChildren = 0.9;
        int maxOldParents = 4;
        double pBinomialOldParents = 0.5;
        double pMinAttackSteps = 0.65;
        Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExitSteps, maxAttackSteps,
                maxChildren, pBinomialChildren, maxOldParents, pBinomialOldParents, pMinAttackSteps);
        graph.sample();
        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph,0);
    }

    private static Object[][] getExperimentData() {
        return Dither.ipog(3, new Object[][]{
                new Object[]{1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, 41, 43, 45, 47,
                        49, 51, 53, 55, 57, 59}, // 0. Number of entry steps
                new Object[]{1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, 41, 43, 45, 47,
                        49, 51, 53, 55, 57, 59}, // 1. Number of Exit Steps
                new Object[]{50, 100, 200, 500, 750, 1000}, //2. Total number of attack steps
                new Object[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, //3. n Binomial for tree edges per node
                new Object[]{0.1, 0.5, 0.9}, // 4. p Binomial for tree edges per node
                new Object[]{0, 1, 2, 3, 4, 5, 6}, //5. n Binomial for forward edges per node
                new Object[]{0.1, 0.5, 0.9}, //6. p Binomial for forward edges per node
                new Object[]{0, 1, 2, 3, 4, 5, 6}, //7. n Binomial for cross edges per progeny
                new Object[]{0.1, 0.5, 0.9}, //8. p Binomial for cross edges per progeny
                new Object[]{0, 1, 2, 3, 4, 5, 6}, //9. n Binomial for back edges per node
                new Object[]{0.1, 0.5, 0.9}, //10. p Binomial for back edges per node
                new Object[]{0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0}} // 11. Proportion of OR nodes
        );
    }
}
/*
attackgraph.**
computation.**
support.**
 */