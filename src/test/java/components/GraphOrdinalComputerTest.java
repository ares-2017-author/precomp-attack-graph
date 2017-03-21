package components;


import attackGraph.*;
import datatypes.OrdinalTtcValue;
import org.apache.commons.math3.distribution.ConstantRealDistribution;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import support.OutputUtils;

import static org.junit.Assert.assertTrue;

public class GraphOrdinalComputerTest {
    @Test
    public void computeTestSimple() throws Exception{
        OutputUtils.plotOn();
        Graph graph = new Graph("LooseGraph");
        AttackStep as1 = new AttackStepMin("as1", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep as2 = new AttackStepMin("as2", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as3 = new AttackStepMin("as3", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as4 = new AttackStepMin("as4", new ConstantRealDistribution(0), Order.EXITSTEP);

        as1.connectToChild(as2);
        as1.connectToChild(as3);
        as2.connectToChild(as4);
        as3.connectToChild(as4);
        graph.addAttackStep(as1);
        graph.addAttackStep(as2);
        graph.addAttackStep(as3);
        graph.addAttackStep(as4);
        graph.sample();

        assertTrue(as1.getChildren().contains(as2));
        assertTrue(as2.getChildren().contains(as4));
        assertTrue(as1.getChildren().contains(as3));

        graph.ordinalCompute(as1);

    }

    @Test
    public void computeTestSimpleWithHardCodedOrdinalsExitMax() throws Exception{
        Graph graph = new Graph("LooseGraph");
        AttackStep as1 = new AttackStepMin("as1", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep as2 = new AttackStepMin("as2", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as3 = new AttackStepMin("as3", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as4 = new AttackStepMax("as4", new ConstantRealDistribution(0), Order.EXITSTEP);

        as1.connectToChild(as2);
        as1.connectToChild(as3);
        as2.connectToChild(as4);
        as3.connectToChild(as4);
        graph.addAttackStep(as1);
        graph.addAttackStep(as2);
        graph.addAttackStep(as3);
        graph.addAttackStep(as4);
        //    graph.sample();

        as1.setOrdinalLocalTtc(OrdinalTtcValue.SOURCE);
        as2.setOrdinalLocalTtc(OrdinalTtcValue.ZERO);
        as3.setOrdinalLocalTtc(OrdinalTtcValue.ANY);
        as4.setOrdinalLocalTtc(OrdinalTtcValue.ANY);

        assertTrue(as1.getChildren().contains(as2));
        assertTrue(as2.getChildren().contains(as4));
        assertTrue(as1.getChildren().contains(as3));

        graph.ordinalCompute(as1);
        assertTrue(as1.getOrdinalTtc().equals(OrdinalTtcValue.SOURCE));
        assertTrue(as2.getOrdinalTtc().equals(OrdinalTtcValue.SOURCE));
        assertTrue(as3.getOrdinalTtc().equals(OrdinalTtcValue.GTESOURCE));
        assertTrue(as4.getOrdinalTtc().equals(OrdinalTtcValue.GTESOURCE));
    }

    @Test
    public void computeTestSimpleWithHardCodedOrdinalsExitMin() throws Exception{
        Graph graph = new Graph("LooseGraph");
        AttackStep as1 = new AttackStepMin("as1", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep as2 = new AttackStepMin("as2", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as3 = new AttackStepMin("as3", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as4 = new AttackStepMin("as4", new ConstantRealDistribution(0), Order.EXITSTEP);

        as1.connectToChild(as2);
        as1.connectToChild(as3);
        as2.connectToChild(as4);
        as3.connectToChild(as4);
        graph.addAttackStep(as1);
        graph.addAttackStep(as2);
        graph.addAttackStep(as3);
        graph.addAttackStep(as4);
        //    graph.sample();

        as1.setOrdinalLocalTtc(OrdinalTtcValue.SOURCE);
        as2.setOrdinalLocalTtc(OrdinalTtcValue.ZERO);
        as3.setOrdinalLocalTtc(OrdinalTtcValue.ANY);
        as4.setOrdinalLocalTtc(OrdinalTtcValue.ANY);

        assertTrue(as1.getChildren().contains(as2));
        assertTrue(as2.getChildren().contains(as4));
        assertTrue(as1.getChildren().contains(as3));

        OutputUtils.plotOn();
        graph.ordinalCompute(as1);
        OutputUtils.mathematicaPlot(graph,5);
        assertTrue(as1.getOrdinalTtc().equals(OrdinalTtcValue.SOURCE));
        assertTrue(as2.getOrdinalTtc().equals(OrdinalTtcValue.SOURCE));
        assertTrue(as3.getOrdinalTtc().equals(OrdinalTtcValue.GTESOURCE));
        assertTrue(as4.getOrdinalTtc().equals(OrdinalTtcValue.GTESOURCE));
    }

    @Test
    public void computeTestComplexWithHardCodedOrdinals() throws Exception{
        Graph graph = new Graph("LooseGraph");
        AttackStep as1 = new AttackStepMin("as1", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep as2 = new AttackStepMin("as2", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as3 = new AttackStepMax("as3", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as4 = new AttackStepMin("as4", new ConstantRealDistribution(0), Order.MIDSTEP);
        AttackStep as5 = new AttackStepMax("as5", new ConstantRealDistribution(1), Order.MIDSTEP);
        AttackStep as6 = new AttackStepMin("as6", new ConstantRealDistribution(3), Order.EXITSTEP);
        AttackStep as7 = new AttackStepMax("as7", new ConstantRealDistribution(4), Order.EXITSTEP);

        as1.connectToChild(as2);
        as1.connectToChild(as3);
        as2.connectToChild(as3);
        as2.connectToChild(as4);
        as3.connectToChild(as4);
        as3.connectToChild(as5);
        as4.connectToChild(as5);
        as4.connectToChild(as6);
        as4.connectToChild(as7);
        as5.connectToChild(as7);
        graph.addAttackStep(as1);
        graph.addAttackStep(as2);
        graph.addAttackStep(as3);
        graph.addAttackStep(as4);
        graph.addAttackStep(as5);
        graph.addAttackStep(as6);
        graph.addAttackStep(as7);
        //    graph.sample();

        as1.setOrdinalLocalTtc(OrdinalTtcValue.SOURCE);
        as2.setOrdinalLocalTtc(OrdinalTtcValue.ZERO);
        as3.setOrdinalLocalTtc(OrdinalTtcValue.ANY);
        as4.setOrdinalLocalTtc(OrdinalTtcValue.ZERO);
        as5.setOrdinalLocalTtc(OrdinalTtcValue.ANY);
        as6.setOrdinalLocalTtc(OrdinalTtcValue.ZERO);
        as7.setOrdinalLocalTtc(OrdinalTtcValue.ANY);

        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph,5);

        graph.ordinalCompute(as1);
        OutputUtils.mathematicaPlot(graph,5);
        assertTrue(as1.getOrdinalTtc().equals(OrdinalTtcValue.SOURCE));
        assertTrue(as2.getOrdinalTtc().equals(OrdinalTtcValue.SOURCE));
        assertTrue(as3.getOrdinalTtc().equals(OrdinalTtcValue.GTESOURCE));
        assertTrue(as4.getOrdinalTtc().equals(OrdinalTtcValue.SOURCE));
        assertTrue(as5.getOrdinalTtc().equals(OrdinalTtcValue.GTESOURCE));
        assertTrue(as6.getOrdinalTtc().equals(OrdinalTtcValue.SOURCE));
        assertTrue(as7.getOrdinalTtc().equals(OrdinalTtcValue.GTESOURCE));
    }

    @Ignore
    @Test
    public void computewithSampling() throws Exception{
        Graph graph = new Graph("LooseGraph");
        AttackStep as1 = new AttackStepMin("as1", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep as8 = new AttackStepMin("as8", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep as2 = new AttackStepMin("as2", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as3 = new AttackStepMax("as3", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as4 = new AttackStepMin("as4", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as5 = new AttackStepMax("as5", new ConstantRealDistribution(1), Order.MIDSTEP);
        AttackStep as6 = new AttackStepMin("as6", new ConstantRealDistribution(3), Order.EXITSTEP);
        AttackStep as7 = new AttackStepMax("as7", new ConstantRealDistribution(4), Order.EXITSTEP);

        as1.connectToChild(as2);
        as1.connectToChild(as3);
        as2.connectToChild(as3);
        as2.connectToChild(as4);
        as3.connectToChild(as4);
        as3.connectToChild(as5);
        as4.connectToChild(as5);
        as4.connectToChild(as6);
        as4.connectToChild(as7);
        as5.connectToChild(as7);
        as8.connectToChild(as4);
        graph.addAttackSteps(as1,as2,as3,as4,as5,as6,as7,as8);
        graph.hardReset();
        graph.sample();

        graph.ordinalCompute(as1);
        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph,5);
        assertTrue(as1.getOrdinalTtc().equals(OrdinalTtcValue.SOURCE));
        assertTrue(as2.getOrdinalTtc().equals(OrdinalTtcValue.GTESOURCE));
        assertTrue(as3.getOrdinalTtc().equals(OrdinalTtcValue.GTESOURCE));
        assertTrue(as4.getOrdinalTtc().equals(OrdinalTtcValue.ANY));
        assertTrue(as5.getOrdinalTtc().equals(OrdinalTtcValue.GTESOURCE));
        assertTrue(as6.getOrdinalTtc().equals(OrdinalTtcValue.SOURCE));
        assertTrue(as7.getOrdinalTtc().equals(OrdinalTtcValue.GTESOURCE));
    }

    @Test
    public void computeTestComplexWithHardCodedOrdinalsDefenses() throws Exception{
        Graph graph = new Graph("LooseGraph");
        AttackStep as1 = new AttackStepMin("as1", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep as2 = new AttackStepMin("as2", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as3 = new AttackStepMax("as3", new ConstantRealDistribution(2), Order.MIDSTEP);
        AttackStep as4 = new Defense("def1", true,false);
        AttackStep as5 = new AttackStepMax("as5", new ConstantRealDistribution(1), Order.MIDSTEP);
        AttackStep as6 = new AttackStepMin("as6", new ConstantRealDistribution(3), Order.EXITSTEP);
        AttackStep as7 = new AttackStepMax("as7", new ConstantRealDistribution(4), Order.EXITSTEP);

        as1.connectToChild(as2);
        as1.connectToChild(as3);
        as2.connectToChild(as3);
        as2.connectToChild(as4);
        as3.connectToChild(as4);
        as3.connectToChild(as5);
        as4.connectToChild(as5);
        as4.connectToChild(as6);
        as4.connectToChild(as7);
        as5.connectToChild(as7);
        graph.addAttackSteps(as1,as2,as3,as4,as5,as6,as7);
        graph.hardReset();
        //    graph.sample();

        as1.setOrdinalLocalTtc(OrdinalTtcValue.SOURCE);
        as2.setOrdinalLocalTtc(OrdinalTtcValue.ZERO);
        as3.setOrdinalLocalTtc(OrdinalTtcValue.ANY);
        as4.setOrdinalLocalTtc(OrdinalTtcValue.ZERO);
        as5.setOrdinalLocalTtc(OrdinalTtcValue.ANY);
        as6.setOrdinalLocalTtc(OrdinalTtcValue.ZERO);
        as7.setOrdinalLocalTtc(OrdinalTtcValue.ANY);

        graph.ordinalCompute(as1);
        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph,5);
        assertTrue(as1.getOrdinalTtc().equals(OrdinalTtcValue.SOURCE));
        assertTrue(as2.getOrdinalTtc().equals(OrdinalTtcValue.SOURCE));
        assertTrue(as3.getOrdinalTtc().equals(OrdinalTtcValue.GTESOURCE));
        assertTrue(as4.getOrdinalTtc().equals(OrdinalTtcValue.SOURCE));
        assertTrue(as5.getOrdinalTtc().equals(OrdinalTtcValue.GTESOURCE));
        assertTrue(as6.getOrdinalTtc().equals(OrdinalTtcValue.SOURCE));
        assertTrue(as7.getOrdinalTtc().equals(OrdinalTtcValue.GTESOURCE));
    }

    @Test
    public void computeTestSimpleAgentWithHardCodedOrdinals() throws Exception{
        OutputUtils.plotOn();
        Graph agentGraph = new Graph("LooseGraph");
        AttackStep as1 = new AttackStepMin("access", new ConstantRealDistribution(0), Order.ENTRYSTEP);
        AttackStep as2 = new AttackStepMin("authorized", new ConstantRealDistribution(2), Order.ENTRYSTEP);
        AttackStep as3 = new AttackStepMax("compromise", new ConstantRealDistribution(2), Order.EXITSTEP);

        as1.connectToChild(as3);
        as2.connectToChild(as3);
        agentGraph.addAttackStep(as1);
        agentGraph.addAttackStep(as2);
        agentGraph.addAttackStep(as3);
        //    graph.sample();

        as1.setOrdinalLocalTtc(OrdinalTtcValue.SOURCE);
        as2.setOrdinalLocalTtc(OrdinalTtcValue.ZERO);
        as3.setOrdinalLocalTtc(OrdinalTtcValue.ANY);

        assertTrue(as1.getChildren().contains(as3));
        assertTrue(as2.getChildren().contains(as3));

        agentGraph.ordinalCompute(as1);
        agentGraph.ordinalCompute(as2);
        agentGraph.ordinalCompute(as3);

    }

    @After
    public void close() {
        OutputUtils.verboseOff();
    }
}
