package support;


import attackgraph.AttackStep;
import attackgraph.AttackStepMin;
import datatypes.Order;
import attackgraph.Graph;
import org.apache.commons.math3.distribution.ConstantRealDistribution;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BFSTest {

    @Test
    public void testAncestors1(){
        AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep d = new AttackStepMin("d", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep e = new AttackStepMin("e", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep f = new AttackStepMin("f", new ConstantRealDistribution(0), Order.ENTRYSTEP);

        a.connectToChild(b);
        b.connectToChild(c);
        c.connectToChild(d);
        a.connectToChild(e);
        e.connectToChild(f);

        Set<AttackStep> ancestorsD = BFS.breadthFirstAncestorsOf(d);

        assertFalse(ancestorsD.contains(d));
        assertTrue(ancestorsD.contains(b));
        assertTrue(ancestorsD.contains(c));
        assertTrue(ancestorsD.contains(a));
        assertFalse(ancestorsD.contains(e));
        assertFalse(ancestorsD.contains(f));
    }

    @Test
    public void testDescendants1(){
        AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep d = new AttackStepMin("d", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep e = new AttackStepMin("e", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep f = new AttackStepMin("f", new ConstantRealDistribution(0), Order.ENTRYSTEP);

        a.connectToChild(b);
        b.connectToChild(c);
        c.connectToChild(d);
        a.connectToChild(e);
        e.connectToChild(f);

        Set<AttackStep> descendantsOfA = BFS.breadthFirstDescendantsOf(a);

        assertFalse(descendantsOfA.contains(a));
        assertTrue(descendantsOfA.contains(b));
        assertTrue(descendantsOfA.contains(c));
        assertTrue(descendantsOfA.contains(d));
        assertTrue(descendantsOfA.contains(e));
        assertTrue(descendantsOfA.contains(f));
    }

    @Test
    public void testAncestors2(){
        AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep d = new AttackStepMin("d", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep e = new AttackStepMin("e", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep f = new AttackStepMin("f", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep g = new AttackStepMin("g", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep h = new AttackStepMin("h", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep i = new AttackStepMin("i", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep j = new AttackStepMin("j", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep k = new AttackStepMin("k", new ConstantRealDistribution(0), Order.EXITSTEP);

        a.connectToChild(b);
        a.connectToChild(c);
        b.connectToChild(d);
        b.connectToChild(e);
        c.connectToChild(e);
        c.connectToChild(f);
        f.connectToChild(i);
        d.connectToChild(g);
        e.connectToChild(g);
        e.connectToChild(h);
        g.connectToChild(j);
        h.connectToChild(j);
        h.connectToChild(k);

        Graph gr = new Graph("loos");
        gr.addAttackSteps(a,b,c,d,e,f,g,h,i,j,k);

        Set<AttackStep> ancestorsJ = BFS.breadthFirstAncestorsOf(j);
        System.out.println(ancestorsJ.stream().map(AttackStep::getName).collect(Collectors.joining("; ")));

        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(gr,2);

        assertTrue(ancestorsJ.contains(d));
        assertTrue(ancestorsJ.contains(b));
        assertTrue(ancestorsJ.contains(c));
        assertTrue(ancestorsJ.contains(a));
        assertTrue(ancestorsJ.contains(e));
        assertTrue(ancestorsJ.contains(g));
        assertTrue(ancestorsJ.contains(h));
        assertFalse(ancestorsJ.contains(j));
        assertFalse(ancestorsJ.contains(f));
        assertFalse(ancestorsJ.contains(i));
        assertFalse(ancestorsJ.contains(k));
    }


    @Test
    public void testDescendants2(){
        AttackStep a = new AttackStepMin("a", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep b = new AttackStepMin("b", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep d = new AttackStepMin("d", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep e = new AttackStepMin("e", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep f = new AttackStepMin("f", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep g = new AttackStepMin("g", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep h = new AttackStepMin("h", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep i = new AttackStepMin("i", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep j = new AttackStepMin("j", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep k = new AttackStepMin("k", new ConstantRealDistribution(0), Order.EXITSTEP);

        a.connectToChild(b);
        a.connectToChild(c);
        b.connectToChild(d);
        b.connectToChild(e);
        c.connectToChild(e);
        c.connectToChild(f);
        f.connectToChild(i);
        d.connectToChild(g);
        e.connectToChild(g);
        e.connectToChild(h);
        g.connectToChild(j);
        h.connectToChild(j);
        h.connectToChild(k);

        Graph gr = new Graph("loos");
        gr.addAttackSteps(a,b,c,d,e,f,g,h,i,j,k);

        Set<AttackStep> descendantsA = BFS.breadthFirstDescendantsOf(a);
        System.out.println(descendantsA.stream().map(AttackStep::getName).collect(Collectors.joining("; ")));

        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(gr,2);

        assertTrue(descendantsA.contains(d));
        assertTrue(descendantsA.contains(b));
        assertTrue(descendantsA.contains(c));
        assertFalse(descendantsA.contains(a));
        assertTrue(descendantsA.contains(e));
        assertTrue(descendantsA.contains(g));
        assertTrue(descendantsA.contains(h));
        assertTrue(descendantsA.contains(j));
        assertTrue(descendantsA.contains(f));
        assertTrue(descendantsA.contains(i));
        assertTrue(descendantsA.contains(k));
    }

    @Test
    public void ancestorsLoop() {
        Graph gr = new Graph("LooseGraph");
        AttackStep as1 = new AttackStepMin("as1", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep as2 = new AttackStepMin("as2", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as3 = new AttackStepMin("as3", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as4 = new AttackStepMin("as4", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep as5 = new AttackStepMin("as5", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep as6 = new AttackStepMin("as6", new ConstantRealDistribution(0), Order.EXITSTEP);

        as1.connectToChild(as2);
        as2.connectToChild(as3);
        as3.connectToChild(as4);
        as4.connectToChild(as5);
        as5.connectToChild(as2);
        as5.connectToChild(as6);
        gr.addAttackSteps(as1, as2, as3, as4, as5, as6);

        Set<AttackStep> ancestorsAS6 = BFS.breadthFirstAncestorsOf(as6);
        Set<AttackStep> ancestorsAS2 = BFS.breadthFirstAncestorsOf(as2);
        System.out.println(ancestorsAS6.stream().map(AttackStep::getName).collect(Collectors.joining("; ")));

        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(gr,2);

        assertTrue(ancestorsAS6.contains(as5));
        assertTrue(ancestorsAS6.contains(as4));
        assertTrue(ancestorsAS6.contains(as3));
        assertTrue(ancestorsAS6.contains(as2));
        assertTrue(ancestorsAS6.contains(as1));
        assertFalse(ancestorsAS6.contains(as6));

        assertTrue(ancestorsAS2.contains(as5));
        assertTrue(ancestorsAS2.contains(as4));
        assertTrue(ancestorsAS2.contains(as3));
        assertTrue(ancestorsAS2.contains(as1));
        assertFalse(ancestorsAS2.contains(as2));
        assertFalse(ancestorsAS2.contains(as6));
    }

    @Test
    public void ProgenyLoop() {
        Graph gr = new Graph("LooseGraph");
        AttackStep as1 = new AttackStepMin("as1", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep as2 = new AttackStepMin("as2", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as3 = new AttackStepMin("as3", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as4 = new AttackStepMin("as4", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep as5 = new AttackStepMin("as5", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep as6 = new AttackStepMin("as6", new ConstantRealDistribution(0), Order.EXITSTEP);

        as1.connectToChild(as2);
        as2.connectToChild(as3);
        as3.connectToChild(as4);
        as4.connectToChild(as5);
        as5.connectToChild(as2);
        as5.connectToChild(as6);
        gr.addAttackSteps(as1, as2, as3, as4, as5, as6);

        Set<AttackStep> progenyAS1 = BFS.breadthFirstDescendantsOf(as1);
        Set<AttackStep> progenyAS3 = BFS.breadthFirstDescendantsOf(as3);
        System.out.println(progenyAS1.stream().map(AttackStep::getName).collect(Collectors.joining("; ")));

        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(gr,2);

        assertTrue(progenyAS1.contains(as5));
        assertTrue(progenyAS1.contains(as4));
        assertTrue(progenyAS1.contains(as3));
        assertTrue(progenyAS1.contains(as2));
        assertTrue(progenyAS1.contains(as6));
        assertFalse(progenyAS1.contains(as1));

        assertTrue(progenyAS3.contains(as5));
        assertTrue(progenyAS3.contains(as4));
        assertFalse(progenyAS3.contains(as3));
        assertTrue(progenyAS3.contains(as2));
        assertTrue(progenyAS3.contains(as6));
        assertFalse(progenyAS3.contains(as1));
    }

    /*

   @Test
   public void testProgeniesSuperSimple() {
      Graph graph = new Graph("LooseGraph");
      AttackStep as1 = new AttackStepMin("as1", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep as2 = new AttackStepMin("as2", new ConstantRealDistribution(2), Order.EXITSTEP);

      as1.connectToChild(as2);
      graph.addAttackSteps(as1, as2);

      assertTrue(as1.getChildren().contains(as2));

      OutputUtils.plotOn();
      assertTrue(!graph.areProgeniesUpToDate());

      graph.computeAllProgenies();
      assertTrue(graph.areProgeniesUpToDate());
      assertTrue(as1.getDescendants().contains(as2));
   }

   @Test
   public void testProgeniesSuperSimpleCycle() {
      Graph graph = new Graph("LooseGraph");
      AttackStep as1 = new AttackStepMin("as1", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep as2 = new AttackStepMin("as2", new ConstantRealDistribution(2), Order.EXITSTEP);

      as1.connectToChild(as2);
      as2.connectToChild(as1);
      graph.addAttackSteps(as1, as2);

      assertTrue(as1.getChildren().contains(as2));
      assertTrue(as2.getChildren().contains(as1));

      OutputUtils.plotOn();
      assertTrue(!graph.areProgeniesUpToDate());

      graph.computeAllProgenies();
      assertTrue(graph.areProgeniesUpToDate());
      assertTrue(as1.getDescendants().contains(as2));
   }

   @Test
   public void testProgeniesSimple() {
      Graph graph = new Graph("LooseGraph");
      AttackStep as1 = new AttackStepMin("as1", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep as2 = new AttackStepMin("as2", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep as3 = new AttackStepMin("as3", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep as4 = new AttackStepMin("as4", new ConstantRealDistribution(0), Order.EXITSTEP);

      as1.connectToChild(as2);
      as1.connectToChild(as3);
      as2.connectToChild(as4);
      as3.connectToChild(as4);
      graph.addAttackSteps(as1, as2, as3, as4);

      assertTrue(as1.getChildren().contains(as2));
      assertTrue(as2.getChildren().contains(as4));
      assertTrue(as1.getChildren().contains(as3));

      OutputUtils.plotOn();
      assertTrue(!graph.areProgeniesUpToDate());

      graph.computeAllProgenies();
      assertTrue(graph.areProgeniesUpToDate());
      assertTrue(as1.getDescendants().contains(as2));
      assertTrue(as1.getDescendants().contains(as3));
      assertTrue(as1.getDescendants().contains(as4));
      assertTrue(as2.getDescendants().contains(as4));
      assertTrue(as3.getDescendants().contains(as4));
      assertFalse(as4.getDescendants().contains(as1));
      assertFalse(as4.getDescendants().contains(as2));
      assertFalse(as4.getDescendants().contains(as3));
      assertFalse(as2.getDescendants().contains(as3));
      assertFalse(as2.getDescendants().contains(as1));
      assertFalse(as3.getDescendants().contains(as2));
      assertFalse(as3.getDescendants().contains(as1));
   }

   @Ignore
   @Test
   public void testProgeniesSimpleCycle() {
      Graph graph = new Graph("LooseGraph");
      AttackStep as1 = new AttackStepMin("as1", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep as2 = new AttackStepMin("as2", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep as3 = new AttackStepMin("as3", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep as4 = new AttackStepMin("as4", new ConstantRealDistribution(0), Order.EXITSTEP);

      as1.connectToChild(as2);
      as1.connectToChild(as3);
      as2.connectToChild(as4);
      as3.connectToChild(as4);
      as4.connectToChild(as1);
      graph.addAttackSteps(as1, as2, as3, as4);

      assertTrue(as1.getChildren().contains(as2));
      assertTrue(as2.getChildren().contains(as4));
      assertTrue(as1.getChildren().contains(as3));

      OutputUtils.plotOn();
      assertTrue(!graph.areProgeniesUpToDate());

      graph.computeAllProgenies();
      assertTrue(graph.areProgeniesUpToDate());
      assertTrue(as1.getDescendants().contains(as2));
      assertTrue(as1.getDescendants().contains(as3));
      assertTrue(as1.getDescendants().contains(as4));
      assertTrue(as2.getDescendants().contains(as4));
      assertTrue(as3.getDescendants().contains(as4));
      assertTrue(as4.getDescendants().contains(as1));
      assertTrue(as4.getDescendants().contains(as2));
      assertTrue(as4.getDescendants().contains(as3));
      assertTrue(as2.getDescendants().contains(as3));
      assertTrue(as2.getDescendants().contains(as1));
      assertTrue(as3.getDescendants().contains(as2));
      assertTrue(as3.getDescendants().contains(as1));
   }

   @Ignore
   @Test
   public void testProgeniesObserver() throws Exception {
      Graph graph = new Graph("LooseGraph");
      AttackStep as1 = new AttackStepMin("as1", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep as2 = new AttackStepMin("as2", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep as3 = new AttackStepMin("as3", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep as4 = new AttackStepMin("as4", new ConstantRealDistribution(0), Order.EXITSTEP);

      as1.connectToChild(as2);
      as1.connectToChild(as3);
      as2.connectToChild(as4);
      as3.connectToChild(as4);
      as4.connectToChild(as1);
      graph.addAttackSteps(as1,as2,as3,as4);

      assertTrue(as1.getChildren().contains(as2));
      assertTrue(as2.getChildren().contains(as4));
      assertTrue(as1.getChildren().contains(as3));

      OutputUtils.plotOn();
      assertTrue(!graph.areProgeniesUpToDate());

      graph.updateRoots();
      graph.computeAllProgenies();
      assertTrue(graph.areProgeniesUpToDate());
      assertTrue(as1.getDescendants().contains(as2));
      assertTrue(as1.getDescendants().contains(as3));
      assertTrue(as1.getDescendants().contains(as4));
      assertTrue(as2.getDescendants().contains(as4));
      assertTrue(as3.getDescendants().contains(as4));
      assertTrue(as4.getDescendants().contains(as1));
      assertTrue(as4.getDescendants().contains(as2));
      assertTrue(as4.getDescendants().contains(as3));
      assertTrue(as2.getDescendants().contains(as3));
      assertTrue(as2.getDescendants().contains(as1));
      assertTrue(as3.getDescendants().contains(as2));
      assertTrue(as3.getDescendants().contains(as1));

      as4.removeChild(as1);

      assertTrue(!graph.areProgeniesUpToDate());


      graph.computeAllProgenies();
      assertTrue(graph.areProgeniesUpToDate());
      assertTrue(as1.getDescendants().contains(as2));
      assertTrue(as1.getDescendants().contains(as3));
      assertTrue(as1.getDescendants().contains(as4));
      assertTrue(as2.getDescendants().contains(as4));
      assertTrue(as3.getDescendants().contains(as4));
      assertFalse(as4.getDescendants().contains(as1));
      assertFalse(as4.getDescendants().contains(as2));
      assertFalse(as4.getDescendants().contains(as3));
      assertFalse(as2.getDescendants().contains(as3));
      assertFalse(as2.getDescendants().contains(as1));
      assertFalse(as3.getDescendants().contains(as2));
      assertFalse(as3.getDescendants().contains(as1));
   }

   @Ignore
   @Test
   public void testProgeniesAncestors2Observer() throws Exception {
      Graph graph = new Graph("LooseGraph");
      AttackStep as1 = new AttackStepMin("as1", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep as2 = new AttackStepMin("as2", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep as3 = new AttackStepMin("as3", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep as4 = new AttackStepMin("as4", new ConstantRealDistribution(0), Order.EXITSTEP);

      as1.connectToChild(as2);
      as1.connectToChild(as3);
      as2.connectToChild(as4);
      as3.connectToChild(as4);
      as4.connectToChild(as1);
      graph.addAttackSteps(as1,as2,as3,as4);

      assertTrue(as1.getChildren().contains(as2));
      assertTrue(as2.getChildren().contains(as4));
      assertTrue(as1.getChildren().contains(as3));

      OutputUtils.plotOn();
      assertTrue(!graph.areProgeniesUpToDate());

      graph.updateRoots();
      graph.computeProgenies2();
      assertTrue(graph.areProgeniesUpToDate());
      assertTrue(as1.getDescendants().contains(as2));
      assertTrue(as1.getDescendants().contains(as3));
      assertTrue(as1.getDescendants().contains(as4));
      assertTrue(as2.getDescendants().contains(as4));
      assertTrue(as3.getDescendants().contains(as4));
      assertTrue(as4.getDescendants().contains(as1));
      assertTrue(as4.getDescendants().contains(as2));
      assertTrue(as4.getDescendants().contains(as3));
      assertTrue(as2.getDescendants().contains(as3));
      assertTrue(as2.getDescendants().contains(as1));
      assertTrue(as3.getDescendants().contains(as2));
      assertTrue(as3.getDescendants().contains(as1));
      assertTrue(as1.getAncestorsR().contains(as4));
      assertTrue(as1.getAncestorsR().contains(as2));
      assertTrue(as1.getAncestorsR().contains(as3));
      assertTrue(as2.getAncestorsR().contains(as1));
      assertTrue(as2.getAncestorsR().contains(as3));
      assertTrue(as2.getAncestorsR().contains(as4));
      assertTrue(as3.getAncestorsR().contains(as1));
      assertTrue(as3.getAncestorsR().contains(as2));
      assertTrue(as3.getAncestorsR().contains(as4));
      assertTrue(as4.getAncestorsR().contains(as1));
      assertTrue(as4.getAncestorsR().contains(as2));
      assertTrue(as4.getAncestorsR().contains(as3));

      as4.removeChild(as1);

      assertTrue(!graph.areProgeniesUpToDate());


      graph.computeAllProgenies();
      assertTrue(graph.areProgeniesUpToDate());
      assertTrue(as1.getDescendants().contains(as2));
      assertTrue(as1.getDescendants().contains(as3));
      assertTrue(as1.getDescendants().contains(as4));
      assertTrue(as2.getDescendants().contains(as4));
      assertTrue(as3.getDescendants().contains(as4));
      assertFalse(as4.getDescendants().contains(as1));
      assertFalse(as4.getDescendants().contains(as2));
      assertFalse(as4.getDescendants().contains(as3));
      assertFalse(as2.getDescendants().contains(as3));
      assertFalse(as2.getDescendants().contains(as1));
      assertFalse(as3.getDescendants().contains(as2));
      assertFalse(as3.getDescendants().contains(as1));
      assertFalse(as1.getAncestorsR().contains(as4));
      assertFalse(as1.getAncestorsR().contains(as2));
      assertFalse(as1.getAncestorsR().contains(as3));
      assertTrue(as2.getAncestorsR().contains(as1));
      assertFalse(as2.getAncestorsR().contains(as3));
      assertFalse(as2.getAncestorsR().contains(as4));
      assertTrue(as3.getAncestorsR().contains(as1));
      assertFalse(as3.getAncestorsR().contains(as2));
      assertFalse(as3.getAncestorsR().contains(as4));
      assertTrue(as4.getAncestorsR().contains(as1));
      assertTrue(as4.getAncestorsR().contains(as2));
      assertTrue(as4.getAncestorsR().contains(as3));
   }

   @Ignore
   @Test
   public void testProgeniesAncestors2doubleEntries() throws Exception {
      Graph graph = new Graph("LooseGraph");
      AttackStep as1 = new AttackStepMin("as1", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep as2 = new AttackStepMin("as2", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep as3 = new AttackStepMin("as3", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep as4 = new AttackStepMin("as4", new ConstantRealDistribution(0), Order.MIDSTEP);
      AttackStep as5 = new AttackStepMin("as5", new ConstantRealDistribution(0), Order.MIDSTEP);

      as1.connectToChild(as2);
      as1.connectToChild(as3);
      as2.connectToChild(as4);
      as3.connectToChild(as4);
      as5.connectToChild(as3);
      as4.connectToChild(as1);
      graph.addAttackSteps(as1, as2, as3, as4, as5);

      assertTrue(as1.getChildren().contains(as2));
      assertTrue(as2.getChildren().contains(as4));
      assertTrue(as1.getChildren().contains(as3));

      OutputUtils.plotOn();
      assertTrue(!graph.areProgeniesUpToDate());

      graph.updateRoots();
      graph.computeProgenies2();
      assertTrue(graph.areProgeniesUpToDate());
      assertTrue(as1.getDescendants().contains(as2));
      assertTrue(as1.getDescendants().contains(as3));
      assertTrue(as1.getDescendants().contains(as4));
      assertFalse(as1.getDescendants().contains(as5));
      assertTrue(as5.getDescendants().contains(as1));
      assertTrue(as5.getDescendants().contains(as2));
      assertTrue(as5.getDescendants().contains(as3));
      assertTrue(as5.getDescendants().contains(as4));
      assertFalse(as2.getDescendants().contains(as5));
      assertFalse(as3.getDescendants().contains(as5));
      assertFalse(as4.getDescendants().contains(as5));
      assertTrue(as2.getDescendants().contains(as4));
      assertTrue(as3.getDescendants().contains(as4));
      assertTrue(as4.getDescendants().contains(as1));
      assertTrue(as4.getDescendants().contains(as2));
      assertTrue(as4.getDescendants().contains(as3));
      assertTrue(as2.getDescendants().contains(as3));
      assertTrue(as2.getDescendants().contains(as1));
      assertTrue(as3.getDescendants().contains(as2));
      assertTrue(as3.getDescendants().contains(as1));
      assertTrue(as1.getAncestorsR().contains(as4));
      assertTrue(as1.getAncestorsR().contains(as2));
      assertTrue(as1.getAncestorsR().contains(as3));
      assertTrue(as2.getAncestorsR().contains(as1));
      assertTrue(as2.getAncestorsR().contains(as3));
      assertTrue(as2.getAncestorsR().contains(as4));
      assertTrue(as3.getAncestorsR().contains(as1));
      assertTrue(as3.getAncestorsR().contains(as2));
      assertTrue(as3.getAncestorsR().contains(as4));
      assertTrue(as4.getAncestorsR().contains(as1));
      assertTrue(as4.getAncestorsR().contains(as2));
      assertTrue(as4.getAncestorsR().contains(as3));
      assertFalse(as5.getAncestorsR().contains(as1));
      assertFalse(as5.getAncestorsR().contains(as2));
      assertFalse(as5.getAncestorsR().contains(as3));
      assertFalse(as5.getAncestorsR().contains(as4));

      as4.removeChild(as1);

      assertTrue(!graph.areProgeniesUpToDate());


      graph.computeAllProgenies();
      assertTrue(graph.areProgeniesUpToDate());
      assertTrue(as1.getDescendants().contains(as2));
      assertTrue(as1.getDescendants().contains(as3));
      assertTrue(as1.getDescendants().contains(as4));
      assertTrue(as2.getDescendants().contains(as4));
      assertTrue(as3.getDescendants().contains(as4));
      assertFalse(as1.getDescendants().contains(as5));
      assertFalse(as2.getDescendants().contains(as5));
      assertFalse(as3.getDescendants().contains(as5));
      assertFalse(as4.getDescendants().contains(as5));
      assertFalse(as4.getDescendants().contains(as1));
      assertFalse(as4.getDescendants().contains(as2));
      assertFalse(as4.getDescendants().contains(as3));
      assertFalse(as2.getDescendants().contains(as3));
      assertFalse(as2.getDescendants().contains(as1));
      assertFalse(as3.getDescendants().contains(as2));
      assertFalse(as3.getDescendants().contains(as1));
      assertFalse(as1.getAncestorsR().contains(as4));
      assertFalse(as1.getAncestorsR().contains(as2));
      assertFalse(as1.getAncestorsR().contains(as3));
      assertTrue(as2.getAncestorsR().contains(as1));
      assertFalse(as2.getAncestorsR().contains(as3));
      assertFalse(as2.getAncestorsR().contains(as4));
      assertTrue(as3.getAncestorsR().contains(as1));
      assertFalse(as3.getAncestorsR().contains(as2));
      assertFalse(as3.getAncestorsR().contains(as4));
      assertTrue(as4.getAncestorsR().contains(as1));
      assertTrue(as4.getAncestorsR().contains(as2));
      assertTrue(as4.getAncestorsR().contains(as3));
      assertFalse(as5.getAncestorsR().contains(as3));
      assertFalse(as5.getAncestorsR().contains(as4));
      assertFalse(as5.getAncestorsR().contains(as1));
      assertFalse(as5.getAncestorsR().contains(as2));
   }

   @Ignore
   @Test
   public void testComputeAllProgeniesAncestorsCycle() throws Exception {
      Graph graph = new Graph("LooseGraph");
      AttackStep as1 = new AttackStepMin("as1", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep as2 = new AttackStepMin("as2", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep as3 = new AttackStepMin("as3", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep as4 = new AttackStepMin("as4", new ConstantRealDistribution(0), Order.MIDSTEP);
      AttackStep as5 = new AttackStepMin("as5", new ConstantRealDistribution(0), Order.MIDSTEP);
      AttackStep as6 = new AttackStepMin("as6", new ConstantRealDistribution(0), Order.EXITSTEP);

      as1.connectToChild(as2);
      as2.connectToChild(as3);
      as3.connectToChild(as4);
      as4.connectToChild(as5);
      as5.connectToChild(as2);
      as5.connectToChild(as6);
      graph.addAttackSteps(as1, as2, as3, as4, as5, as6);

      OutputUtils.plotOn();
      OutputUtils.mathematicaPlot(graph, 1);
      assertTrue(!graph.areProgeniesUpToDate());

      graph.computeAllProgenies();
      assertTrue(graph.areProgeniesUpToDate());
      assertTrue(as1.getDescendants().contains(as2));
      assertTrue(as1.getDescendants().contains(as3));
      assertTrue(as1.getDescendants().contains(as4));
      assertTrue(as1.getDescendants().contains(as5));
      assertTrue(as1.getDescendants().contains(as6));
      assertTrue(as5.getDescendants().contains(as2));
      assertTrue(as5.getDescendants().contains(as2));
      assertTrue(as5.getDescendants().contains(as3));
      assertTrue(as5.getDescendants().contains(as4));
      assertTrue(as5.getDescendants().contains(as6));
      assertFalse(as5.getDescendants().contains(as1));
      assertFalse(as4.getDescendants().contains(as1));
      assertFalse(as3.getDescendants().contains(as1));
      assertFalse(as2.getDescendants().contains(as1));
      assertFalse(as6.getDescendants().contains(as1));
      assertTrue(as2.getDescendants().contains(as3));
      assertTrue(as2.getDescendants().contains(as4));
      assertTrue(as2.getDescendants().contains(as5));
      assertTrue(as2.getDescendants().contains(as6));
      assertTrue(as4.getDescendants().contains(as2));
      assertTrue(as4.getDescendants().contains(as3));
      assertTrue(as4.getDescendants().contains(as5));
      assertTrue(as4.getDescendants().contains(as6));
      assertTrue(as3.getDescendants().contains(as2));
      assertTrue(as3.getDescendants().contains(as4));
      assertTrue(as3.getDescendants().contains(as5));
      assertTrue(as3.getDescendants().contains(as6));
   }

   @Ignore
   @Test
   public void testProgeniesAncestorsCycle() throws Exception {
      Graph graph = new Graph("LooseGraph");
      AttackStep as1 = new AttackStepMin("as1", new ConstantRealDistribution(0), Order.ENTRYSTEP);
      AttackStep as2 = new AttackStepMin("as2", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep as3 = new AttackStepMin("as3", new ConstantRealDistribution(2), Order.MIDSTEP);
      AttackStep as4 = new AttackStepMin("as4", new ConstantRealDistribution(0), Order.MIDSTEP);
      AttackStep as5 = new AttackStepMin("as5", new ConstantRealDistribution(0), Order.MIDSTEP);
      AttackStep as6 = new AttackStepMin("as6", new ConstantRealDistribution(0), Order.EXITSTEP);

      as1.connectToChild(as2);
      as2.connectToChild(as3);
      as3.connectToChild(as4);
      as4.connectToChild(as5);
      as5.connectToChild(as2);
      as5.connectToChild(as6);
      graph.addAttackSteps(as1, as2, as3, as4, as5, as6);

      OutputUtils.plotOn();
      OutputUtils.mathematicaPlot(graph,1);
      assertTrue(!graph.areProgeniesUpToDate());

      graph.updateRoots();
      graph.updateLeaves();

      graph.computeProgenies2();
      assertTrue(graph.areProgeniesUpToDate());
      assertTrue(as1.getDescendants().contains(as2));
      assertTrue(as1.getDescendants().contains(as3));
      assertTrue(as1.getDescendants().contains(as4));
      assertTrue(as1.getDescendants().contains(as5));
      assertTrue(as1.getDescendants().contains(as6));
      assertTrue(as5.getDescendants().contains(as2));
      assertTrue(as5.getDescendants().contains(as2));
      assertTrue(as5.getDescendants().contains(as3));
      assertTrue(as5.getDescendants().contains(as4));
      assertTrue(as5.getDescendants().contains(as6));
      assertFalse(as5.getDescendants().contains(as1));
      assertFalse(as4.getDescendants().contains(as1));
      assertFalse(as3.getDescendants().contains(as1));
      assertFalse(as2.getDescendants().contains(as1));
      assertFalse(as6.getDescendants().contains(as1));
      assertTrue(as2.getDescendants().contains(as3));
      assertTrue(as2.getDescendants().contains(as4));
      assertTrue(as2.getDescendants().contains(as5));
      assertTrue(as2.getDescendants().contains(as6));
      assertTrue(as4.getDescendants().contains(as2));
      assertTrue(as4.getDescendants().contains(as3));
      assertTrue(as4.getDescendants().contains(as5));
      assertTrue(as4.getDescendants().contains(as6));
      assertTrue(as3.getDescendants().contains(as2));
      assertTrue(as3.getDescendants().contains(as4));
      assertTrue(as3.getDescendants().contains(as5));
      assertTrue(as3.getDescendants().contains(as6));

      graph.computeAncestries2();
      assertFalse(as1.getAncestorsR().contains(as2));
      assertFalse(as1.getAncestorsR().contains(as3));
      assertFalse(as1.getAncestorsR().contains(as4));
      assertFalse(as1.getAncestorsR().contains(as5));
      assertFalse(as1.getAncestorsR().contains(as6));
      assertTrue(as2.getAncestorsR().contains(as1));
      assertTrue(as2.getAncestorsR().contains(as3));
      assertTrue(as2.getAncestorsR().contains(as4));
      assertTrue(as2.getAncestorsR().contains(as5));
      assertFalse(as2.getAncestorsR().contains(as6));
      assertTrue(as3.getAncestorsR().contains(as1));
      assertTrue(as3.getAncestorsR().contains(as2));
      assertTrue(as3.getAncestorsR().contains(as4));
      assertTrue(as3.getAncestorsR().contains(as5));
      assertFalse(as3.getAncestorsR().contains(as6));
      assertTrue(as4.getAncestorsR().contains( as1));
      assertTrue(as4.getAncestorsR().contains(as2));
      assertTrue(as4.getAncestorsR().contains(as3));
      assertTrue(as4.getAncestorsR().contains(as5));
      assertFalse(as4.getAncestorsR().contains(as6));
      assertTrue(as5.getAncestorsR().contains(as2));
      assertTrue(as5.getAncestorsR().contains(as3));
      assertTrue(as5.getAncestorsR().contains(as4));
      assertFalse(as5.getAncestorsR().contains(as6));
      assertTrue(as6.getAncestorsR().contains(as1));
      assertTrue(as6.getAncestorsR().contains(as2));
      assertTrue(as6.getAncestorsR().contains(as3));
      assertTrue(as6.getAncestorsR().contains(as4));
      assertTrue(as6.getAncestorsR().contains(as5));
   }
     */

}
