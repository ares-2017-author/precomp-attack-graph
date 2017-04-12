package support;

import attackgraph.AttackStep;
import attackgraph.Graph;
import computation.GraphComputer;
import computation.GraphOrdinalComputer;
import computation.GraphTransformer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class CorrectnessTest {

    @Before
    public void setup() {
        OutputUtils.verboseOff();
        OutputUtils.plotOff();
    }

    @Test
    public void testMC450Correctness() {
        Graph graph = TestUtils.generateRandomGraph(3, 4, 450, 4, 0.7, 2, 0.3, 1, 0.025, 1, 0.15, 0.65);
        int initSize = graph.attackStepsAsList().size();

        graph.softReset();
        graph.sample();
        Graph graphReduced = TestUtils.cloneGraph(graph);
        GraphComputer goc = new GraphComputer(graph); goc.compute();

        graphReduced.softReset();
        graphReduced.sample();
        GraphTransformer gt = new GraphTransformer(graphReduced);
        OutputUtils.verboseOn();
        gt.reduce(graphReduced);
        OutputUtils.verboseOff();
        goc = new GraphComputer(graphReduced); goc.compute();


        List<AttackStep> unreducedExits = graph.getExitSteps().stream().filter(as -> as.hasBeenPerformed()).collect(Collectors.toList());
        unreducedExits.sort(Comparator.comparingDouble(AttackStep::getTtc));
        //Previously unreachable exit steps should have been removed by now -- NOT TRUE!
        List<AttackStep> reducedExits = graphReduced.getExitSteps().stream().filter(as -> as.hasBeenPerformed()).collect(Collectors.toList());
        reducedExits.sort(Comparator.comparingDouble(AttackStep::getTtc));
        double ttcSumUnreduced = unreducedExits.stream().mapToDouble(ue -> ue.getTtc()).sum();
        double ttcSumReduced = reducedExits.stream().mapToDouble(re -> re.getTtc()).sum();
        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Unreduced TTC Sum: "+ttcSumUnreduced+"; Reduced TTC Sum:"+ttcSumReduced);
        boolean correct = (ttcSumReduced == 0 && ttcSumUnreduced == 0) ||Math.abs(ttcSumUnreduced-ttcSumReduced)/ttcSumUnreduced < 0.025;
        if(!correct) {
            OutputUtils.plotOn();
            OutputUtils.mathematicaPlot(graph,2);
            OutputUtils.mathematicaPlot(graphReduced,2);
        }
        assertTrue(correct);
//        for (int i = 0; i < unreducedExits.size(); i++) {
//            AttackStep unreducedExitStep = unreducedExits.get(i);
//            AttackStep reducedExitStep = reducedExits.get(i);
//            OutputUtils.printVerbose("Unreduced " + unreducedExitStep.getName() + ": " + unreducedExitStep.getTtc());
//            OutputUtils.printVerbose("Reduced " + reducedExitStep.getName() + ": " + reducedExitStep.getTtc());
//            assertTrue((reducedExitStep.getTtc() == 0.0 && unreducedExitStep.getTtc() == 0.0)
//                    || (unreducedExitStep.getTtc() - reducedExitStep.getTtc() > -.3
//                    && unreducedExitStep.getTtc() - reducedExitStep.getTtc() < .3));
//        }

        OutputUtils.printVerbose("unreduced attacksteps: " + initSize);
        OutputUtils.printVerbose("Reduced attacksteps: " + graphReduced.attackStepsAsList().size());
        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph,2);
        OutputUtils.mathematicaPlot(graphReduced,2);
    }

    @Test
    public void testMC1000Correctness() {
        // 501; 0; Infinity; 28.3; 3; 3; 0.0042395215; 8; 0.9; 3; 0.3; 2; 0.1; 2; 0.15; 0.65; 0
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(2, 5, 1000, 4, 0.6, 3, 0.3, 2, .1, 2, .15, 0.3);
        int initSize = graph.attackStepsAsList().size();

        graph.softReset();
        graph.sample();
        GraphComputer goc = new GraphComputer(graph); goc.compute();
        Graph graphReduced = TestUtils.cloneGraph(graph);

        graphReduced.softReset();
        graphReduced.sample();
        GraphTransformer gt = new GraphTransformer(graphReduced);
        gt.reduce(graphReduced);
        goc = new GraphComputer(graphReduced); goc.compute();

        List<AttackStep> unreducedExits = graph.getExitSteps().stream().filter(as -> as.hasBeenPerformed()).collect(Collectors.toList());
        unreducedExits.sort(Comparator.comparingDouble(AttackStep::getTtc));
        //Previously unreachable exit steps should have been removed by now
        List<AttackStep> reducedExits = graphReduced.getExitSteps().stream().filter(as -> as.hasBeenPerformed()).collect(Collectors.toList());
        reducedExits.sort(Comparator.comparingDouble(AttackStep::getTtc));
        double ttcSumUnreduced = unreducedExits.stream().mapToDouble(ue -> ue.getTtc()).sum();
        double ttcSumReduced = reducedExits.stream().mapToDouble(re -> re.getTtc()).sum();
        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Unreduced TTC Sum: "+ttcSumUnreduced+"; Reduced TTC Sum:"+ttcSumReduced);
        boolean correct = (ttcSumReduced == 0 && ttcSumUnreduced == 0) ||Math.abs(ttcSumUnreduced-ttcSumReduced)/ttcSumUnreduced < 0.025;
        if(!correct) {
            OutputUtils.plotOn();
            OutputUtils.mathematicaPlot(graph,2);
            OutputUtils.mathematicaPlot(graphReduced,2);
        }
        assertTrue(correct);
//        for (int i = 0; i < unreducedExits.size(); i++) {
//            AttackStep unreducedExitStep = unreducedExits.get(i);
//            AttackStep reducedExitStep = reducedExits.get(i);
//            OutputUtils.printVerbose("Unreduced " + unreducedExitStep.getName() + ": " + unreducedExitStep.getTtc());
//            OutputUtils.printVerbose("Reduced " + reducedExitStep.getName() + ": " + reducedExitStep.getTtc());
//            assertTrue((reducedExitStep.getTtc() == 0.0 && unreducedExitStep.getTtc() == 0.0)
//                    || (unreducedExitStep.getTtc() - reducedExitStep.getTtc() > -.3
//                    && unreducedExitStep.getTtc() - reducedExitStep.getTtc() < .3));
//        }

        OutputUtils.printVerbose("unreduced attacksteps: " + initSize);
        OutputUtils.printVerbose("Reduced attacksteps: " + graphReduced.attackStepsAsList().size());
        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph,2);
        OutputUtils.mathematicaPlot(graphReduced,2);
    }

    @Test
    public void testMC500Correctness() {
        // 501; 0; Infinity; 28.3; 3; 3; 0.0042395215; 8; 0.9; 3; 0.3; 2; 0.1; 2; 0.15; 0.65; 0
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(3, 3, 500, 4, 0.7, 3, 0.3, 2, .1, 2, .15, 0.8);
        int initSize = graph.attackStepsAsList().size();

        graph.softReset();
        graph.sample();
        Graph graphReduced = TestUtils.cloneGraph(graph);
        GraphComputer goc = new GraphComputer(graph); goc.compute();

        graphReduced.softReset();
        graphReduced.sample();
        GraphTransformer gt = new GraphTransformer(graphReduced);
        gt.reduce(graphReduced);
        goc = new GraphComputer(graphReduced); goc.compute();

        List<AttackStep> unreducedExits = graph.getExitSteps().stream().filter(as -> as.hasBeenPerformed()).collect(Collectors.toList());
        unreducedExits.sort(Comparator.comparingDouble(AttackStep::getTtc));
        //Previously unreachable exit steps should have been removed by now
        List<AttackStep> reducedExits = graphReduced.getExitSteps().stream().filter(as -> as.hasBeenPerformed()).collect(Collectors.toList());
        reducedExits.sort(Comparator.comparingDouble(AttackStep::getTtc));
        double ttcSumUnreduced = unreducedExits.stream().mapToDouble(ue -> ue.getTtc()).sum();
        double ttcSumReduced = reducedExits.stream().mapToDouble(re -> re.getTtc()).sum();
        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Unreduced TTC Sum: "+ttcSumUnreduced+"; Reduced TTC Sum:"+ttcSumReduced);
        boolean correct = (ttcSumReduced == 0 && ttcSumUnreduced == 0) ||Math.abs(ttcSumUnreduced-ttcSumReduced)/ttcSumUnreduced < 0.025;
        if(!correct) {
            OutputUtils.plotOn();
            OutputUtils.mathematicaPlot(graph,2);
            OutputUtils.mathematicaPlot(graphReduced,2);
        }
        assertTrue(correct);
//        for (int i = 0; i < unreducedExits.size(); i++) {
//            AttackStep unreducedExitStep = unreducedExits.get(i);
//            AttackStep reducedExitStep = reducedExits.get(i);
//            OutputUtils.printVerbose("Unreduced " + unreducedExitStep.getName() + ": " + unreducedExitStep.getTtc());
//            OutputUtils.printVerbose("Reduced " + reducedExitStep.getName() + ": " + reducedExitStep.getTtc());
//            assertTrue((reducedExitStep.getTtc() == 0.0 && unreducedExitStep.getTtc() == 0.0)
//                    || (unreducedExitStep.getTtc() - reducedExitStep.getTtc() > -.3
//                    && unreducedExitStep.getTtc() - reducedExitStep.getTtc() < .3));
//        }

        OutputUtils.printVerbose("unreduced attacksteps: " + initSize);
        OutputUtils.printVerbose("Reduced attacksteps: " + graphReduced.attackStepsAsList().size());
        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph,2);
        OutputUtils.mathematicaPlot(graphReduced,2);
    }

    @Test
    public void testMC250Correctness() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(3, 3, 250, 6, 0.3, 3, 0.025, 0.8);
        int initSize = graph.attackStepsAsList().size();

        graph.softReset();
        graph.sample();
        Graph graphReduced = TestUtils.cloneGraph(graph);
        GraphComputer goc = new GraphComputer(graph); goc.compute();

        graphReduced.softReset();
        graphReduced.sample();
        GraphTransformer gt = new GraphTransformer(graphReduced);
        gt.reduce(graphReduced);
        goc = new GraphComputer(graphReduced); goc.compute();

        OutputUtils.verboseOn();
        List<AttackStep> unreducedExits = graph.getExitSteps().stream().filter(as -> as.hasBeenPerformed()).collect(Collectors.toList());
        unreducedExits.sort(Comparator.comparingDouble(AttackStep::getTtc));
        //Previously unreachable exit steps should have been removed by now
        List<AttackStep> reducedExits = graphReduced.getExitSteps().stream().filter(as -> as.hasBeenPerformed()).collect(Collectors.toList());
        reducedExits.sort(Comparator.comparingDouble(AttackStep::getTtc));
        double ttcSumUnreduced = unreducedExits.stream().mapToDouble(ue -> ue.getTtc()).sum();
        double ttcSumReduced = reducedExits.stream().mapToDouble(re -> re.getTtc()).sum();
        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Unreduced TTC Sum: "+ttcSumUnreduced+"; Reduced TTC Sum:"+ttcSumReduced);
        boolean correct = (ttcSumReduced == 0 && ttcSumUnreduced == 0) ||Math.abs(ttcSumUnreduced-ttcSumReduced)/ttcSumUnreduced < 0.025;
        if(!correct) {
            OutputUtils.plotOn();
            OutputUtils.mathematicaPlot(graph,2);
            OutputUtils.mathematicaPlot(graphReduced,2);
        }
        assertTrue(correct);
