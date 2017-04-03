package computation;


import attackgraph.AttackStep;
import attackgraph.Graph;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import support.OutputUtils;
import support.TestUtils;

import java.util.HashMap;

public class GraphComputerTest {

    @Before
    public void setup() {
        OutputUtils.plotOn();
    }

    @Test
    public void testMaxNodeComparisonsA() {
        Graph graph = TestUtils.generateRandomGraph(1, 1, 15, 3, 0.4, 3, 0.2, 0.5);
        graph.sample();
        GraphComputer gc = new GraphComputer();
        HashMap<AttackStep,AttackStepResult> asResults = new HashMap<>();
        for(AttackStep as : graph.attackStepsAsList()) {
            asResults.put(as, new AttackStepResult(as));
        }
        OutputUtils.mathematicaPlot(graph,1);

        gc.computeGraph(graph);
        for(AttackStep as : graph.attackStepsAsList()) {
            asResults.get(as).setMax0Ttc(as.getTtc());
        }
        graph.attackStepsAsList().stream().forEach(AttackStep::softReset);

        gc.computeGraph(graph);
        for(AttackStep as : graph.attackStepsAsList()) {
            asResults.get(as).setMax1Ttc(as.getTtc());
        }
        graph.attackStepsAsList().stream().forEach(AttackStep::softReset);

        gc.computeGraph(graph);
        for(AttackStep as : graph.attackStepsAsList()) {
            asResults.get(as).setSumTtc(as.getTtc());
            System.out.println(as.getName()+": "+asResults.get(as).getMax0Ttc()+" / "
                    +asResults.get(as).getMax1Ttc()+" / "+asResults.get(as).getSumTtc());
        }
    }

    @After
    public void tearDown() {
        OutputUtils.plotOff();
    }

    class AttackStepResult {
        AttackStep as;
        double max0Ttc;
        double max1Ttc;
        double sumTtc;

        AttackStepResult (AttackStep as) {
            this.as = as;
        }

        public double getMax0Ttc() {
            return max0Ttc;
        }

        public void setMax0Ttc(double max0Ttc) {
            this.max0Ttc = max0Ttc;
        }

        public double getMax1Ttc() {
            return max1Ttc;
        }

        public void setMax1Ttc(double max1Ttc) {
            this.max1Ttc = max1Ttc;
        }

        public double getSumTtc() {
            return sumTtc;
        }

        public void setSumTtc(double sumTtc) {
            this.sumTtc = sumTtc;
        }
    }
}
