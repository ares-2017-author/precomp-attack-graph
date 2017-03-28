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

public class DFSTest {

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

        Set<AttackStep> ancestorsD = DFS.depthFirstAncestorsOf(d);

        assertFalse(ancestorsD.contains(d));
        assertTrue(ancestorsD.contains(b));
        assertTrue(ancestorsD.contains(c));
        assertTrue(ancestorsD.contains(a));
        assertFalse(ancestorsD.contains(e));
        assertFalse(ancestorsD.contains(f));
    }

    @Test
    public void testAncestors1To(){
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

        Set<AttackStep> ancestorsDtoA = DFS.depthFirstAncestorsTo(d,a);

        assertFalse(ancestorsDtoA.contains(d));
        assertTrue(ancestorsDtoA.contains(b));
        assertTrue(ancestorsDtoA.contains(c));
        assertTrue(ancestorsDtoA.contains(a));
        assertFalse(ancestorsDtoA.contains(e));
        assertFalse(ancestorsDtoA.contains(f));
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

        Set<AttackStep> descendantsOfA = DFS.depthFirstDescendantsOf(a);

        assertFalse(descendantsOfA.contains(a));
        assertTrue(descendantsOfA.contains(b));
        assertTrue(descendantsOfA.contains(c));
        assertTrue(descendantsOfA.contains(d));
        assertTrue(descendantsOfA.contains(e));
        assertTrue(descendantsOfA.contains(f));
    }

    @Test
    public void testDescendantsTo(){
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

        Set<AttackStep> progenyAtoD = DFS.depthFirstDescendantsTo(a,d);

        assertFalse(progenyAtoD.contains(a));
        assertTrue(progenyAtoD.contains(b));
        assertTrue(progenyAtoD.contains(c));
        assertTrue(progenyAtoD.contains(d));
        assertFalse(progenyAtoD.contains(e));
        assertFalse(progenyAtoD.contains(f));
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

        Set<AttackStep> ancestorsJ = DFS.depthFirstAncestorsOf(j);
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
    public void testAncestorsToBis(){
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

        Set<AttackStep> ancestorsJtoA = DFS.depthFirstAncestorsTo(j,a);
        System.out.println(ancestorsJtoA.stream().map(AttackStep::getName).collect(Collectors.joining("; ")));

        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(gr,2);

        assertTrue(ancestorsJtoA.contains(d));
        assertTrue(ancestorsJtoA.contains(b));
        assertTrue(ancestorsJtoA.contains(c));
        assertTrue(ancestorsJtoA.contains(a));
        assertTrue(ancestorsJtoA.contains(e));
        assertTrue(ancestorsJtoA.contains(g));
        assertTrue(ancestorsJtoA.contains(h));
        assertFalse(ancestorsJtoA.contains(j));
        assertFalse(ancestorsJtoA.contains(f));
        assertFalse(ancestorsJtoA.contains(i));
        assertFalse(ancestorsJtoA.contains(k));
    }


    @Test
    public void testAncestorsTo2(){
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

        Set<AttackStep> ancestorsJtoA = DFS.depthFirstAncestorsTo2(j,a);
        System.out.println(ancestorsJtoA.stream().map(AttackStep::getName).collect(Collectors.joining("; ")));

        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(gr,2);

        assertTrue(ancestorsJtoA.contains(d));
        assertTrue(ancestorsJtoA.contains(b));
        assertTrue(ancestorsJtoA.contains(c));
        assertFalse(ancestorsJtoA.contains(a));
        assertTrue(ancestorsJtoA.contains(e));
        assertTrue(ancestorsJtoA.contains(g));
        assertTrue(ancestorsJtoA.contains(h));
        assertFalse(ancestorsJtoA.contains(j));
        assertFalse(ancestorsJtoA.contains(f));
        assertFalse(ancestorsJtoA.contains(i));
        assertFalse(ancestorsJtoA.contains(k));
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

        Set<AttackStep> descendantsA = DFS.depthFirstDescendantsOf(a);
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
    public void testDescendantsTo2(){
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

        Set<AttackStep> descendantsAtoJ = DFS.depthFirstDescendantsTo(a,j);
        System.out.println(descendantsAtoJ.stream().map(AttackStep::getName).collect(Collectors.joining("; ")));

        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(gr,2);

        assertFalse(descendantsAtoJ.contains(a));
        assertTrue(descendantsAtoJ.contains(b));
        assertTrue(descendantsAtoJ.contains(c));
        assertTrue(descendantsAtoJ.contains(d));
        assertTrue(descendantsAtoJ.contains(e));
        assertTrue(descendantsAtoJ.contains(g));
        assertTrue(descendantsAtoJ.contains(h));
        assertTrue(descendantsAtoJ.contains(j));
        assertFalse(descendantsAtoJ.contains(f));
        assertFalse(descendantsAtoJ.contains(i));
        assertFalse(descendantsAtoJ.contains(k));
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

        Set<AttackStep> ancestorsAS6 = DFS.depthFirstAncestorsOf(as6);
        Set<AttackStep> ancestorsAS2 = DFS.depthFirstAncestorsOf(as2);
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

        Set<AttackStep> progenyAS1 = DFS.depthFirstDescendantsOf(as1);
        Set<AttackStep> progenyAS3 = DFS.depthFirstDescendantsOf(as3);
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

    @Test
    public void ancestorsLoopTo() {
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

        Set<AttackStep> ancestorsAS5toAS2 = DFS.depthFirstAncestorsTo(as5,as2);
        Set<AttackStep> ancestorsAS6toAS3 = DFS.depthFirstAncestorsTo(as6,as3);
        Set<AttackStep> ancestorsAS6toAS1 = DFS.depthFirstAncestorsTo(as6,as1);
        Set<AttackStep> ancestorsAS4toAS1 = DFS.depthFirstAncestorsTo(as4,as1);

        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(gr,2);

        assertFalse(ancestorsAS5toAS2.contains(as5));
        assertTrue(ancestorsAS5toAS2.contains(as4));
        assertTrue(ancestorsAS5toAS2.contains(as3));
        assertTrue(ancestorsAS5toAS2.contains(as2));
        assertFalse(ancestorsAS5toAS2.contains(as1));
        assertFalse(ancestorsAS5toAS2.contains(as6));

        assertTrue(ancestorsAS6toAS1.contains(as5));
        assertTrue(ancestorsAS6toAS1.contains(as4));
        assertTrue(ancestorsAS6toAS1.contains(as3));
        assertTrue(ancestorsAS6toAS1.contains(as1));
        assertTrue(ancestorsAS6toAS1.contains(as2));
        assertFalse(ancestorsAS6toAS1.contains(as6));

        assertFalse(ancestorsAS4toAS1.contains(as5));
        assertFalse(ancestorsAS4toAS1.contains(as4));
        assertTrue(ancestorsAS4toAS1.contains(as3));
        assertTrue(ancestorsAS4toAS1.contains(as1));
        assertTrue(ancestorsAS4toAS1.contains(as2));
        assertFalse(ancestorsAS4toAS1.contains(as6));

        assertTrue(ancestorsAS6toAS3.contains(as5));
        assertTrue(ancestorsAS6toAS3.contains(as4));
        assertTrue(ancestorsAS6toAS3.contains(as3));
        assertFalse(ancestorsAS6toAS3.contains(as1));
        assertFalse(ancestorsAS6toAS3.contains(as2));
        assertFalse(ancestorsAS6toAS3.contains(as6));
    }


    @Test
    public void ancestorsLoopTo2() {
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

        Set<AttackStep> ancestorsAS5toAS2 = DFS.depthFirstAncestorsTo2(as5,as2);
        Set<AttackStep> ancestorsAS6toAS3 = DFS.depthFirstAncestorsTo2(as6,as3);
        Set<AttackStep> ancestorsAS6toAS1 = DFS.depthFirstAncestorsTo2(as6,as1);
        Set<AttackStep> ancestorsAS4toAS1 = DFS.depthFirstAncestorsTo2(as4,as1);

        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(gr,2);

        assertFalse(ancestorsAS5toAS2.contains(as5));
        assertTrue(ancestorsAS5toAS2.contains(as4));
        assertTrue(ancestorsAS5toAS2.contains(as3));
        assertFalse(ancestorsAS5toAS2.contains(as2));
        assertFalse(ancestorsAS5toAS2.contains(as1));
        assertFalse(ancestorsAS5toAS2.contains(as6));

        assertTrue(ancestorsAS6toAS1.contains(as5));
        assertTrue(ancestorsAS6toAS1.contains(as4));
        assertTrue(ancestorsAS6toAS1.contains(as3));
        assertFalse(ancestorsAS6toAS1.contains(as1));
        assertTrue(ancestorsAS6toAS1.contains(as2));
        assertFalse(ancestorsAS6toAS1.contains(as6));

        assertFalse(ancestorsAS4toAS1.contains(as5));
        assertFalse(ancestorsAS4toAS1.contains(as4));
        assertTrue(ancestorsAS4toAS1.contains(as3));
        assertFalse(ancestorsAS4toAS1.contains(as1));
        assertTrue(ancestorsAS4toAS1.contains(as2));
        assertFalse(ancestorsAS4toAS1.contains(as6));

        assertTrue(ancestorsAS6toAS3.contains(as5));
        assertTrue(ancestorsAS6toAS3.contains(as4));
        assertFalse(ancestorsAS6toAS3.contains(as3));
        assertFalse(ancestorsAS6toAS3.contains(as1));
        assertFalse(ancestorsAS6toAS3.contains(as2));
        assertFalse(ancestorsAS6toAS3.contains(as6));
    }

    @Test
    public void ProgenyLoopTo() {
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

        Set<AttackStep> progenyAS1 = DFS.depthFirstDescendantsOf(as1);
        Set<AttackStep> progenyAS3 = DFS.depthFirstDescendantsOf(as3);
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

    @Test
    public void ProgenyLoopToBis() throws Exception{
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

        Set<AttackStep> descendantsFromAtoE = DFS.depthFirstDescendantsTo(a,e);

        assertFalse(descendantsFromAtoE.contains(a));
        assertTrue(descendantsFromAtoE.contains(e));
        assertFalse(descendantsFromAtoE.contains(b));
        assertFalse(descendantsFromAtoE.contains(c));
        assertFalse(descendantsFromAtoE.contains(d));
    }
}
