package computation;

import attackgraph.*;
import datatypes.Order;
import org.apache.commons.math3.distribution.ConstantRealDistribution;
import org.junit.After;
import org.junit.Test;

import support.TestUtils;
import support.OutputUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GraphTest {

   @Test
   public void NetworkAssetTest() {
      Graph networkGraph = new Graph("LooseGraph");
      AttackStep physicalAccess = new AttackStepMax("physicalAccess", new ConstantRealDistribution(0.0), Order.ENTRYSTEP);
      AttackStep physicalCompromiseWithoutPortSecurity = new AttackStepMax("physicalCompromiseWithoutPortSecurity", new ConstantRealDistribution(0.0), Order.MIDSTEP);
      AttackStep portSecurity_disable = new AttackStepMin("portSecurity_disable", new ConstantRealDistribution(0.0), Order.MIDSTEP);
      AttackStep compromise = new AttackStepMin("compromise", new ConstantRealDistribution(0.0), Order.EXITSTEP);

      AttackStep compromiseAnyOperatingSystem = new AttackStepMin("compromiseAnyOperatingSystem", new ConstantRealDistribution(0.0), Order.ENTRYSTEP);
      AttackStep staticARPTables_disable = new AttackStepMin("staticARPTables_disable", new ConstantRealDistribution(0.0), Order.MIDSTEP);
      AttackStep aRPCachePoisoning = new AttackStepMax("compromiseAnyOperatingSystem", new ConstantRealDistribution(0.0), Order.ENTRYSTEP);

      AttackStep denialOfService = new AttackStepMin("denialOfService", new ConstantRealDistribution(0.0), Order.EXITSTEP);

      networkGraph.addAttackStep(physicalAccess);
      networkGraph.addAttackStep(physicalCompromiseWithoutPortSecurity);
      networkGraph.addAttackStep(portSecurity_disable);
      networkGraph.addAttackStep(compromise);
      networkGraph.addAttackStep(compromiseAnyOperatingSystem);
      networkGraph.addAttackStep(staticARPTables_disable);
      networkGraph.addAttackStep(aRPCachePoisoning);
      networkGraph.addAttackStep(denialOfService);

      physicalAccess.connectToChild(physicalCompromiseWithoutPortSecurity);
      portSecurity_disable.connectToChild(physicalCompromiseWithoutPortSecurity);
      physicalCompromiseWithoutPortSecurity.connectToChild(compromise);
      compromise.connectToChild(denialOfService);

      compromiseAnyOperatingSystem.connectToChild(aRPCachePoisoning);
      staticARPTables_disable.connectToChild(aRPCachePoisoning);
      aRPCachePoisoning.connectToChild(denialOfService);
   }

   @Test
   public void testCreateAndComputeOriginalGraph() {
      // OutputUtils.verboseOn();
      Graph graph = TestUtils.generateRandomGraph(2, 2, 50, 3, 0.3, 3, 0.2, 0.5);
      graph.zeroEntrySteps();
      GraphComputer goc = new GraphComputer(graph); goc.compute();
      OutputUtils.printVerbose(Double.toString(graph.getExitSteps().get(0).getTtc()));
   }

   @Test
   public void testCreateAndComputeNewGraphWithBackCross() {
      Graph graph = TestUtils.generateRandomGraph(2, 2, 100, 3, 0.7, 2, 0.3, 1, 0.15, 2, 0.45, 0.7);
      graph.sample();
      graph.zeroEntrySteps();
      GraphComputer goc = new GraphComputer(graph); goc.compute();
      OutputUtils.plotOn();
      OutputUtils.printVerbose(Double.toString(graph.getExitSteps().get(0).getTtc()));
      OutputUtils.mathematicaPlot(graph,2);
      OutputUtils.plotOff();
      graph.softReset();
      OutputUtils.verboseOn();
      GraphTransformer gt = new GraphTransformer(graph);
      gt.reduce(graph);
      OutputUtils.plotOn();
      OutputUtils.printVerbose(Double.toString(graph.getExitSteps().get(0).getTtc()));
      OutputUtils.mathematicaPlot(graph,2);
   }

   @Test
   public void testClone() {
      Graph original = TestUtils.generateRandomGraph(2, 2, 50, 3, 0.3, 3, 0.2, 0.5);
      original.zeroEntrySteps();
      GraphComputer goc = new GraphComputer(original); goc.compute();
      Graph clone = TestUtils.cloneGraph(original);
      clone.zeroEntrySteps();
      goc = new GraphComputer(clone); goc.compute();
      assert (clone.attackStepsAsSet().size() == original.attackStepsAsSet().size());
      for (int i = 0; i < original.getExitSteps().size(); i++) {
         assert (clone.getExitSteps().get(i).getTtc() == original.getExitSteps().get(i).getTtc());
      }
   }

   @Test
   public void testSpecificClone() {
      // OutputUtils.verboseOn();
      Graph original = TestUtils.generateRandomGraph(1, 1, 19, 2, 0.5, 2, 0.5, 0.5);
      original.zeroEntrySteps();
      Graph clone = TestUtils.cloneGraph(original);
      clone.zeroEntrySteps();
      GraphComputer goc = new GraphComputer(original); goc.compute();
      goc = new GraphComputer(clone); goc.compute();
      assert (clone.attackStepsAsSet().size() == original.attackStepsAsSet().size());
      for (int i = 0; i < original.getExitSteps().size(); i++) {
         assert (clone.getExitSteps().get(i).getTtc() == original.getExitSteps().get(i).getTtc());
      }
   }

   @Test
   public void testOrdinalCompute() {
      OutputUtils.plotOn();
      Graph graph = TestUtils.generateRandomGraph(1, 1, 15, 3, 0.4, 3, 0.2, 0.5);
      graph.sample();
      GraphOrdinalComputer goc = new GraphOrdinalComputer(graph);        goc.ordinalCompute(graph.getEntrySteps().get(0));
      OutputUtils.mathematicaPlot(graph, 2);
   }

   @Test
   public void testMathematicaPlotString() {
      Graph original = TestUtils.generateRandomGraph(1, 1, 19, 2, 0.5, 2, 0.5, 0.5);
      original.sample();
      original.zeroEntrySteps();
      // OutputUtils.verboseOn();
      OutputUtils.mathematicaPlot(original, 1);
   }

   @After
   public void close() {
      OutputUtils.verboseOff();
   }

}
