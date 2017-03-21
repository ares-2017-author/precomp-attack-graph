package support;

import attackGraph.AttackStep;
import attackGraph.AttackStepMin;
import attackGraph.Order;
import components.Graph;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.Set;
import java.util.stream.Collectors;

public class TarjanTest {

    @Test
    public void strongConnectTest() {
        AttackStep a = new AttackStepMin("a", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep b = new AttackStepMin("b", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep c = new AttackStepMin("c", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep d = new AttackStepMin("d", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep e = new AttackStepMin("e", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep f = new AttackStepMin("f", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep g = new AttackStepMin("g", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep h = new AttackStepMin("h", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);

        a.connectToChild(b);
        b.connectToChild(a);
        b.connectToChild(c);
        c.connectToChild(d);
        d.connectToChild(a);
        a.connectToChild(e);
        e.connectToChild(f);
        a.connectToChild(g);
        g.connectToChild(h);
        h.connectToChild(a);

        Graph graph = new Graph("LooseGraph");
        graph.addAttackStep(a);
        graph.addAttackStep(b);
        graph.addAttackStep(c);
        graph.addAttackStep(d);
        graph.addAttackStep(e);
        graph.addAttackStep(f);
        graph.addAttackStep(g);
        graph.addAttackStep(h);

        Tarjan t = new Tarjan();
        Set<AttackStep> scc =  t.getSccOf(a);
        System.out.println(scc.stream().map(AttackStep::getName).collect(Collectors.joining("; ")));

    }

    @Test
    public void strongConnectWithOtherSCCsTest() {
        AttackStep a = new AttackStepMin("a", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep b = new AttackStepMin("b", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep c = new AttackStepMin("c", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep d = new AttackStepMin("d", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep e = new AttackStepMin("e", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep f = new AttackStepMin("f", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep g = new AttackStepMin("g", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep h = new AttackStepMin("h", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep i = new AttackStepMin("i", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);

        a.connectToChild(b);
        b.connectToChild(a);
        b.connectToChild(c);
        c.connectToChild(d);
        d.connectToChild(a);
        a.connectToChild(e);
        e.connectToChild(f);
        f.connectToChild(i);
        i.connectToChild(e);
        a.connectToChild(g);
        g.connectToChild(h);
        h.connectToChild(a);

        Graph graph = new Graph("LooseGraph");
        graph.addAttackStep(a);
        graph.addAttackStep(b);
        graph.addAttackStep(c);
        graph.addAttackStep(d);
        graph.addAttackStep(e);
        graph.addAttackStep(f);
        graph.addAttackStep(g);
        graph.addAttackStep(h);

        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph,1);
        OutputUtils.plotOff();

        Tarjan t = new Tarjan();
        Set<AttackStep> scc =  t.getSccOf(a);
        System.out.println(scc.stream().map(AttackStep::getName).collect(Collectors.joining("; ")));

    }

    @Test
    public void strongConnectWithOtherSCCsTest2() throws Exception {
        AttackStep a = new AttackStepMin("a", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep b = new AttackStepMin("b", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep c = new AttackStepMin("c", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep d = new AttackStepMin("d", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep e = new AttackStepMin("e", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep f = new AttackStepMin("f", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep g = new AttackStepMin("g", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep h = new AttackStepMin("h", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep i = new AttackStepMin("i", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);

        a.connectToChild(b);
        b.connectToChild(a);
        b.connectToChild(c);
        c.connectToChild(d);
        d.connectToChild(a);
        e.connectToChild(a);
        e.connectToChild(f);
        f.connectToChild(i);
        i.connectToChild(e);
        a.connectToChild(g);
        g.connectToChild(h);
        h.connectToChild(a);

        Graph graph = new Graph("LooseGraph");
        graph.addAttackStep(a);
        graph.addAttackStep(b);
        graph.addAttackStep(c);
        graph.addAttackStep(d);
        graph.addAttackStep(e);
        graph.addAttackStep(f);
        graph.addAttackStep(g);
        graph.addAttackStep(h);

        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph,1);
        OutputUtils.plotOff();

        Tarjan t = new Tarjan();
        Set<AttackStep> scc =  t.getSccOf(a);
        System.out.println(scc.stream().map(AttackStep::getName).collect(Collectors.joining("; ")));
    }

    @Test
    public void strongConnectWithOtherSCCsTest3() throws Exception {
        AttackStep a = new AttackStepMin("a", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep b = new AttackStepMin("b", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep c = new AttackStepMin("c", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep d = new AttackStepMin("d", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep e = new AttackStepMin("e", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep f = new AttackStepMin("f", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep g = new AttackStepMin("g", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep h = new AttackStepMin("h", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep i = new AttackStepMin("i", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);

        a.connectToChild(b);
        b.connectToChild(a);
        b.connectToChild(c);
        c.connectToChild(d);
        d.connectToChild(a);
        e.connectToChild(a);
        e.connectToChild(f);
        f.connectToChild(i);
        i.connectToChild(e);
        a.connectToChild(g);
        g.connectToChild(h);
        h.connectToChild(a);

        Graph graph = new Graph("LooseGraph");
        graph.addAttackStep(a);
        graph.addAttackStep(b);
        graph.addAttackStep(c);
        graph.addAttackStep(d);
        graph.addAttackStep(e);
        graph.addAttackStep(f);
        graph.addAttackStep(g);
        graph.addAttackStep(h);

        OutputUtils.plotOn();
        OutputUtils.mathematicaPlot(graph,1);
        OutputUtils.plotOff();

        Tarjan t = new Tarjan();
        Set<Set<AttackStep>> sccs =
                Whitebox.invokeMethod(t,"strongConnect",a,true);
        sccs.stream().forEach(scc ->
                System.out.println(scc.stream().map(AttackStep::getName).collect(Collectors.joining("; "))));

    }
}
