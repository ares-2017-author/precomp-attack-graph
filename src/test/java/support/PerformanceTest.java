package support;

import attackGraph.AttackStep;
import components.Graph;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import support.OutputUtils;
import support.TestUtils;
import support.TimeWatch;

import java.io.IOException;
import java.lang.*;
import java.lang.System;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class PerformanceTest {

    @Before
    public void setup() {
        OutputUtils.verboseOff();
        OutputUtils.plotOff();
    }

    @Test
    public void testMC80() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(2, 2, 80, 3, 0.3, 3, 0.2, 0.5);
     //   Graph reducedGraph = Graph.class.c graph.clone();
        TimeWatch tm = TimeWatch.start();

        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);

        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    @Test
    public void testMC85() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(2, 2, 85, 3, 0.3, 3, 0.2, 0.5);
        //   Graph reducedGraph = Graph.class.c graph.clone();
        TimeWatch tm = TimeWatch.start();

        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);

        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    @Test
    public void testMC90() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(2, 2, 90, 3, 0.3, 3, 0.2, 0.5);
        //   Graph reducedGraph = Graph.class.c graph.clone();
        TimeWatch tm = TimeWatch.start();

        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);

        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    @Test
    public void testMC90b() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(2, 2, 90, 3, 0.3, 3, 0.2, 0.7);
        //   Graph reducedGraph = Graph.class.c graph.clone();
        TimeWatch tm = TimeWatch.start();

        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);

        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    /** This is very similar **/
    @Test
    public void testMC100b() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(2, 2, 100, 3, 0.3, 3, 0.2, 0.7);
        //   Graph reducedGraph = Graph.class.c graph.clone();
        TimeWatch tm = TimeWatch.start();

        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);

        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    @Test
    public void testMC200b() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(2, 2, 200, 3, 0.3, 3, 0.2, 0.7);
        //   Graph reducedGraph = Graph.class.c graph.clone();
        TimeWatch tm = TimeWatch.start();

        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);

        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    @Test
    public void testMC250b() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(2, 2, 250, 3, 0.3, 3, 0.2, 0.7);
        //   Graph reducedGraph = Graph.class.c graph.clone();
        TimeWatch tm = TimeWatch.start();

        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);

        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    @Test
    public void testMC300b() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(2, 2, 300, 3, 0.3, 3, 0.2, 0.7);
        //   Graph reducedGraph = Graph.class.c graph.clone();
        TimeWatch tm = TimeWatch.start();

        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);

        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    @Ignore
    @Test
    public void testMCComposition1() {
        // OutputUtils.verboseOn();

        Graph superGraph = new Graph("superGraph");

        Graph graph = TestUtils.generateRandomGraph(2, 2, 30, 3, 0.3, 3, 0.2, 0.7);

        List<AttackStep> entrySteps = graph.getEntrySteps();
        List<AttackStep> exitSteps = graph.getExitSteps();

  //      for(int i = 0)


        //   Graph reducedGraph = Graph.class.c graph.clone();
        TimeWatch tm = TimeWatch.start();

        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);

        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    @Ignore
    @Test
    public void testMC600b() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(2, 2, 600, 3, 0.3, 3, 0.2, 0.7);
        //   Graph reducedGraph = Graph.class.c graph.clone();
        TimeWatch tm = TimeWatch.start();

        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);

        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    @Ignore
    @Test
    public void testMC800b() throws InterruptedException {
        OutputUtils.verboseOff();
        Graph graph = TestUtils.generateRandomGraph(2, 2, 800, 3, 0.3, 3, 0.2, 0.7);
        //   Graph reducedGraph = Graph.class.c graph.clone();
        TimeWatch tm = TimeWatch.start();
        int init_size = graph.attackStepsAsList().size();
        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);


        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Size unreduced: " + init_size + " steps.");
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Size reduced: " + graph.attackStepsAsList().size() + " steps.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    @Ignore
    @Test
    public void testMC1200b() throws InterruptedException {
        OutputUtils.verboseOff();
        Graph graph = TestUtils.generateRandomGraph(2, 3, 1200, 3, 0.3, 3, 0.2, 0.7);
        //   Graph reducedGraph = Graph.class.c graph.clone();
        TimeWatch tm = TimeWatch.start();
        int init_size = graph.attackStepsAsList().size();
        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);


        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Size unreduced: " + init_size + " steps.");
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Size reduced: " + graph.attackStepsAsList().size() + " steps.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    @Ignore
    @Test
    public void testMC1600b() throws InterruptedException {
        OutputUtils.verboseOff();
        Graph graph = TestUtils.generateRandomGraph(2, 3, 1600, 3, 0.3, 3, 0.2, 0.7);
        //   Graph reducedGraph = Graph.class.c graph.clone();
        TimeWatch tm = TimeWatch.start();
        int init_size = graph.attackStepsAsList().size();
        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);


        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Size unreduced: " + init_size + " steps.");
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Size reduced: " + graph.attackStepsAsList().size() + " steps.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    @Ignore
    @Test
    public void testMC2500b() throws InterruptedException {
        OutputUtils.verboseOff();
        Graph graph = TestUtils.generateRandomGraph(2, 3, 2500, 3, 0.3, 3, 0.2, 0.7);
        //   Graph reducedGraph = Graph.class.c graph.clone();

        int init_size = graph.attackStepsAsList().size();
        int n = 500;
        TimeWatch tm = TimeWatch.start();
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);

        tm = TimeWatch.start();
        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);

        tm = TimeWatch.start();
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Size unreduced: " + init_size + " steps.");
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Size reduced: " + graph.attackStepsAsList().size() + " steps.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    /** Not much of a change now with an increased number of min nodes **/
    @Test
    public void testMC100c() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(2, 2, 100, 3, 0.3, 3, 0.2, 0.8);
        //   Graph reducedGraph = Graph.class.c graph.clone();
        TimeWatch tm = TimeWatch.start();

        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);

        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    @Test
    public void testMC100d() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(3, 3, 100, 3, 0.3, 3, 0.2, 0.8);
        //   Graph reducedGraph = Graph.class.c graph.clone();
        TimeWatch tm = TimeWatch.start();

        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);

        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    /** the more entry and exit nodes, the longer the computation? Variations are not large enough **/
    @Test
    public void testMC100e() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(5, 5, 100, 3, 0.3, 3, 0.2, 0.8);
        //   Graph reducedGraph = Graph.class.c graph.clone();
        TimeWatch tm = TimeWatch.start();

        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);

        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    /** Still the same threshold with an increased number of maxOldParents **/
    @Test
    public void testMC100f() {

        OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(3, 3, 100, 3, 0.3, 6, 0.2, 0.8);
        //   Graph reducedGraph = Graph.class.c graph.clone();
        TimeWatch tm = TimeWatch.start();

        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);

        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    /** Wow wow, what happened here? **/
    @Test
    public void testMC100g() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(3, 3, 100, 6, 0.3, 3, 0.2, 0.8);
        //   Graph reducedGraph = Graph.class.c graph.clone();
        TimeWatch tm = TimeWatch.start();

        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph,2);
        OutputUtils.plotOff();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);

        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }

    /** I guess it makes sense? the denser the graph, the better the ordinal reduction **/
    @Test
    public void testMC150g() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(3, 3, 150, 6, 0.3, 3, 0.2, 0.8);
        int initSize = graph.attackStepsAsList().size();
        TimeWatch tm = TimeWatch.start();

        int n = 500;
        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_unreduced = tm.time(TimeUnit.MILLISECONDS);


        graph.reduce();
        long reduction_time = tm.time(TimeUnit.MILLISECONDS);

        for (int i = 0; i <= n; i++) {
            graph.softReset();
            graph.sample();
            graph.compute();
        }
        long computation_time_reduced = tm.time(TimeUnit.MILLISECONDS);

        OutputUtils.verboseOn();
        OutputUtils.printVerbose("unreduced attacksteps: " + initSize);
        OutputUtils.printVerbose("Computation time unreduced: " + computation_time_unreduced + " ms.");
        OutputUtils.printVerbose("Reduced attacksteps: " + graph.attackStepsAsList().size());
        OutputUtils.printVerbose("Reduction time: " + reduction_time + " ms.");
        OutputUtils.printVerbose("Computation time once reduced: " + computation_time_reduced + " ms.");
    }


    @Test
    public void testMC450Correctness() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(3, 4, 450, 6, 0.4, 3, 0.3, 0.65);
        int initSize = graph.attackStepsAsList().size();

        graph.softReset();
        graph.sample();
        Graph graphReduced = TestUtils.cloneGraph(graph);
        graph.compute();

        graphReduced.softReset();
        graphReduced.sample();
        graphReduced.reduce();
        graphReduced.compute();

        OutputUtils.verboseOn();
        List<AttackStep> unreducedExits = graph.getExitSteps();
        unreducedExits.stream().sorted((e1,e2) -> Double.compare(e1.getTtc(),e2.getTtc()));
        List<AttackStep> reducedExits = graphReduced.getExitSteps();
        reducedExits.stream().sorted((e1,e2) -> Double.compare(e1.getTtc(),e2.getTtc()));
        for (int i = 0; i < unreducedExits.size(); i++) {
            AttackStep unreducedExitStep = unreducedExits.get(i);
            AttackStep reducedExitStep = reducedExits.get(i);
            OutputUtils.printVerbose("Unreduced " + unreducedExitStep.getName() + ": " + unreducedExitStep.getTtc());
            OutputUtils.printVerbose("Reduced " + reducedExitStep.getName() + ": " + reducedExitStep.getTtc());
//            assertTrue(unreducedExitStep.getTtc() == reducedExitStep.getTtc());
        }

        OutputUtils.printVerbose("unreduced attacksteps: " + initSize);
        OutputUtils.printVerbose("Reduced attacksteps: " + graphReduced.attackStepsAsList().size());
        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph,2);
        OutputUtils.mathematicaPlot(graphReduced,2);
    }

    @Test
    public void testMC150Correctness() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(3, 3, 150, 6, 0.3, 3, 0.2, 0.8);
        int initSize = graph.attackStepsAsList().size();

        graph.softReset();
        graph.sample();
        Graph graphReduced = TestUtils.cloneGraph(graph);
        graph.compute();

        graphReduced.softReset();
        graphReduced.sample();
        graphReduced.reduce();
        graphReduced.compute();

        OutputUtils.verboseOn();
        List<AttackStep> unreducedExits = graph.getExitSteps();
        unreducedExits.stream().sorted((e1,e2) -> Double.compare(e1.getTtc(),e2.getTtc()));
        List<AttackStep> reducedExits = graphReduced.getExitSteps();
        reducedExits.stream().sorted((e1,e2) -> Double.compare(e1.getTtc(),e2.getTtc()));
        for (int i = 0; i < unreducedExits.size(); i++) {
            AttackStep unreducedExitStep = unreducedExits.get(i);
            AttackStep reducedExitStep = reducedExits.get(i);
            OutputUtils.printVerbose("Unreduced " + unreducedExitStep.getName() + ": " + unreducedExitStep.getTtc());
            OutputUtils.printVerbose("Reduced " + reducedExitStep.getName() + ": " + reducedExitStep.getTtc());
//            assertTrue(unreducedExitStep.getTtc() == reducedExitStep.getTtc());
        }

        OutputUtils.printVerbose("unreduced attacksteps: " + initSize);
        OutputUtils.printVerbose("Reduced attacksteps: " + graphReduced.attackStepsAsList().size());
        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph,2);
        OutputUtils.mathematicaPlot(graphReduced,2);
    }

    @Test
    public void testMC100Correctness() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(2, 2, 100, 2, 0.3, 3, 0.2, 0.6);
        int initSize = graph.attackStepsAsList().size();

        graph.softReset();
        graph.sample();
        Graph graphReduced = TestUtils.cloneGraph(graph);
        graph.compute();

        graphReduced.softReset();
        graphReduced.sample();
        graphReduced.reduce();
        graphReduced.compute();

        OutputUtils.verboseOn();
        List<AttackStep> unreducedExits = graph.getExitSteps();
        unreducedExits.stream().sorted((e1,e2) -> Double.compare(e1.getTtc(),e2.getTtc()));
        List<AttackStep> reducedExits = graphReduced.getExitSteps();
        reducedExits.stream().sorted((e1,e2) -> Double.compare(e1.getTtc(),e2.getTtc()));
        for (int i = 0; i < unreducedExits.size(); i++) {
            AttackStep unreducedExitStep = unreducedExits.get(i);
            AttackStep reducedExitStep = reducedExits.get(i);
            OutputUtils.printVerbose("Unreduced " + unreducedExitStep.getName() + ": " + unreducedExitStep.getTtc());
            OutputUtils.printVerbose("Reduced " + reducedExitStep.getName() + ": " + reducedExitStep.getTtc());
//            assertTrue(unreducedExitStep.getTtc() == reducedExitStep.getTtc());
        }

        OutputUtils.printVerbose("unreduced attacksteps: " + initSize);
        OutputUtils.printVerbose("Reduced attacksteps: " + graphReduced.attackStepsAsList().size());
        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph,2);
        OutputUtils.mathematicaPlot(graphReduced,2);
    }

}
