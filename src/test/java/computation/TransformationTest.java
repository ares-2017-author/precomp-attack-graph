package computation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import attackgraph.*;
import com.google.common.collect.Sets;
import datatypes.Order;
import datatypes.OrdinalTtcValue;
import org.apache.commons.math3.distribution.ConstantRealDistribution;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import support.OutputUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GraphTransformer.class)
public class TransformationTest {

   @Test
   public void removeRedundantEdgesExtractFromService() throws Exception{
      OutputUtils.verboseOn();
      Graph serviceGraph = new Graph("service");
      AttackStep access = new AttackStepMin("access", new ConstantRealDistribution(10), Order.ENTRYSTEP);
      AttackStep bypassAC = new AttackStepMax("bypassAccessControl", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep entry = new AttackStepMin("entry", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep login = new AttackStepMin("login", new ConstantRealDistribution(0), Order.MIDSTEP);

      access.connectToChild(entry);
      access.connectToChild(bypassAC);
      bypassAC.connectToChild(login);
      login.connectToChild(entry);
      entry.connectToChild(access);

      serviceGraph.addAttackSteps(access,bypassAC,entry,login);
      serviceGraph.sample();

      OutputUtils.plotOn();
      OutputUtils.mathematicaPlot(serviceGraph, 5);
      OutputUtils.plotOff();

      GraphTransformer graphTransformer = new GraphTransformer(serviceGraph);
      Whitebox.invokeMethod(graphTransformer,"deleteAllRedundantEdges");

      OutputUtils.plotOn();
      OutputUtils.mathematicaPlot(serviceGraph, 5);
      OutputUtils.plotOff();
   }

   @Test
   public void ordinalReduceMinZeroSource() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lp");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.ZERO);
      b.setOrdinalTtc(OrdinalTtcValue.SOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      Whitebox.invokeMethod(graphTransformer,"tryOrdinalReduce",c,a,b);

      assertTrue(a.getChildren().contains(b));
      assertFalse(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));
   }

   @Test
   public void ordinalReduceMaxZeroSource() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMax("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lp");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.ZERO);
      b.setOrdinalTtc(OrdinalTtcValue.SOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      Whitebox.invokeMethod(graphTransformer,"tryOrdinalReduce",c,a,b);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertFalse(a.getChildren().contains(c));
      assertTrue(a.getProgenyI().contains(b));
   }