//        for (int i = 0; i < unreducedExits.size(); i++) {
//            AttackStep unreducedExitStep = unreducedExits.get(i);
//            AttackStep reducedExitStep = reducedExits.get(i);
//            OutputUtils.printVerbose("Unreduced " + unreducedExitStep.getName() + ": " + unreducedExitStep.getTtc());
//            OutputUtils.printVerbose("Reduced " + reducedExitStep.getName() + ": " + reducedExitStep.getTtc());
//            assertTrue((reducedExitStep.getTtc() == 0.0 && unreducedExitStep.getTtc() == 0.0)
//                    || (unreducedExitStep.getTtc() - reducedExitStep.getTtc() > -.3
//                    && unreducedExitStep.getTtc() - reducedExitStep.getTtc() < .3));
//        }

        OutputUtils.printVerbose("unreduced attacksteps: " + initSize);
        OutputUtils.printVerbose("Reduced attacksteps: " + graphReduced.attackStepsAsList().size());
        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph,2);
        OutputUtils.mathematicaPlot(graphReduced,2);
    }

    @Test
    public void testMC150Correctness() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(3, 3, 150, 6, 0.3, 3, 0.025, 0.8);
        int initSize = graph.attackStepsAsList().size();

        graph.softReset();
        graph.sample();
        Graph graphReduced = TestUtils.cloneGraph(graph);
        GraphComputer goc = new GraphComputer(graph); goc.compute();

        graphReduced.softReset();
        graphReduced.sample();
        GraphTransformer gt = new GraphTransformer(graphReduced);
        OutputUtils.verboseOn();
        gt.reduce(graphReduced);
        OutputUtils.verboseOff();
        goc = new GraphComputer(graphReduced); goc.compute();

        OutputUtils.verboseOn();
        List<AttackStep> unreducedExits = graph.getExitSteps().stream().filter(as -> as.hasBeenPerformed()).collect(Collectors.toList());
        unreducedExits.sort(Comparator.comparingDouble(AttackStep::getTtc));
        //Previously unreachable exit steps should have been removed by now
        List<AttackStep> reducedExits = graphReduced.getExitSteps().stream().filter(as -> as.hasBeenPerformed()).collect(Collectors.toList());
        reducedExits.sort(Comparator.comparingDouble(AttackStep::getTtc));
        double ttcSumUnreduced = unreducedExits.stream().mapToDouble(ue -> ue.getTtc()).sum();
        double ttcSumReduced = reducedExits.stream().mapToDouble(re -> re.getTtc()).sum();
        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Unreduced TTC Sum: "+ttcSumUnreduced+"; Reduced TTC Sum:"+ttcSumReduced);
        boolean correct = (ttcSumReduced == 0 && ttcSumUnreduced == 0) ||Math.abs(ttcSumUnreduced-ttcSumReduced)/ttcSumUnreduced < 0.02;
        if(!correct) {
            OutputUtils.plotOn();
            OutputUtils.mathematicaPlot(graph,2);
            OutputUtils.mathematicaPlot(graphReduced,2);
        }
        assertTrue(correct);
