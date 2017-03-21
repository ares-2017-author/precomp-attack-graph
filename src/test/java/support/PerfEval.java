package support;

import com.github.jesg.dither.Dither;
import components.Graph;

import java.util.concurrent.TimeUnit;

/**
 * Created by avernotte on 2/24/17.
 */
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

    public static void main( String[] args) {

        CVSFileWriter cvsFileWriter = new CVSFileWriter("graph-experiments-3ways.csv");
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
            tm = TimeWatch.start();
            graph.reduce();
            double reduce_time = tm.time(TimeUnit.MILLISECONDS);
            cvsFileWriter.newRecord((int)testData[0], (int)testData[1], real_size,
                    (int)testData[3], (double)testData[4], (int)testData[5], (double)testData[6], (double)testData[7],
                    graph.size(),reduce_time);
            cpt++;
        }
        cvsFileWriter.close();
    }

    public static void main3(String args[]) {
        Graph graph = TestUtils.generateRandomGraph(12, 28, 100,
                17, 0.35, 36, 0.25, 0.6);
        graph.sample();
        System.out.println("unreduced steps: "+graph.size());
        graph.reduce();
        System.out.println("reduced steps: "+graph.size());
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
                new Object[] { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0}, // 4. Binomial probability of children for each node
                 new Object[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18}, // 5. Max Number of parents
                new Object[] { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0}, // 6. Binomial probability of parent for each node
                new Object[] { 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0 }} // 7. Proportion of OR nodes
        );
    }
}
/*
attackGraph.**
components.**
support.**
 */