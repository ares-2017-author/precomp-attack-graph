package support;


import attackgraph.*;
import datatypes.Order;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.BinomialDistribution;

import java.util.*;
import java.util.stream.Collectors;

public class TestUtils {

    private static int randomSeed = (int) System.nanoTime();
    private static Random rand = new Random(randomSeed);

    public static Graph generateRandomGraph(int nEntrySteps, int nExitSteps, int maxAttackSteps, int nBinomialTreeEdges,
                                            double pBinomialTreeEdges, int nBinomialForwardEdges, double pBinomialForwardEdges,
                                            double pMinAttackSteps) {

        Graph graph = generateRandomGraph(nEntrySteps,nExitSteps,maxAttackSteps,nBinomialTreeEdges,pBinomialTreeEdges,
                nBinomialForwardEdges,pBinomialForwardEdges,1,0.05,1,0.05,pMinAttackSteps);

        return graph;
    }

    public static Graph generateRandomGraph(int nEntrySteps, int nExitSteps, int maxAttackSteps, int nBinomialTreeEdges,
                                            double pBinomialTreeEdges, int nBinomialForwardEdges, double pBinomialForwardEdges,
                                            int nBinomialCrossEdges, double pBinomialCrossEdges, int nBinomialBackEdges,
                                            double pBinomialBackEdges, double pMinAttackSteps) {

        Graph graph = new Graph("LooseGraph");
        List<AttackStep> frontier = new ArrayList<>();
        List<AttackStep> stepList = new ArrayList<>();
        int nAttackSteps = 0;
        int depthness = 0;
        AttackStep child;
        BinomialDistribution treeEdgesDistribution = new BinomialDistribution(nBinomialTreeEdges, pBinomialTreeEdges);
        BinomialDistribution forwardEdgesDistribution = new BinomialDistribution(nBinomialForwardEdges, pBinomialForwardEdges);
        BinomialDistribution crossEdgesDistribution = new BinomialDistribution(nBinomialCrossEdges, pBinomialCrossEdges);
        BinomialDistribution backEdgesDistribution = new BinomialDistribution(nBinomialBackEdges, pBinomialBackEdges);
        AttackStep exitStep;

        treeEdgesDistribution.reseedRandomGenerator(randomSeed);
        forwardEdgesDistribution.reseedRandomGenerator(randomSeed);
        crossEdgesDistribution.reseedRandomGenerator(randomSeed);
        backEdgesDistribution.reseedRandomGenerator(randomSeed);
        rand.setSeed(randomSeed);
        OutputUtils.printVeryVerbose("Create model");

        AttackStepMin entryStep;
        for (int iEntryStep = 1; iEntryStep <= nEntrySteps; iEntryStep++) {
            entryStep = new AttackStepMin("entryStep" + iEntryStep, new TestDistribution(0.8, 10, 1), Order.ENTRYSTEP);
            graph.addAttackStep(entryStep);
            frontier.add(entryStep);
            entryStep.setDepth(0);
        }
        while (frontier.size() > 0) {
            OutputUtils.printVeryVerbose("Frontier holds " + frontier.size() + " elements.");
            AttackStep parent = frontier.get(0);
            frontier.remove(parent);
            stepList.add(parent);
            int nChildren = treeEdgesDistribution.sample();
            if (frontier.size() == 0 && nChildren == 0) {
                nChildren = 1;
            }
            OutputUtils.printVeryVerbose(parent.getName() + " gets " + nChildren + " children.");
            List<AttackStep> children = new ArrayList<>();
            for (int iChild = 1; iChild <= nChildren; iChild++) {
                if (nAttackSteps <= maxAttackSteps) {
                    /* tree edges */
                    String childName = Integer.toString(nAttackSteps + nEntrySteps);
                    child = randomlyCreateMaxOrMinAttackStep(pMinAttackSteps, childName, Order.MIDSTEP);
                    nAttackSteps++;
                    children.add(child);
                    graph.addAttackStep(child);
                    parent.connectToChild(child);
                    graph.incTreeEdges();
                    child.setDepth(parent.getDepth()+1);
                    depthness = Math.max(child.getDepth(),depthness);
                    OutputUtils.printVerbose("Connected parent " + parent.getName()
                            + " to child " + child.getName() + ".");
                    /* forward edges */
                    int nForwardEdges = forwardEdgesDistribution.sample();
                    nForwardEdges = Math.min(nForwardEdges, stepList.size());
                    Collections.shuffle(stepList, new Random(1));
                    OutputUtils.printVeryVerbose(child.getName() + " gets " + nForwardEdges + " random (forward) parents.");
                    for (int iForwardEdges = 0; iForwardEdges < nForwardEdges; iForwardEdges++) {
                        AttackStep forwardStep = stepList.get(iForwardEdges);
                        while (forwardStep.equals(parent) && nForwardEdges < stepList.size()) {
                            //if frontierStep is a direct parent, we move the whole range
                            iForwardEdges++;
                            nForwardEdges++;
                            forwardStep = stepList.get(iForwardEdges);
                        }
                        if (!forwardStep.equals(parent)) {
                            OutputUtils.printVerbose("Connected parent " + forwardStep.getName()
                                    + " randomly (forward) to child " + child.getName());
                            forwardStep.connectToChild(child);
                            graph.incForwardEdges();
                        } else OutputUtils.printVerbose("Forward edge: could not find a suitable ancestor in frontier");
                    }
                    // shuffle the frontier again for back edges this time
                    Collections.shuffle(stepList, new Random(1));
                    /* back edges */
                    int nBackEdges = backEdgesDistribution.sample();
                    nBackEdges = Math.min(nBackEdges, stepList.size());
                    OutputUtils.printVeryVerbose(child.getName() + " gets " + nBackEdges + " random (back) children.");
                    for (int iBackEdges = 0; iBackEdges < nBackEdges; iBackEdges++) {
                        AttackStep backStep = stepList.get(iBackEdges);
                        while (children.contains(backStep) && nBackEdges < stepList.size()) {
                            //if frontierStep is part of the same iteration, we move the whole range
                            iBackEdges++;
                            nBackEdges++;
                            backStep = stepList.get(iBackEdges);
                        }
                        if (!backStep.equals(parent)) {
                            OutputUtils.printVerbose("Connected parent " + child.getName()
                                    + " randomly to (back) child " + backStep.getName());
                            backStep.connectToChild(child);
                            graph.incBackEdges();
                        } else OutputUtils.printVerbose("Back edge: could not find a suitable ancestor in frontier");
                    }
                    frontier.add(child);
                }
            }
        }
        /* cross edges */
        // Cross edges are made once the graph has been fully created
        for(int crossEdgesProgression = 0; crossEdgesProgression < depthness; crossEdgesProgression++) {
            final int currentDepth = crossEdgesProgression;
            List<AttackStep> crossSteps = stepList.stream().filter(as -> as.getDepth() == currentDepth).collect(Collectors.toList());
            if (crossSteps.size() > 1) {
                for(AttackStep crossStep: crossSteps) {
                    List<AttackStep> otherCrosses = new LinkedList<>(crossSteps);
                    otherCrosses.remove(crossStep);
                    otherCrosses.removeAll(crossStep.getChildren());
                    int nCrossEdges = crossEdgesDistribution.sample();
                    // we don't want double edges
                    nCrossEdges = Math.min(nCrossEdges, otherCrosses.size());
                    while (nCrossEdges > 0) {
                        AttackStep cChild;
                        do {
                            cChild = otherCrosses.get(rand.nextInt(otherCrosses.size()));
                        } while (crossStep.equals(cChild) || crossStep.getChildren().contains(cChild));

                        OutputUtils.printVeryVerbose(crossStep.getName() + " gets " + cChild + " as (cross) child.");
                        crossStep.connectToChild(cChild);
                        otherCrosses.remove(cChild);
                        graph.incCrossEdges();
                        OutputUtils.printVerbose("Connected parent " + crossStep.getName()
                                + " randomly to (back) child " + cChild.getName());
                        nCrossEdges--;
                    }
                }
            }
        }
        final int finaldepthbeforeExits = depthness;
        List<AttackStep> deepSteps =
                graph.attackStepsAsSet().stream().filter(as -> as.getDepth() == finaldepthbeforeExits).collect(Collectors.toList());
        int createdExits;
        for (createdExits = 0; createdExits < nEntrySteps / 2; createdExits++) {
            exitStep = new AttackStepMin("exitStep" + (nAttackSteps + nEntrySteps + 1),
                    new TestDistribution(0.8, 10, 1), Order.EXITSTEP);
            graph.addAttackStep(exitStep);
            AttackStep exitParent = deepSteps.get(rand.nextInt(deepSteps.size()));
            exitParent.connectToChild(exitStep);
            exitStep.setDepth(exitParent.getDepth()+1);
            depthness = Math.max(exitStep.getDepth(),depthness);
            graph.incTreeEdges();
            OutputUtils.printVerbose("Connected parent " + exitParent.getName()
                    + " to child " + exitStep.getName() + ".");
        }


        // the rest of the attack steps are chosen randomly
        // To make it more realistic, i.e. with exit steps not to close to entry steps,
        // we use a beta distribution a=4 b=1 to select the step to be converted
        // the idea is to have the exit step still deep inside the graph
        List<AttackStep> allmidsteps = new LinkedList<>(graph.getMidSteps());
        allmidsteps.sort(Comparator.comparing(a -> a.getDepth()));
//        for(AttackStep as: allmidsteps) System.out.print(as.getName()+"["+as.getDepth()+"] ,");
        BetaDistribution bd_exits = new BetaDistribution(4,1);
        bd_exits.reseedRandomGenerator(randomSeed);
        for (int iExitStep = 1; iExitStep < nExitSteps-createdExits; iExitStep++) {
            int stepId = (int)(bd_exits.sample() * (allmidsteps.size()));
            AttackStep newExitStep = allmidsteps.get(stepId);
            newExitStep.setOrder(Order.EXITSTEP);
            allmidsteps.remove(newExitStep);
        }

        /* Finally, there should be no loose child amongst the mid-steps */
        List<AttackStep> allsteps = new LinkedList<>(graph.attackStepsAsList());
        allsteps.sort(Comparator.comparing(a -> a.getDepth()));
        // it is further enforced that the child has to be deep in the graph
        BetaDistribution bd_sadSteps = new BetaDistribution(4.5,1);
        graph.getMidSteps().stream().filter(as -> as.getChildren().isEmpty()).forEach(midstep -> {
            AttackStep newChild = allsteps.get((int)(bd_sadSteps.sample() * (allmidsteps.size())));
            midstep.connectToChild(newChild);
            if (midstep.getDepth() == newChild.getDepth()-1) graph.incTreeEdges();
            else if (midstep.getDepth() < newChild.getDepth() -1) graph.incForwardEdges();
            else if (midstep.getDepth() == newChild.getDepth()) graph.incCrossEdges();
            else if (midstep.getDepth() > newChild.getDepth()) graph.incBackEdges();
        });


        OutputUtils.printVeryVerbose("\n\n");
        return graph;
    }


    private static AttackStep randomlyCreateMaxOrMinAttackStep(double pMinAttackSteps, String name, Order order) {
        AttackStep child;
        if (rand.nextDouble() <= pMinAttackSteps) {
            child = new AttackStepMin("min" + name, new TestDistribution(0.8, 10, 1), order);
        } else {
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
        } else {
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
                } else {
                    AttackStep copiedChild = copiedGraph.getAttackStep(child.getName());
                    copiedAttackStep.connectToChild(copiedChild);

                }
            }
        }
        return copiedAttackStep;
    }
}