//        for (int i = 0; i < unreducedExits.size(); i++) {
//            AttackStep unreducedExitStep = unreducedExits.get(i);
//            AttackStep reducedExitStep = reducedExits.get(i);
//            OutputUtils.printVerbose("Unreduced " + unreducedExitStep.getName() + ": " + unreducedExitStep.getTtc());
//            OutputUtils.printVerbose("Reduced " + reducedExitStep.getName() + ": " + reducedExitStep.getTtc());
//            assertTrue((reducedExitStep.getTtc() == 0.0 && unreducedExitStep.getTtc() == 0.0)
//                    || (unreducedExitStep.getTtc() - reducedExitStep.getTtc() > -.3
//                    && unreducedExitStep.getTtc() - reducedExitStep.getTtc() < .3));
//        }

        OutputUtils.printVerbose("unreduced attacksteps: " + initSize);
        OutputUtils.printVerbose("Reduced attacksteps: " + graphReduced.attackStepsAsList().size());
        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph,2);
        OutputUtils.mathematicaPlot(graphReduced,2);
    }

    @Test
    public void testMC100Correctness() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(3, 3, 100, 2, 0.3, 3, 0.025, 0.6);
        int initSize = graph.attackStepsAsList().size();

        graph.softReset();
        graph.sample();
        Graph graphReduced = TestUtils.cloneGraph(graph);
        GraphComputer goc = new GraphComputer(graph); goc.compute();

        graphReduced.softReset();
        graphReduced.sample();
        GraphTransformer gt = new GraphTransformer(graphReduced);
        gt.reduce(graphReduced);
        goc = new GraphComputer(graphReduced); goc.compute();

        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph,2);
        OutputUtils.mathematicaPlot(graphReduced,2);
        List<AttackStep> unreducedExits = graph.getExitSteps().stream().filter(as -> as.hasBeenPerformed()).collect(Collectors.toList());
        unreducedExits.sort(Comparator.comparingDouble(AttackStep::getTtc));
        List<AttackStep> reducedExits = graphReduced.getExitSteps().stream().filter(as -> as.hasBeenPerformed()).collect(Collectors.toList());
        reducedExits.sort(Comparator.comparingDouble(AttackStep::getTtc));
        double ttcSumUnreduced = unreducedExits.stream().mapToDouble(ue -> ue.getTtc()).sum();
        double ttcSumReduced = reducedExits.stream().mapToDouble(re -> re.getTtc()).sum();
        OutputUtils.verboseOn();
        OutputUtils.printVerbose("Unreduced TTC Sum: "+ttcSumUnreduced+"; Reduced TTC Sum:"+ttcSumReduced);
        boolean correct = (ttcSumReduced == 0 && ttcSumUnreduced == 0) ||Math.abs(ttcSumUnreduced-ttcSumReduced)/ttcSumUnreduced < 0.025;
        if(!correct) {
            OutputUtils.plotOn();
            OutputUtils.mathematicaPlot(graph,2);
            OutputUtils.mathematicaPlot(graphReduced,2);
        }
        assertTrue(correct);
//        for (int i = 0; i < unreducedExits.size(); i++) {
//            AttackStep unreducedExitStep = unreducedExits.get(i);
//            AttackStep reducedExitStep = reducedExits.get(i);
//            OutputUtils.printVerbose("Unreduced " + unreducedExitStep.getName() + ": " + unreducedExitStep.getTtc());
//            OutputUtils.printVerbose("Reduced " + reducedExitStep.getName() + ": " + reducedExitStep.getTtc());
//            assertTrue((reducedExitStep.getTtc() == 0.0 && unreducedExitStep.getTtc() == 0.0)
//                    || (unreducedExitStep.getTtc() - reducedExitStep.getTtc() > -.3
//                        && unreducedExitStep.getTtc() - reducedExitStep.getTtc() < .3)
//            );
//        }

        OutputUtils.printVerbose("unreduced attacksteps: " + initSize);
        OutputUtils.printVerbose("Reduced attacksteps: " + graphReduced.attackStepsAsList().size());
    }

}