   @Test
   public void ordinalReduceSourceSource() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMax("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lp");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.SOURCE);
      b.setOrdinalTtc(OrdinalTtcValue.SOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      Whitebox.invokeMethod(graphTransformer,"tryOrdinalReduce",c,a,b);

      assertTrue(a.getChildren().contains(b));
      assertFalse(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(c));
   }

   @Test
   public void ordinalReduceMaxSourceLTESource() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMax("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lp");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.SOURCE);
      b.setOrdinalTtc(OrdinalTtcValue.LTESOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      Whitebox.invokeMethod(graphTransformer,"tryOrdinalReduce",c,a,b);

      assertTrue(a.getChildren().contains(b));
      assertFalse(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(c));
   }

   @Test
   public void ordinalReduceMaxSourceGTESource() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMax("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lp");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.SOURCE);
      b.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      Whitebox.invokeMethod(graphTransformer,"tryOrdinalReduce",c,a,b);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertFalse(a.getChildren().contains(c));
      assertTrue(a.getProgenyI().contains(b));
   }

   @Test
   public void ordinalReduceMinSourceLTESource() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lp");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.SOURCE);
      b.setOrdinalTtc(OrdinalTtcValue.LTESOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      Whitebox.invokeMethod(graphTransformer,"tryOrdinalReduce",c,a,b);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertFalse(a.getChildren().contains(c));
      assertTrue(a.getProgenyI().contains(b));
   }

   @Test
   public void ordinalReduceMinSourceGTESource() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lp");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.SOURCE);
      b.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      Whitebox.invokeMethod(graphTransformer,"tryOrdinalReduce",c,a,b);

      assertTrue(a.getChildren().contains(b));
      assertFalse(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(c));
   }

   @Test
   public void ordinalReduceMinZero() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lp");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.ZERO);
      b.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.ANY);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      Whitebox.invokeMethod(graphTransformer,"tryOrdinalReduce",c,a,b);

      assertTrue(a.getChildren().contains(b));
      assertFalse(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(c));
   }

   @Test
   public void ordinalReduceMaxZero() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMax("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lp");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.ZERO);
      b.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.ANY);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      Whitebox.invokeMethod(graphTransformer,"tryOrdinalReduce",c,a,b);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertFalse(a.getChildren().contains(c));
      assertTrue(a.getProgenyI().contains(b));
   }

   @Test
   public void ordinalReduceMinInfinite() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lp");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.INFINITE);
      b.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.ANY);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      Whitebox.invokeMethod(graphTransformer,"tryOrdinalReduce",c,a,b);


      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertFalse(a.getChildren().contains(c));
      assertTrue(a.getProgenyI().contains(b));
   }

   @Test
   public void ordinalReduceMaxInfinite() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMax("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lp");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.INFINITE);
      b.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.ANY);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      Whitebox.invokeMethod(graphTransformer,"tryOrdinalReduce",c,a,b);

      assertTrue(a.getChildren().contains(b));
      assertFalse(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(c));
   }

   @Test
   public void testDeleteRedundantEdgesFromSource() {
      OutputUtils.plotOn();
      Graph graph = new Graph("LooseGraph");
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.ENTRYSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(0), Order.MIDSTEP);
      AttackStep g = new AttackStepMin("g", new ConstantRealDistribution(3), Order.MIDSTEP);
      AttackStep d = new AttackStepMin("d", new ConstantRealDistribution(5), Order.MIDSTEP);
      AttackStep e = new AttackStepMin("e", new ConstantRealDistribution(4), Order.EXITSTEP);
      AttackStep f = new AttackStepMin("f", new ConstantRealDistribution(7), Order.EXITSTEP);

      a.connectToChild(c);
      b.connectToChild(c);
      b.connectToChild(d);
      b.connectToChild(g);
      c.connectToChild(e);
      c.connectToChild(d);
      d.connectToChild(e);
      g.connectToChild(f);
      c.connectToChild(f);
      d.connectToChild(f);
      graph.addAttackSteps(a,b,c,g,d,e,f);

      graph.sample();
      graph.compute();

      OutputUtils.mathematicaPlot(graph, 1);

      assertEquals(graph.getAttackStep(0),a);
      assertEquals(graph.getAttackStep(1),b);
      assertEquals(graph.getEntrySteps().get(1),b);
      graph.ordinalCompute(graph.getEntrySteps().get(1));

      GraphTransformer graphTransformer = new GraphTransformer(graph);
      graphTransformer.deleteRedundantEdgesFromSource(graph, a);

      OutputUtils.mathematicaPlot(graph, 1);
   }

   @Test
   public void testDeleteAllRedundantEdges() throws Exception {
      Graph graph = new Graph("LooseGraph");
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.ENTRYSTEP);
      AttackStep c = new Defense("c", false,false);
      AttackStep g = new AttackStepMin("g", new ConstantRealDistribution(3), Order.MIDSTEP);
      AttackStep d = new AttackStepMin("d", new ConstantRealDistribution(5), Order.MIDSTEP);
      AttackStep e = new AttackStepMin("e", new ConstantRealDistribution(4), Order.EXITSTEP);
      AttackStep f = new AttackStepMin("f", new ConstantRealDistribution(7), Order.EXITSTEP);

      a.connectToChild(c);
      b.connectToChild(c);
      b.connectToChild(d);
      b.connectToChild(g);
      c.connectToChild(e);
      c.connectToChild(d);
      d.connectToChild(e);
      g.connectToChild(f);
      c.connectToChild(f);
      d.connectToChild(f);
      graph.addAttackStep(a);
      graph.addAttackStep(b);
      graph.addAttackStep(c);
      graph.addAttackStep(g);
      graph.addAttackStep(d);
      graph.addAttackStep(e);
      graph.addAttackStep(f);

      graph.sample();
      graph.compute();

      OutputUtils.plotOn();
      OutputUtils.mathematicaPlot(graph, 1);
      OutputUtils.plotOff();

      assertEquals(graph.getAttackStep(0),a);
      assertEquals(graph.getAttackStep(1),b);
      assertEquals(graph.getEntrySteps().get(1),b);
      graph.ordinalCompute(graph.getEntrySteps().get(1));

      GraphTransformer graphTransformer = new GraphTransformer(graph);
      Whitebox.invokeMethod(graphTransformer,"deleteAllRedundantEdges");

      OutputUtils.plotOn();
      OutputUtils.mathematicaPlot(graph, 1);
      OutputUtils.plotOff();
   }

   @Ignore
   @Test
   public void testOrdinalRedAndDelRedEdges() throws Exception {
      Graph graph = new Graph("LooseGraph");
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.ENTRYSTEP);
      AttackStep c = new Defense("c", false,false);
      AttackStep g = new AttackStepMin("g", new ConstantRealDistribution(3), Order.MIDSTEP);
      AttackStep d = new AttackStepMin("d", new ConstantRealDistribution(5), Order.MIDSTEP);
      AttackStep e = new AttackStepMin("e", new ConstantRealDistribution(4), Order.EXITSTEP);
      AttackStep f = new AttackStepMin("f", new ConstantRealDistribution(7), Order.EXITSTEP);

      a.connectToChild(c);
      b.connectToChild(c);
      b.connectToChild(d);
      b.connectToChild(g);
      c.connectToChild(e);
      c.connectToChild(d);
      d.connectToChild(e);
      g.connectToChild(f);
      c.connectToChild(f);
      d.connectToChild(f);
      graph.addAttackStep(a);
      graph.addAttackStep(b);
      graph.addAttackStep(c);
      graph.addAttackStep(g);
      graph.addAttackStep(d);
      graph.addAttackStep(e);
      graph.addAttackStep(f);

      graph.sample();

      OutputUtils.plotOn();
      OutputUtils.mathematicaPlot(graph, 5);
      OutputUtils.plotOff();

      assertEquals(graph.getAttackStep(0),a);
      assertEquals(graph.getAttackStep(1),b);
      assertEquals(graph.getEntrySteps().get(1),b);
      graph.ordinalCompute(graph.getEntrySteps().get(1));

      GraphTransformer graphTransformer = new GraphTransformer(graph);
      Whitebox.invokeMethod(graphTransformer,"ordinalComputeAndDeleteReduntantEdges",a);

      OutputUtils.plotOn();
      OutputUtils.mathematicaPlot(graph, 1);
      OutputUtils.plotOff();
   }

   /* deleteRedundantEdgesFromSource Tests
   * Similar cases to ordinal reduces
   */

   @Test
   public void deleteRedundantEFSMinZeroSource() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lo");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.ZERO);
      b.setOrdinalTtc(OrdinalTtcValue.SOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);

      OutputUtils.plotOn();
      OutputUtils.partialMathematicaPlot(Sets.newHashSet(a,b,c),0);
      OutputUtils.plotOff();

      g.computeDescendantsOf(a);
      graphTransformer.deleteRedundantEdgesFromSource(g,a);

      assertTrue(a.getChildren().contains(b));
      assertFalse(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));
   }

   @Test
   public void deleteRedundantEFSMaxZeroSource() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMax("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lo");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.ZERO);
      b.setOrdinalTtc(OrdinalTtcValue.SOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      g.computeDescendantsOf(a);
      graphTransformer.deleteRedundantEdgesFromSource(g,a);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertFalse(a.getChildren().contains(c));
      assertTrue(a.getProgenyI().contains(b));
   }

   @Ignore
   @Test
   public void deleteRedundantEFSSourceSource() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(2), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(0), Order.MIDSTEP);
      AttackStep c = new AttackStepMax("c", new ConstantRealDistribution(1), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lo");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.SOURCE);
      b.setOrdinalTtc(OrdinalTtcValue.SOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      g.computeDescendantsOf(a);
      graphTransformer.deleteRedundantEdgesFromSource(g,a);
      // TODO figure out firstparent is random
      assertTrue(a.getChildren().contains(b));
      assertFalse(a.getChildren().contains(c));
      assertTrue(b.getChildren().contains(c));
   }

   @Test
   public void deleteRedundantEFSMaxSourceLTESource() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMax("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lo");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.SOURCE);
      b.setOrdinalTtc(OrdinalTtcValue.LTESOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      g.computeDescendantsOf(a);
      graphTransformer.deleteRedundantEdgesFromSource(g,a);

      assertTrue(a.getChildren().contains(b));
      assertFalse(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(c));
   }

   @Test
   public void deleteRedundantEFSMaxSourceGTESource() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMax("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lo");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.SOURCE);
      b.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      g.computeDescendantsOf(a);
      graphTransformer.deleteRedundantEdgesFromSource(g,a);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertFalse(a.getChildren().contains(c));
      assertTrue(a.getProgenyI().contains(b));
   }

   @Test
   public void deleteRedundantEFSMinSourceLTESource() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lo");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.SOURCE);
      b.setOrdinalTtc(OrdinalTtcValue.LTESOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      g.computeDescendantsOf(a);
      graphTransformer.deleteRedundantEdgesFromSource(g,a);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertFalse(a.getChildren().contains(c));
      assertTrue(a.getProgenyI().contains(b));
   }

   @Test
   public void deleteRedundantEFSMaxZero() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMax("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lo");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.ZERO);
      b.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.ANY);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      g.computeDescendantsOf(a);
      graphTransformer.deleteRedundantEdgesFromSource(g,a);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertFalse(a.getChildren().contains(c));
      assertTrue(a.getProgenyI().contains(b));
   }

   @Test
   public void deleteRedundantEFSMinInfinite() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lo");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.INFINITE);
      b.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.ANY);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      g.computeDescendantsOf(a);
      graphTransformer.deleteRedundantEdgesFromSource(g,a);


      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertFalse(a.getChildren().contains(c));
      assertTrue(a.getProgenyI().contains(b));
   }

   @Test
   public void deleteRedundantEFSMaxInfinite() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMax("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lo");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.INFINITE);
      b.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.ANY);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      g.computeDescendantsOf(a);
      graphTransformer.deleteRedundantEdgesFromSource(g,a);

      assertTrue(a.getChildren().contains(b));
      assertFalse(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(c));
   }

   @Test
   public void deleteRedundantEFSMinSourceGTESource() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lo");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.SOURCE);
      b.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      g.computeDescendantsOf(a);
      graphTransformer.deleteRedundantEdgesFromSource(g,a);

      assertTrue(a.getChildren().contains(b));
      assertFalse(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(c));
   }

   @Test
   public void deleteRedundantEFSMinZero() throws Exception{
      OutputUtils.verboseOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(0), Order.MIDSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);

      Graph g = new Graph("lo");
      g.addAttackSteps(a,b,c);

      a.setOrdinalTtc(OrdinalTtcValue.ZERO);
      b.setOrdinalTtc(OrdinalTtcValue.GTESOURCE);
      c.setOrdinalTtc(OrdinalTtcValue.ANY);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      GraphTransformer graphTransformer = new GraphTransformer(g);
      g.computeDescendantsOf(a);
      graphTransformer.deleteRedundantEdgesFromSource(g,a);

      assertTrue(a.getChildren().contains(b));
      assertFalse(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(c));
   }

   /* Single parent merging */

   @Test
   public void mergeSingleParent1() throws Exception{
      OutputUtils.plotOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(4), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(1), Order.MIDSTEP);
      AttackStep d = new AttackStepMin("d", new ConstantRealDistribution(3), Order.EXITSTEP);
      Graph graph = new Graph("LooseGraph");

      a.connectToChild(b);
      b.connectToChild(c);
      c.connectToChild(d);
      graph.addAttackSteps(a,b,c,d);
      graph.sample();
      graph.compute();

      OutputUtils.mathematicaPlot(graph,2);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(c.getChildren().contains(d));

      GraphTransformer graphTransformer = new GraphTransformer(graph);
      Whitebox.invokeMethod(graphTransformer,"reduceAllSingleChildParent");
      OutputUtils.mathematicaPlot(graph,2);
      assertFalse(a.getChildren().contains(b));
      assertFalse(b.getChildren().contains(d));
      assertFalse(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(c));
      assertFalse(c.getChildren().contains(d));
   }

   @Test
   public void mergeSingleParent2() throws Exception{
      OutputUtils.plotOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep e = new AttackStepMin("e", new ConstantRealDistribution(2), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(5), Order.MIDSTEP);
      AttackStep d = new AttackStepMin("d", new ConstantRealDistribution(3), Order.EXITSTEP);
      AttackStep f = new AttackStepMin("f", new ConstantRealDistribution(1), Order.EXITSTEP);
      Graph graph = new Graph("LooseGraph");

      a.connectToChild(b);
      e.connectToChild(b);
      b.connectToChild(c);
      c.connectToChild(d);
      c.connectToChild(f);
      graph.addAttackStep(a);
      graph.addAttackStep(b);
      graph.addAttackStep(c);
      graph.addAttackStep(d);
      graph.addAttackStep(e);
      graph.addAttackStep(f);
      graph.sample();
      graph.compute();

      OutputUtils.mathematicaPlot(graph,2);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(c.getChildren().contains(d));

      GraphTransformer graphTransformer = new GraphTransformer(graph);
      Whitebox.invokeMethod(graphTransformer,"reduceAllSingleChildParent");
      OutputUtils.mathematicaPlot(graph,2);
      assertTrue(a.getChildren().contains(b));
      assertTrue(e.getChildren().contains(b));
      assertTrue(b.getChildren().contains(d));
      assertTrue(b.getChildren().contains(f));
      assertFalse(b.getChildren().contains(c));
      assertFalse(c.getChildren().contains(d));
   }

   @Test
   public void mergeSingleParent3() throws Exception{
      OutputUtils.plotOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep e = new AttackStepMin("e", new ConstantRealDistribution(2), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(5), Order.MIDSTEP);
      AttackStep d = new AttackStepMin("d", new ConstantRealDistribution(3), Order.EXITSTEP);
      Graph graph = new Graph("LooseGraph");

      a.connectToChild(b);
      e.connectToChild(b);
      b.connectToChild(c);
      c.connectToChild(d);
      graph.addAttackStep(a);
      graph.addAttackStep(b);
      graph.addAttackStep(c);
      graph.addAttackStep(d);
      graph.addAttackStep(e);
      graph.sample();

      OutputUtils.mathematicaPlot(graph,2);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(c.getChildren().contains(d));

      OutputUtils.verboseOn();
      GraphTransformer graphTransformer = new GraphTransformer(graph);
      Whitebox.invokeMethod(graphTransformer,"reduceAllSingleChildParent");
      OutputUtils.mathematicaPlot(graph,2);
      assertTrue(a.getChildren().contains(b));
      assertTrue(e.getChildren().contains(b));
      assertFalse(b.getChildren().contains(d));
      assertFalse(b.getChildren().contains(c));
      assertFalse(c.getChildren().contains(d));
   }

   @Test
   public void mergeSingleParent4() throws Exception{
      OutputUtils.plotOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep e = new AttackStepMin("e", new ConstantRealDistribution(2), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMax("c", new ConstantRealDistribution(5), Order.MIDSTEP);
      AttackStep d = new AttackStepMin("d", new ConstantRealDistribution(3), Order.EXITSTEP);
      Graph graph = new Graph("LooseGraph");

      a.connectToChild(b);
      e.connectToChild(b);
      b.connectToChild(c);
      c.connectToChild(d);
      graph.addAttackStep(a);
      graph.addAttackStep(b);
      graph.addAttackStep(c);
      graph.addAttackStep(d);
      graph.addAttackStep(e);
      graph.sample();
      graph.compute();

      OutputUtils.mathematicaPlot(graph,2);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(c.getChildren().contains(d));

      GraphTransformer graphTransformer = new GraphTransformer(graph);
      Whitebox.invokeMethod(graphTransformer,"reduceAllSingleChildParent");
      OutputUtils.mathematicaPlot(graph,2);
      assertTrue(a.getChildren().contains(b));
      assertTrue(e.getChildren().contains(b));
      assertTrue(b instanceof AttackStepMin);
      assertFalse(b.getChildren().contains(d));
      assertFalse(b.getChildren().contains(c));
      assertFalse(c.getChildren().contains(d));
   }

   @Test
   public void mergeSingleParent5() throws Exception{
      OutputUtils.plotOn();
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep e = new AttackStepMin("e", new ConstantRealDistribution(2), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMax("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(5), Order.MIDSTEP);
      AttackStep d = new AttackStepMin("d", new ConstantRealDistribution(3), Order.EXITSTEP);
      Graph graph = new Graph("LooseGraph");

      a.connectToChild(b);
      e.connectToChild(b);
      b.connectToChild(c);
      c.connectToChild(d);
      graph.addAttackStep(a);
      graph.addAttackStep(b);
      graph.addAttackStep(c);
      graph.addAttackStep(d);
      graph.addAttackStep(e);
      graph.sample();
      graph.compute();

      OutputUtils.mathematicaPlot(graph,2);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(c.getChildren().contains(d));

      GraphTransformer graphTransformer = new GraphTransformer(graph);
      Whitebox.invokeMethod(graphTransformer,"reduceAllSingleChildParent");
      OutputUtils.mathematicaPlot(graph,2);
      assertTrue(a.getChildren().contains(b));
      assertTrue(e.getChildren().contains(b));
      assertTrue(b instanceof AttackStepMax);
      assertFalse(b.getChildren().contains(d));
      assertFalse(b.getChildren().contains(c));
      assertFalse(c.getChildren().contains(d));
   }
   /* Full Reduce */

   @Ignore
   @Test
   public void reduceThreeAS() throws Exception{
      OutputUtils.plotOn();
      OutputUtils.veryVerboseOn();
      Graph graph = new Graph("LooseGraph");
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(3), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMax("c", new ConstantRealDistribution(3), Order.EXITSTEP);

      a.connectToChild(b);
      a.connectToChild(c);
      b.connectToChild(c);
      graph.addAttackStep(a);
      graph.addAttackStep(b);
      graph.addAttackStep(c);
      graph.sample();

      OutputUtils.mathematicaPlot(graph,2);

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(a.getChildren().contains(b));

      double initialLocalTTCOfa = a.getLocalTtc();

      GraphTransformer graphTransformer = new GraphTransformer(graph);
      graphTransformer.reduce(graph);

      OutputUtils.mathematicaPlot(graph,1);

      assertFalse(a.getChildren().contains(c));
      assertTrue(graph.containsStep(b));
      assertTrue(a.getLocalTtc() == b.getLocalTtc() + initialLocalTTCOfa);
   }

   /* Loop Removal */

   @Test
   public void LRsimpleDelete() throws Exception{
      OutputUtils.plotOn();
      OutputUtils.veryVerboseOn();
      // Simple loop between a and b, as is midstep so will be deleted
      Graph graph = new Graph("LooseGraph");
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(3), Order.MIDSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.EXITSTEP);

      a.connectToChild(b);
      b.connectToChild(a);
      graph.addAttackStep(a);
      graph.addAttackStep(b);
      graph.sample();

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(a));

      GraphTransformer graphTransformer = new GraphTransformer(graph);
      Whitebox.invokeMethod(graphTransformer,"deleteUnproductiveLoops");

      OutputUtils.mathematicaPlot(graph,1);



      assertFalse(graph.containsStep(a));
      assertTrue(graph.containsStep(b));
   }

   @Test
   public void LR3NodesDelete() throws Exception{
      OutputUtils.plotOn();
      OutputUtils.veryVerboseOn();
      // Simple loop between a b and c, a and b are midstep so will be deleted
      Graph graph = new Graph("LooseGraph");
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(3), Order.MIDSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(3), Order.MIDSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(2), Order.EXITSTEP);

      a.connectToChild(b);
      b.connectToChild(c);
      c.connectToChild(a);
      graph.addAttackStep(a);
      graph.addAttackStep(b);
      graph.addAttackStep(c);
      graph.sample();

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(c.getChildren().contains(a));

      GraphTransformer graphTransformer = new GraphTransformer(graph);
      Whitebox.invokeMethod(graphTransformer,"deleteUnproductiveLoops");

      OutputUtils.mathematicaPlot(graph,1);

      assertFalse(graph.containsStep(a));
      assertFalse(graph.containsStep(b));
      assertTrue(graph.containsStep(c));
   }

   @Test
   public void LR3NodesNoDelete() throws Exception{
      OutputUtils.plotOn();
      OutputUtils.veryVerboseOn();
      // A is now a EntryStep so no removal
      Graph graph = new Graph("LooseGraph");
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(3), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(3), Order.MIDSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(2), Order.EXITSTEP);

      a.connectToChild(b);
      b.connectToChild(c);
      c.connectToChild(a);
      graph.addAttackStep(a);
      graph.addAttackStep(b);
      graph.addAttackStep(c);
      graph.sample();

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(c.getChildren().contains(a));

      GraphTransformer graphTransformer = new GraphTransformer(graph);
      Whitebox.invokeMethod(graphTransformer,"deleteUnproductiveLoops");

      OutputUtils.mathematicaPlot(graph,1);

      assertTrue(graph.containsStep(a));
      assertTrue(graph.containsStep(b));
      assertTrue(graph.containsStep(c));
   }

   @Test
   public void LR3NodesNoDelete2() throws Exception{
      OutputUtils.plotOn();
      OutputUtils.veryVerboseOn();
      // B is now a ExitStep so no removal
      Graph graph = new Graph("LooseGraph");
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(3), Order.MIDSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(3), Order.EXITSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(2), Order.EXITSTEP);

      a.connectToChild(b);
      b.connectToChild(c);
      c.connectToChild(a);
      graph.addAttackStep(a);
      graph.addAttackStep(b);
      graph.addAttackStep(c);
      graph.sample();

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(c));
      assertTrue(c.getChildren().contains(a));

      GraphTransformer graphTransformer = new GraphTransformer(graph);
      Whitebox.invokeMethod(graphTransformer,"deleteUnproductiveLoops");

      OutputUtils.mathematicaPlot(graph,1);

      assertTrue(graph.containsStep(a));
      assertTrue(graph.containsStep(b));
      assertTrue(graph.containsStep(c));
   }

   @Test
   public void LRsimpleNoDeleteEn() throws Exception{
      OutputUtils.plotOn();
      OutputUtils.veryVerboseOn();
      // A is now a EntryStep so should not be removed
      Graph graph = new Graph("LooseGraph");
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(3), Order.ENTRYSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.EXITSTEP);

      a.connectToChild(b);
      b.connectToChild(a);
      graph.addAttackStep(a);
      graph.addAttackStep(b);
      graph.sample();

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(a));

      GraphTransformer graphTransformer = new GraphTransformer(graph);
      Whitebox.invokeMethod(graphTransformer,"deleteUnproductiveLoops");

      OutputUtils.mathematicaPlot(graph,1);

      assertTrue(graph.containsStep(a));
      assertTrue(graph.containsStep(b));
   }

   @Test
   public void LRsimpleNoDeleteEx() throws Exception{
      OutputUtils.plotOn();
      OutputUtils.veryVerboseOn();
      // A is now a ExitStep so should not be removed
      Graph graph = new Graph("LooseGraph");
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(3), Order.EXITSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.EXITSTEP);

      a.connectToChild(b);
      b.connectToChild(a);
      graph.addAttackStep(a);
      graph.addAttackStep(b);
      graph.sample();

      assertTrue(a.getChildren().contains(b));
      assertTrue(b.getChildren().contains(a));

      GraphTransformer graphTransformer = new GraphTransformer(graph);
      Whitebox.invokeMethod(graphTransformer,"deleteUnproductiveLoops");

      OutputUtils.mathematicaPlot(graph,1);

      assertTrue(graph.containsStep(a));
      assertTrue(graph.containsStep(b));
   }

   @Test
   public void LRLoopEx() throws Exception{
      OutputUtils.plotOn();
      OutputUtils.veryVerboseOn();
      // A is now a ExitStep so should not be removed
      Graph graph = new Graph("LooseGraph");
      AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(3), Order.EXITSTEP);
      AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep d = new AttackStepMin("d", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep e = new AttackStepMin("e", new ConstantRealDistribution(2), Order.EXITSTEP);

      a.connectToChild(b);
      b.connectToChild(c);
      c.connectToChild(d);
      d.connectToChild(a);
      a.connectToChild(e);
      graph.addAttackSteps(a,b,c,d,e);
      graph.sample();

      GraphTransformer graphTransformer = new GraphTransformer(graph);
      Whitebox.invokeMethod(graphTransformer,"deleteUnproductiveLoops");

      OutputUtils.mathematicaPlot(graph,1);

      for (AttackStep as : a.descendantsTo(e)) OutputUtils.printVerbose(as.getName());

      assertTrue(graph.containsStep(a));
      assertTrue(graph.containsStep(e));
      assertFalse(graph.containsStep(b));
      assertFalse(graph.containsStep(c));
      assertFalse(graph.containsStep(d));
   }

   @After
   public void close() {
      OutputUtils.verboseOff();
   }
}
