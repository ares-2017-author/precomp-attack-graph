package attackgraph;

import datatypes.Order;
import org.apache.commons.math3.distribution.ConstantRealDistribution;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import support.OutputUtils;
import support.TestDistribution;
import support.TestUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttackStepTest {

    @Test
    public void samplingTest() {
        OutputUtils.veryVerboseOn();
        AttackStepMin as = new AttackStepMin("testAttackStep", new TestDistribution(0.8, 10, 1), Order.MIDSTEP);
        as.sample();
        double localTtc1 = as.getLocalTtc();
        as.sample();
        double localTtc2 = as.getLocalTtc();
        OutputUtils.printVeryVerbose("Sample 1 = " + localTtc1);
        OutputUtils.printVeryVerbose("Sample 2 = " + localTtc2);
        assertTrue(localTtc1 != localTtc2);

        OutputUtils.verboseOff();
    }

    @Test
    public void cloneTest() {
        AttackStepMin a = new AttackStepMin("testAttackStep", new TestDistribution(0.5, 10, 1), Order.MIDSTEP);
        AttackStep b = TestUtils.cloneAttackStep(a);
        assertTrue(b instanceof AttackStepMin);
        assertTrue(b.getName().equals(a.getName()));
        assertTrue(b.getLocalTtc() == a.getLocalTtc());
    }

    @Test
    public void testProgeny() {
        // OutputUtils.verboseOn();
        Graph graph = TestUtils.generateRandomGraph(3, 3, 16, 2, 0.5, 0, 0.5, 0.5);
        OutputUtils.mathematicaPlot(graph, 2);
        OutputUtils.printVerbose("Progeny is comprised of:");
        AttackStep source = graph.getEntrySteps().get(0);
        for (AttackStep p : source.getProgenyI()) {
            OutputUtils.printVerbose(p.getName());
            assertTrue(p.getAncestors().contains(source));
        }
    }

    @Test
    public void testAncestorsToAndProgenyTo() {
        AttackStep a = new AttackStepMin("a", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep b = new AttackStepMin("b", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep c = new AttackStepMin("c", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep d = new AttackStepMin("d", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep e = new AttackStepMin("e", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep f = new AttackStepMin("f", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);

        a.connectToChild(b);
        b.connectToChild(c);
        c.connectToChild(d);
        a.connectToChild(e);
        e.connectToChild(f);

        assertTrue(d.ancestorsTo(a).contains(b));
        assertTrue(d.ancestorsTo(a).contains(c));
        assertTrue(d.ancestorsTo(a).contains(a));
        assertFalse(d.ancestorsTo(a).contains(e));
        assertFalse(d.ancestorsTo(a).contains(f));
        assertFalse(d.ancestorsTo(b).contains(a));
        assertTrue(d.ancestorsTo(a).contains(a));

        assertTrue(a.descendantsTo(c).contains(b));
        assertTrue(a.descendantsTo(c).contains(c));
        assertFalse(a.descendantsTo(c).contains(d));
        assertFalse(a.descendantsTo(c).contains(e));

    }

    @Test
    public void testRecursiveDescendantsCycle() throws Exception {
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

        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph, 1);

        Set<AttackStep> as1Descendants = as1.getProgenyI();
        Set<AttackStep> as2Descendants = as2.getProgenyI();
        Set<AttackStep> as3Descendants = as3.getProgenyI();
        Set<AttackStep> as4Descendants = as4.getProgenyI();
        Set<AttackStep> as5Descendants = as5.getProgenyI();
        Set<AttackStep> as6Descendants = as6.getProgenyI();

        assertTrue(as1Descendants.contains(as2));
        assertTrue(as1Descendants.contains(as3));
        assertTrue(as1Descendants.contains(as4));
        assertTrue(as1Descendants.contains(as5));
        assertTrue(as1Descendants.contains(as6));
        assertTrue(as5Descendants.contains(as2));
        assertTrue(as5Descendants.contains(as2));
        assertTrue(as5Descendants.contains(as3));
        assertTrue(as5Descendants.contains(as4));
        assertTrue(as5Descendants.contains(as6));
        assertFalse(as5Descendants.contains(as1));
        assertFalse(as4Descendants.contains(as1));
        assertFalse(as3Descendants.contains(as1));
        assertFalse(as2Descendants.contains(as1));
        assertFalse(as6Descendants.contains(as1));
        assertTrue(as2Descendants.contains(as3));
        assertTrue(as2Descendants.contains(as4));
        assertTrue(as2Descendants.contains(as5));
        assertTrue(as2Descendants.contains(as6));
        assertTrue(as4Descendants.contains(as2));
        assertTrue(as4Descendants.contains(as3));
        assertTrue(as4Descendants.contains(as5));
        assertTrue(as4Descendants.contains(as6));
        assertTrue(as3Descendants.contains(as2));
        assertTrue(as3Descendants.contains(as4));
        assertTrue(as3Descendants.contains(as5));
        assertTrue(as3Descendants.contains(as6));
    }

    @Test
    public void testRecursiveDescendantsDoubleCycle() throws Exception {
        Graph graph = new Graph("LooseGraph");
        AttackStep as1 = new AttackStepMin("as1", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep as2 = new AttackStepMin("as2", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as3 = new AttackStepMin("as3", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as4 = new AttackStepMin("as4", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep as5 = new AttackStepMin("as5", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep as6 = new AttackStepMin("as6", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep as7 = new AttackStepMin("as7", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep as8 = new AttackStepMin("as8", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep as9 = new AttackStepMin("as9", new ConstantRealDistribution(0), Order.EXITSTEP);

        as1.connectToChild(as2);
        as2.connectToChild(as3);
        as3.connectToChild(as4);
        as4.connectToChild(as5);
        as5.connectToChild(as2);
        as2.connectToChild(as6);
        as6.connectToChild(as7);
        as7.connectToChild(as8);
        as8.connectToChild(as4);
        as5.connectToChild(as9);

        graph.addAttackSteps(as1,as2,as3,as4,as5,as6,as7,as8,as9);

        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph, 1);

        Set<AttackStep> as1Descendants = as1.getProgenyI();
        Set<AttackStep> as2Descendants = as2.getProgenyI();
        Set<AttackStep> as3Descendants = as3.getProgenyI();
        Set<AttackStep> as4Descendants = as4.getProgenyI();
        Set<AttackStep> as5Descendants = as5.getProgenyI();
        Set<AttackStep> as6Descendants = as6.getProgenyI();
        Set<AttackStep> as7Descendants = as7.getProgenyI();
        Set<AttackStep> as8Descendants = as8.getProgenyI();
        Set<AttackStep> as9Descendants = as9.getProgenyI();

        assertTrue(as1Descendants.contains(as2));
        assertTrue(as1Descendants.contains(as3));
        assertTrue(as1Descendants.contains(as4));
        assertTrue(as1Descendants.contains(as5));
        assertTrue(as1Descendants.contains(as6));
        assertTrue(as1Descendants.contains(as7));
        assertTrue(as1Descendants.contains(as8));
        assertTrue(as1Descendants.contains(as9));
        assertTrue(as5Descendants.contains(as2));
        assertTrue(as5Descendants.contains(as2));
        assertTrue(as5Descendants.contains(as3));
        assertTrue(as5Descendants.contains(as4));
        assertTrue(as5Descendants.contains(as6));
        assertTrue(as5Descendants.contains(as7));
        assertTrue(as5Descendants.contains(as8));
        assertTrue(as5Descendants.contains(as9));
        assertFalse(as5Descendants.contains(as1));
        assertFalse(as4Descendants.contains(as1));
        assertFalse(as3Descendants.contains(as1));
        assertFalse(as2Descendants.contains(as1));
        assertFalse(as6Descendants.contains(as1));
        assertFalse(as7Descendants.contains(as1));
        assertFalse(as8Descendants.contains(as1));
        assertFalse(as9Descendants.contains(as1));
        assertTrue(as2Descendants.contains(as3));
        assertTrue(as2Descendants.contains(as4));
        assertTrue(as2Descendants.contains(as5));
        assertTrue(as2Descendants.contains(as6));
        assertTrue(as2Descendants.contains(as7));
        assertTrue(as2Descendants.contains(as8));
        assertTrue(as2Descendants.contains(as9));
        assertTrue(as4Descendants.contains(as2));
        assertTrue(as4Descendants.contains(as3));
        assertTrue(as4Descendants.contains(as5));
        assertTrue(as4Descendants.contains(as6));
        assertTrue(as4Descendants.contains(as7));
        assertTrue(as4Descendants.contains(as8));
        assertTrue(as4Descendants.contains(as9));
        assertTrue(as3Descendants.contains(as2));
        assertTrue(as3Descendants.contains(as4));
        assertTrue(as3Descendants.contains(as5));
        assertTrue(as3Descendants.contains(as6));
        assertTrue(as3Descendants.contains(as7));
        assertTrue(as3Descendants.contains(as8));
        assertTrue(as3Descendants.contains(as9));
        assertTrue(as7Descendants.contains(as2));
        assertTrue(as7Descendants.contains(as4));
        assertTrue(as7Descendants.contains(as5));
        assertTrue(as7Descendants.contains(as6));
        assertTrue(as7Descendants.contains(as9));
        assertTrue(as7Descendants.contains(as8));
        assertTrue(as7Descendants.contains(as3));
        assertTrue(as8Descendants.contains(as2));
        assertTrue(as8Descendants.contains(as4));
        assertTrue(as8Descendants.contains(as5));
        assertTrue(as8Descendants.contains(as6));
        assertTrue(as8Descendants.contains(as9));
        assertTrue(as8Descendants.contains(as7));
        assertTrue(as8Descendants.contains(as3));
        assertFalse(as9Descendants.contains(as2));
        assertFalse(as9Descendants.contains(as4));
        assertFalse(as9Descendants.contains(as5));
        assertFalse(as9Descendants.contains(as6));
        assertFalse(as9Descendants.contains(as7));
        assertFalse(as9Descendants.contains(as8));
        assertFalse(as9Descendants.contains(as3));
    }

    // TODO fix this test case
    @Ignore
    @Test
    public void testAncestors() {
        OutputUtils.veryVerboseOn();
        Graph graph = TestUtils.generateRandomGraph(1, 1, 6, 2, 0.5, 0, 0.5, 0, 0, 0, 0, 0.5);
        Set<AttackStep> ancestors = graph.getExitSteps().get(0).getAncestors();
        Set<String> actualAncestorNames = new HashSet<>();
        for (AttackStep ancestor : ancestors) {
            actualAncestorNames.add(ancestor.getName());
        }
        Set<String> expectedAncestorNames = new HashSet<>(Arrays.asList("max7", "min4", "max6", "max2", "entryStep1"));
        Set<String> expectedUnrelatedNames = new HashSet<>();
        for (AttackStep step : graph.attackStepsAsSet()) {
            if (!expectedAncestorNames.contains(step.getName())) {
                expectedUnrelatedNames.add(step.getName());
            }
        }
        OutputUtils.printVeryVerbose("Expected ancestor names");
        for (String ean : expectedAncestorNames) {
            OutputUtils.printVeryVerbose(ean);
        }
        OutputUtils.printVeryVerbose("Actual ancestor names");
        for (String aan : actualAncestorNames) {
            OutputUtils.printVeryVerbose(aan);
        }
        assertTrue(actualAncestorNames.equals(expectedAncestorNames));
        assertFalse(expectedUnrelatedNames.contains(actualAncestorNames));
    }

    @After
    public void close() {
        OutputUtils.verboseOff();
    }

}
