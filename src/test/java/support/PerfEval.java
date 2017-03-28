package support;

import attackgraph.Graph;
import com.github.jesg.dither.Dither;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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

    public static void main3(String[] args) {

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

    /* Graph size experiments */
    public static void main4(String args[]) throws InterruptedException {
        CSVFileWriter csvFileWriter = new CSVFileWriter("graph-experiments-indiv-size.csv");
        int nEntrySteps = 4;
        int nExitSteps = 5;
        int maxAttackSteps = 500;
        int nBinomialChildren = 3;
        double pBinomialChildren = 0.6;
        int nBinomialForwardEdges = 1;
        double pBinomialForwardEdges = 0.25;
        int nBinomialCrossEdges = 1;
        double pBinomialCrossEdges = 0.15;
        int nBinomialBackEdges = 1;
        double pBinomialBackEdges = 0.1;
        double pMinAttackSteps = 0.65;
        int iterations = 50;
        for (int maxSteps = 100; maxSteps < 10001; maxSteps = maxSteps + 100) {
            performMultipleReductions(csvFileWriter, nEntrySteps, nExitSteps, maxSteps, nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges, nBinomialCrossEdges, pBinomialCrossEdges, nBinomialBackEdges, pBinomialBackEdges, pMinAttackSteps, iterations);
        }
    }

    /* Individual experiments */
    public static void main(String args[]) throws InterruptedException {
        CSVFileWriter csvFileWriter = new CSVFileWriter("graph-experiments-indiv-nosize.csv");
//        synchronized (args) {
//            args.wait(11000);
//        }
        int nEntrySteps = 3;
        int nExitSteps = 3;
        int maxAttackSteps = 500;
        int nBinomialChildren = 2;
        double pBinomialChildren = 0.7;
        int nBinomialForwardEdges = 1;
        double pBinomialForwardEdges = 0.2;
        int nBinomialCrossEdges = 1;
        double pBinomialCrossEdges = 0.15;
        int nBinomialBackEdges = 1;
        double pBinomialBackEdges = 0.1;
        double pMinAttackSteps = 0.65;
        int iterations = 10;

        /* **** ENTRY STEPS **** */
        for (int nEntry = 1; nEntry < 100; nEntry = nEntry + 2) {
            performMultipleReductions(csvFileWriter, nEntry, nExitSteps, maxAttackSteps, nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges, nBinomialCrossEdges, pBinomialCrossEdges, nBinomialBackEdges, pBinomialBackEdges, pMinAttackSteps, iterations);
        }

        /* **** EXIT STEPS **** */
        for (int nExit = 1; nExit < 100; nExit = nExit + 2) {
            performMultipleReductions(csvFileWriter, nEntrySteps, nExit, maxAttackSteps, nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges, nBinomialCrossEdges, pBinomialCrossEdges, nBinomialBackEdges, pBinomialBackEdges, pMinAttackSteps, iterations);
        }

        /* **** Graph Size **** */
   /*     for (int maxSteps = 100; maxSteps < 10001; maxSteps = maxSteps + 100) {
            performMultipleReductions(csvFileWriter, nEntrySteps, nExitSteps, maxSteps, nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges, nBinomialCrossEdges, pBinomialCrossEdges, nBinomialBackEdges, pBinomialBackEdges, pMinAttackSteps, iterations);
        }*/

        /* **** DirectChildren **** */
        for (double probaChild = 0.1; probaChild <= 1.0; probaChild = probaChild + .4) {
            for (int nbChildren = 1; nbChildren < 10; nbChildren++) {
                performMultipleReductions(csvFileWriter, nEntrySteps, nExitSteps, maxAttackSteps, nbChildren, probaChild, nBinomialForwardEdges, pBinomialForwardEdges, nBinomialCrossEdges, pBinomialCrossEdges, nBinomialBackEdges, pBinomialBackEdges, pMinAttackSteps, iterations);
            }
        }

        /* **** ForwardEdges **** */
        for (double pBinomialFW = 0.1; pBinomialFW <= 1.0; pBinomialFW = pBinomialFW + .4) {
            for (int nBinomialFW = 1; nBinomialFW < 8; nBinomialFW++) {
                performMultipleReductions(csvFileWriter, nEntrySteps, nExitSteps, maxAttackSteps, nBinomialChildren, pBinomialChildren, nBinomialFW, pBinomialFW, nBinomialCrossEdges, pBinomialCrossEdges, nBinomialBackEdges, pBinomialBackEdges, pMinAttackSteps, iterations);
            }
        }

        /* **** BackEdges **** */
        for (double pBinomialBW = 0.1; pBinomialBW <= 1.0; pBinomialBW = pBinomialBW + .4) {
            for (int nBinomialBW = 1; nBinomialBW < 8; nBinomialBW++) {
                performMultipleReductions(csvFileWriter, nEntrySteps, nExitSteps, maxAttackSteps, nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges, nBinomialCrossEdges, pBinomialCrossEdges, nBinomialBW, pBinomialBW, pMinAttackSteps, iterations);
            }
        }

        /* **** CrossEdges **** */
        for (double pBinomialCW = 0.1; pBinomialCW <= 1.0; pBinomialCW = pBinomialCW + .4) {
            for (int nBinomialCW = 1; nBinomialCW < 8; nBinomialCW++) {
                performMultipleReductions(csvFileWriter, nEntrySteps, nExitSteps, maxAttackSteps, nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges, nBinomialCW, pBinomialCW, nBinomialBackEdges, pBinomialBackEdges, pMinAttackSteps, iterations);
            }
        }

        /* **** OR/AND ratio **** */
        for (double orAndRatio = 0.0; orAndRatio <= 1.01; orAndRatio = orAndRatio + .025) {
            performMultipleReductions(csvFileWriter, nEntrySteps, nExitSteps, maxAttackSteps, nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges, nBinomialCrossEdges, pBinomialCrossEdges, nBinomialBackEdges, pBinomialBackEdges, orAndRatio, iterations);

        }

    }

    public static void main6(String args[]) {
        CSVFileWriter CSVFileWriter = new CSVFileWriter("graph-experiments-indiv-cross.csv");
        TimeWatch tm;
        int nEntrySteps = 4;
        int nExitSteps = 5;
        int maxAttackSteps = 500;
        int nBinomialChildren = 4;
        double pBinomialChildren = 0.7;
        int nBinomialForwardEdges = 1;
        double pBinomialForwardEdges = 0.3;
        int nBinomialBackEdges = 1;
        double pBinomialBackEdges = 0.1;
        double pMinAttackSteps = 0.65;

        for (double pBinomialCW = 0.1; pBinomialCW <= 1.0; pBinomialCW = pBinomialCW + .4) {
            for (int nBinomialCW = 1; nBinomialCW < 8; nBinomialCW++) {
                Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExitSteps, maxAttackSteps,
                        nBinomialChildren, pBinomialChildren,nBinomialForwardEdges,pBinomialForwardEdges,
                        nBinomialCW, pBinomialCW, nBinomialBackEdges,pBinomialBackEdges, pMinAttackSteps);
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


    public static void main5(String args[]) throws InterruptedException {
        //  505; 470; 1.0744681; 1700.0; 3; 3; 0.004640107; 2; 0.7; 1; 0.2; 1; 0.1; 2; 0.5; 0.65
        Graph graph = TestUtils.generateRandomGraph(3,3,500,2,.7,1,.2,1,.1,6,.5,.65);
        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph,1);
        OutputUtils.plotOff();
        graph.reduce();
        System.out.println("graph size: "+graph.size());
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
                new Object[]{0, 1, 2, 3, 4, 5, 6, 7, 8}, //5. n Binomial for forward edges per node
                new Object[]{0.1, 0.5, 0.9}, //6. p Binomial for forward edges per node
                new Object[]{0, 1, 2, 3, 4, 5, 6, 7, 8}, //7. n Binomial for cross edges per progeny
                new Object[]{0.1, 0.5, 0.9}, //8. p Binomial for cross edges per progeny
                new Object[]{0, 1, 2, 3, 4, 5, 6, 7, 8}, //9. n Binomial for back edges per node
                new Object[]{0.1, 0.5, 0.9}, //10. p Binomial for back edges per node
                new Object[]{0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0}} // 11. Proportion of OR nodes
        );
    }


    private static void performMultipleReductions(CSVFileWriter CSVFileWriter, int nEntrySteps, int nExitSteps, int maxAttackSteps, int nBinomialChildren, double pBinomialChildren, int nBinomialForwardEdges, double pBinomialForwardEdges, int nBinomialCrossEdges, double pBinomialCrossEdges, int nBinomialBackEdges, double pBinomialBackEdges, double pMinAttackSteps, int iterations) {
        int real_size = 0;
        float reduced_size = 0;
        float min_children = 0;
        float max_children = 0;
        float mean_children = 0;
        float mean_tree = 0;
        float mean_forward = 0;
        float mean_back = 0;
        float mean_cross = 0;
        float graph_density = 0;
        double reduce_time = 0;
        float reduction_ratio = 0;
        TimeWatch tm;
        for (int i = 0; i < iterations; i++) {
            Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExitSteps, maxAttackSteps,
                    nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges,
                    nBinomialCrossEdges, pBinomialCrossEdges, nBinomialBackEdges, pBinomialBackEdges, pMinAttackSteps);
            graph.sample();
            real_size = graph.size();
            min_children += graph.minChildrenNbr();
            max_children += graph.maxChildrenNbr();
            mean_children += graph.meanChildrenNbr();
            mean_tree += graph.getMeanTreeEdges();
            mean_forward += graph.getMeanForwardEdges();
            mean_back += graph.getMeanBackEdges();
            mean_cross += graph.getMeanCrossEdges();
            graph_density += graph.getGraphDensity();
            tm = TimeWatch.start();
            graph.reduce();
            reduce_time += tm.time(TimeUnit.MILLISECONDS);
            reduced_size += graph.size();
            reduction_ratio += ((float) real_size/ graph.size());
        }
        min_children = min_children / iterations;
        max_children = max_children / iterations;
        mean_children = mean_children / iterations;
        mean_tree = mean_tree / iterations;
        mean_forward = mean_forward / iterations;
        mean_back = mean_back / iterations;
        mean_cross = mean_children / iterations;
        graph_density = graph_density / iterations;
        reduce_time = reduce_time / iterations;
        reduced_size = reduced_size / iterations;
        reduction_ratio = reduction_ratio / iterations;
        CSVFileWriter.printNewRecord(nEntrySteps, nExitSteps, real_size, reduction_ratio, nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges,
                nBinomialBackEdges,pBinomialBackEdges,nBinomialCrossEdges,pBinomialCrossEdges,
                pMinAttackSteps, graph_density, (int)reduced_size, reduce_time,(int)min_children, (int)max_children, mean_children, mean_tree,
                mean_forward, mean_back, mean_cross);
    }


    private static void performMultipleReductionsParallel(CSVFileWriter CSVFileWriter, int nEntrySteps, int nExitSteps, int maxAttackSteps, int nBinomialChildren, double pBinomialChildren, int nBinomialForwardEdges, double pBinomialForwardEdges, int nBinomialCrossEdges, double pBinomialCrossEdges, int nBinomialBackEdges, double pBinomialBackEdges, double pMinAttackSteps, int iterations) {
        int real_size = 0;
        float reduced_size = 0;
        float min_children = 0;
        float max_children = 0;
        float mean_children = 0;
        float mean_tree = 0;
        float mean_forward = 0;
        float mean_back = 0;
        float mean_cross = 0;
        float graph_density = 0;
        double reduce_time = 0;
        float reduction_ratio = 0;
        List<List<Object>> data = new LinkedList<>();
        List<Integer> iter_values = ContiguousSet.create(Range.closed(1, iterations), DiscreteDomain.integers()).asList();
        iter_values.parallelStream().forEach(i -> {
            Graph graph = TestUtils.generateRandomGraph(nEntrySteps, nExitSteps, maxAttackSteps,
                    nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges,
                    nBinomialCrossEdges, pBinomialCrossEdges, nBinomialBackEdges, pBinomialBackEdges, pMinAttackSteps);
            graph.sample();
            int realSize = graph.size();
            System.out.println("init size: "+realSize);
            List<Object> instance_data = new LinkedList<>();
            instance_data.add(graph.minChildrenNbr()); // 0. min_children
            instance_data.add(graph.maxChildrenNbr()); // 1. max children
            instance_data.add(graph.meanChildrenNbr()); // 2 mean children
            instance_data.add(graph.getMeanTreeEdges()); // 3 mean tree
            instance_data.add(graph.getMeanForwardEdges()); // 4 mean forward
            instance_data.add(graph.getMeanBackEdges()); // 5 mean back
            instance_data.add(graph.getMeanCrossEdges()); // 6 mean cross
            instance_data.add(graph.getGraphDensity()); // 7 density
            TimeWatch tm = TimeWatch.start();
            graph.reduce();
            instance_data.add(tm.time(TimeUnit.MILLISECONDS)); // 8. reduce_time
            instance_data.add(graph.size()); // 9. reduced_size
            instance_data.add(((float) realSize / graph.size())); //10. reduction_ratio
            instance_data.add(realSize); // 11. size before reduction
            data.add(instance_data);
        });
        for(List<Object> inst : data) {
            min_children += (int)inst.get(0);
            max_children += (int)inst.get(1);
            mean_children += (float)inst.get(2);
            mean_tree += (float)inst.get(3);
            mean_forward += (float)inst.get(4);
            mean_back += (float)inst.get(5);
            mean_cross += (float)inst.get(6);
            graph_density += (float)inst.get(7);
            reduce_time += (long)inst.get(8);
            reduced_size += (int)inst.get(9);
            reduction_ratio += (float)inst.get(10);
        }
        real_size = (int)data.get(0).get(11);
        System.out.println("reduction ratio: "+reduction_ratio);
        min_children = min_children / iterations;
        max_children = max_children / iterations;
        mean_children = mean_children / iterations;
        mean_tree = mean_tree / iterations;
        mean_forward = mean_forward / iterations;
        mean_back = mean_back / iterations;
        mean_cross = mean_children / iterations;
        graph_density = graph_density / iterations;
        reduce_time = reduce_time / iterations;
        reduced_size = reduced_size / iterations;
        reduction_ratio = reduction_ratio / iterations;
        CSVFileWriter.printNewRecord(nEntrySteps, nExitSteps, real_size, reduction_ratio, nBinomialChildren, pBinomialChildren, nBinomialForwardEdges, pBinomialForwardEdges,
                nBinomialBackEdges,pBinomialBackEdges,nBinomialCrossEdges,pBinomialCrossEdges,
                pMinAttackSteps, graph_density, (int)reduced_size, reduce_time,(int)min_children, (int)max_children, mean_children, mean_tree,
                mean_forward, mean_back, mean_cross);
    }

}
/*
attackgraph.**
computation.**
support.**
 */
