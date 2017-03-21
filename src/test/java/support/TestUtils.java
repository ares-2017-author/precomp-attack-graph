package support;


import attackGraph.*;
import components.Graph;
import org.apache.commons.math3.distribution.BinomialDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestUtils {

    // The randomSeed is kept the same to allow for experiment reproduction
    private static int        randomSeed       = 2;
    private static Random     rand             = new Random(randomSeed);

    public static Graph generateRandomGraph(int nEntrySteps, int nExitSteps, int maxAttackSteps, int maxChildren,
                                            double pBinomialChildren, int maxOldParents, double pBinomialOldParents,
                                            double pMinAttackSteps) {

        Graph graph = new Graph("LooseGraph");
        BinomialDistribution nParentDistribution = new BinomialDistribution(4, 0.5);
        BinomialDistribution nChildrenDistribution = new BinomialDistribution(4, 0.5);
        List<AttackStep> frontier = new ArrayList<>();
        int nAttackSteps = 0;
        AttackStep child;
        BinomialDistribution childrenDistribution = new BinomialDistribution(maxChildren, pBinomialChildren);
        BinomialDistribution parentDistribution = new BinomialDistribution(maxOldParents, pBinomialOldParents);
        AttackStep exitStep;

        nParentDistribution.reseedRandomGenerator(randomSeed);
        nChildrenDistribution.reseedRandomGenerator(randomSeed);
        childrenDistribution.reseedRandomGenerator(randomSeed);
        parentDistribution.reseedRandomGenerator(randomSeed);
        rand.setSeed(randomSeed);
        OutputUtils.printVeryVerbose("Create model");

        AttackStepMin entryStep;
        for (int iEntryStep = 1; iEntryStep <= nEntrySteps; iEntryStep++) {
            entryStep = new AttackStepMin("entryStep" + iEntryStep, new TestDistribution(0.8, 10, 1), Order.ENTRYSTEP);
            graph.addAttackStep(entryStep);
            frontier.add(entryStep);
        }
        while (frontier.size() > 0) {
            OutputUtils.printVeryVerbose("Frontier holds " + frontier.size() + " elements.");
            AttackStep parent = frontier.get(0);
            frontier.remove(parent);
            int nChildren = childrenDistribution.sample();
            if (frontier.size() == 0 && nChildren == 0) {
                nChildren = 1;
            }
            OutputUtils.printVeryVerbose(parent.getName() + " gets " + nChildren + " children.");
            for (int iChild = 1; iChild <= nChildren; iChild++) {
                nAttackSteps++;
                if (nAttackSteps <= maxAttackSteps) {
                    String childName = Integer.toString(nAttackSteps + nEntrySteps);
                    child = randomlyCreateMaxOrMinAttackStep(pMinAttackSteps, childName, Order.MIDSTEP);
                    graph.addAttackStep(child);
                    parent.connectToChild(child);
                    OutputUtils.printVerbose("Connected parent " + parent.getName()
                            + " to child " + child.getName() + ".");
                    int nOldParents = parentDistribution.sample();
                    nOldParents = Math.min(nOldParents, frontier.size());
                    OutputUtils.printVeryVerbose(child.getName() + " gets " + nOldParents + " random parents.");
                    for (int iOldParents = 0; iOldParents < nOldParents; iOldParents++) {
                        AttackStep frontierStep = frontier.get(iOldParents);
                        if (!frontierStep.equals(parent)) {
                            OutputUtils.printVerbose("Connected parent " + frontierStep.getName()
                                    + " randomly to child " + child.getName());
                            frontierStep.connectToChild(child);
                        }
                    }
                    frontier.add(child);
                    if (nAttackSteps > nEntrySteps) {
                        Collections.shuffle(frontier, new Random(1));
                    }
                }
            }
        }
        exitStep = new AttackStepMin("exitStep" + (nAttackSteps + nEntrySteps + 1),
                                     new TestDistribution(0.8, 10, 1), Order.EXITSTEP);
        graph.addAttackStep(exitStep);
        graph.getMidSteps().get(graph.getMidSteps().size() - 1).connectToChild(exitStep);
        OutputUtils.printVerbose("Connected parent " + graph.getMidSteps().get(graph.getMidSteps().size() - 1).getName()
                                 + " to child " + exitStep.getName() + ".");
        for (int iExitStep = 2; iExitStep <= nExitSteps; iExitStep++) {
            exitStep = new AttackStepMin("exitStep" + (nAttackSteps + nEntrySteps + iExitStep),
                                         new TestDistribution(0.8, 10, 1), Order.EXITSTEP);
            graph.addAttackStep(exitStep);
            AttackStep penUltimateStep = graph.getMidSteps().get(rand.nextInt(graph.getMidSteps().size() - 1));
            penUltimateStep.connectToChild(exitStep);
            OutputUtils.printVerbose("Connected parent " + penUltimateStep.getName() +
                                     " to child " + exitStep.getName() + ".");
        }
        OutputUtils.printVeryVerbose("\n\n");

        return graph;
    }

    private static AttackStep randomlyCreateMaxOrMinAttackStep(double pMinAttackSteps, String name, Order order) {
        AttackStep child;
        if (rand.nextDouble() <= pMinAttackSteps) {
            child = new AttackStepMin("min" + name, new TestDistribution(0.8, 10, 1), order);
        }
        else {
            child = new AttackStepMax("max" + name, new TestDistribution(0.8, 10, 1), order);
        }
        return child;
    }

    public static Graph cloneGraph(Graph original) {
        Graph copy = new Graph("LooseGraph");
        for (AttackStep attackStep : original.attackStepsAsList()) {
            copy.addAttackStep(cloneAttackStep(attackStep));
        }
        for (AttackStep originalParent : original.attackStepsAsList()) {
            AttackStep copiedParent = copy.getEquivalentAttackStep(originalParent);
            for (AttackStep originalChild : originalParent.getChildren()) {
                AttackStep copiedChild = copy.getEquivalentAttackStep(originalChild);
                copiedParent.connectToChild(copiedChild);
            }
        }

        return copy;
    }

    public static Defense cloneDefense(Defense original) {
        Defense copy;
        copy = new Defense(original.getName(), original.isEnabled(), original.isInitializedAsExitStep());
        copy.setIdSet(original.getIdSet());
        copy.setRandomSeed(original.getRandomSeed());
        copy.setLocalTtc(original.getLocalTtc());
        copy.setOrdinalLocalTtc(original.getOrdinalLocalTtc());
        copy.setLocalTtcDistributions(original.getLocalTtcDistributions());
        copy.setOrder(original.getOrder());
        return copy;
    }

    public static AttackStep cloneAttackStep(AttackStep original) {
        AttackStep copy;
        if (original instanceof AttackStepMax) {
            copy = new AttackStepMax(original.getName(), original.getLocalTtcDistributions(), original.getOrder());
        }
        else {
            copy = new AttackStepMin(original.getName(), original.getLocalTtcDistributions(), original.getOrder());
        }
        copy.setIdSet(original.getIdSet());
        copy.setRandomSeed(original.getRandomSeed());
        copy.setLocalTtc(original.getLocalTtc());
        copy.setOrdinalLocalTtc(original.getOrdinalLocalTtc());

        return copy;
    }

    private static AttackStep copyProgeny(AttackStep attackStep, Graph copiedGraph) {
        AttackStep copiedAttackStep = null;
        if (copiedGraph.getAttackStep(attackStep.getName()) == null) {
            copiedAttackStep = cloneAttackStep(attackStep);
            copiedGraph.addAttackStep(copiedAttackStep);
            for (AttackStep child : attackStep.getChildren()) {
                if (!copiedGraph.containsStep(child)) {
                    AttackStep copiedChild = copyProgeny(child, copiedGraph);
                    copiedAttackStep.connectToChild(copiedChild);
                }
                else {
                    AttackStep copiedChild = copiedGraph.getAttackStep(child.getName());
                    copiedAttackStep.connectToChild(copiedChild);

                }
            }
        }
        return copiedAttackStep;
    }
}
