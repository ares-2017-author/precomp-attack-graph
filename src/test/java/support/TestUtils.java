package support;


import attackgraph.*;
import datatypes.Order;
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
        return generateRandomGraph(nEntrySteps,nExitSteps,maxAttackSteps,nBinomialTreeEdges,pBinomialTreeEdges,
                nBinomialForwardEdges,pBinomialForwardEdges,1,0.05,1,0.05,pMinAttackSteps);
    }

    public static Graph generateRandomGraph(int nEntrySteps, int nExitSteps, int maxAttackSteps, int nBinomialTreeEdges,
                                            double pBinomialTreeEdges, int nBinomialForwardEdges, double pBinomialForwardEdges,
                                            int nBinomialCrossEdges, double pBinomialCrossEdges, int nBinomialBackEdges,
                                            double pBinomialBackEdges, double pMinAttackSteps) {
        return generateRandomGraph(maxAttackSteps, -1, nEntrySteps, 1, 1, nExitSteps, 1, 1, nBinomialTreeEdges,
                pBinomialTreeEdges, nBinomialForwardEdges, pBinomialForwardEdges, nBinomialCrossEdges, pBinomialCrossEdges,
                nBinomialBackEdges, pBinomialBackEdges, pMinAttackSteps);
    }

    public static Graph generateRandomGraph(int size, int rowThreshold, int nEntrySteps, float aBetaEntries, float bBetaEntries, int nExitSteps,
                                            float aBetaExits, float bBetaExits, int nBinomialTreeEdges,
                                            double pBinomialTreeEdges, int nBinomialForwardEdges, double pBinomialForwardEdges,
                                            int nBinomialCrossEdges, double pBinomialCrossEdges, int nBinomialBackEdges,
                                            double pBinomialBackEdges, double pMinAttackSteps) {

        Graph graph = new Graph("LooseGraph");
        LinkedList<AttackStep> frontier = new LinkedList<>();
        List<AttackStep> stepList = new ArrayList<>();
        int[] stepCountPerLayer;
        if (rowThreshold > 1) {
            stepCountPerLayer = new int[(size+5)/rowThreshold];
        } else {
            stepCountPerLayer = new int[size+1]; // TODO find a smaller limit
            rowThreshold = size;
        }
        int nAttackSteps = 0;
        int depthness = 0;
        int currentDepth = 0;
        AttackStep child;
        BinomialDistribution treeEdgesDistribution = new BinomialDistribution(nBinomialTreeEdges, pBinomialTreeEdges);
        BinomialDistribution forwardEdgesDistribution = new BinomialDistribution(nBinomialForwardEdges, pBinomialForwardEdges);
        BinomialDistribution crossEdgesDistribution = new BinomialDistribution(nBinomialCrossEdges, pBinomialCrossEdges);
        BinomialDistribution backEdgesDistribution = new BinomialDistribution(nBinomialBackEdges, pBinomialBackEdges);


        treeEdgesDistribution.reseedRandomGenerator(randomSeed);
        forwardEdgesDistribution.reseedRandomGenerator(randomSeed);
        crossEdgesDistribution.reseedRandomGenerator(randomSeed);
        backEdgesDistribution.reseedRandomGenerator(randomSeed);
        rand.setSeed(randomSeed);
        OutputUtils.printVeryVerbose("Create model");

        // We imagine a ghost root vertex. Therefore, the size of the first row of vertices is decided by sampling
        // the treeEdgeDistribution.

        // We keep at least on EntryStep as first node, otherwise the graphs are too often malformed
        // E.G. no path between entry and exit points
        AttackStep firstEntry = new AttackStepMin("entryStep0", new TestDistribution(0.8, 10, 1), Order.ENTRYSTEP);
        graph.addAttackStep(firstEntry);
        frontier.add(firstEntry);
        stepList.add(firstEntry);
        firstEntry.setDepth(0);
        nAttackSteps++;
        stepCountPerLayer[0]++;

        int nbrOfFirstRow = Math.max(treeEdgesDistribution.sample(),1);
        AttackStep firstRowStep;
        for (int i = 1; i < nbrOfFirstRow; i++) {
            firstRowStep = randomlyCreateMaxOrMinAttackStep(pMinAttackSteps, Integer.toString(i), Order.MIDSTEP);
            graph.addAttackStep(firstRowStep);
            frontier.add(firstRowStep);
            stepList.add(firstRowStep);
            firstRowStep.setDepth(0);
            nAttackSteps++;
            stepCountPerLayer[0]++;//,stepCountPerLayer.get(0)+1);
        }
        while (frontier.size() > 0) {
            if (stepCountPerLayer[currentDepth] >= rowThreshold) {
                final int cd = currentDepth;
                frontier.removeAll(frontier.stream().filter(as -> as.getDepth() == cd).collect(Collectors.toSet()));
                currentDepth++;
            } else {
                OutputUtils.printVeryVerbose("Frontier holds " + frontier.size() + " elements.");
                AttackStep parent = frontier.pollFirst();
                currentDepth = parent.getDepth();
                stepCountPerLayer[currentDepth]++;
                int nChildren = treeEdgesDistribution.sample();
                nChildren = Math.max(nChildren,1); // if threshold is not met, we need to create children
                OutputUtils.printVeryVerbose(parent.getName() + " gets " + nChildren + " children.");
                List<AttackStep> children = new ArrayList<>();
                for (int iChild = 1; iChild <= nChildren; iChild++) {
                    if (nAttackSteps <= size) {
                    /* tree edges */
                        String childName = Integer.toString(nAttackSteps);
                        child = randomlyCreateMaxOrMinAttackStep(pMinAttackSteps, childName, Order.MIDSTEP);
                        if (!stepList.contains(child)) stepList.add(child);
                        nAttackSteps++;
                        children.add(child);
                        graph.addAttackStep(child);
                        parent.connectToChild(child);
                        graph.incTreeEdges();
                        child.setDepth(parent.getDepth() + 1);
                        depthness = Math.max(child.getDepth(), depthness);
                        OutputUtils.printVerbose("Connected parent " + parent.getName()
                                + " to child " + child.getName() + ".");
                    /* forward edges must come from parents that are at least 2 level higher than child */
                        int nForwardEdges = forwardEdgesDistribution.sample();
                        final int childDepth = child.getDepth();
                        LinkedList<AttackStep> forwards = new LinkedList<>(stepList.stream()
                                .filter(as -> as.getDepth() < childDepth - 1).collect(Collectors.toList()));
                        Collections.shuffle(forwards, new Random(randomSeed));
                        nForwardEdges = Math.min(nForwardEdges, forwards.size());
                        OutputUtils.printVeryVerbose(child.getName() + " gets " + nForwardEdges + " random (forward) parents.");
                        for (int iForwardEdges = 0; iForwardEdges < nForwardEdges; iForwardEdges++) {
                            AttackStep forwardStep = forwards.pop();
                            if (!forwardStep.equals(parent)) {
                                OutputUtils.printVerbose("Connected parent " + forwardStep.getName()
                                        + " randomly (forward) to child " + child.getName());
                                forwardStep.connectToChild(child);
                                graph.incForwardEdges();
                            } else
                                OutputUtils.printVerbose("Forward edge: could not find a suitable ancestor in frontier");
                        }
                        // shuffle the frontier again for back edges this time
                        LinkedList<AttackStep> backs = new LinkedList<>(stepList.stream()
                                .filter(as -> as.getDepth() < childDepth).collect(Collectors.toList()));
                        Collections.shuffle(backs, new Random(1));
                    /* back edges */
                        int nBackEdges = backEdgesDistribution.sample();
                        nBackEdges = Math.min(nBackEdges, backs.size());
                        OutputUtils.printVeryVerbose(child.getName() + " gets " + nBackEdges + " random (back) children.");
                        for (int iBackEdges = 0; iBackEdges < nBackEdges; iBackEdges++) {
                            AttackStep backStep = backs.pop();
                            if (!backStep.equals(parent)) {
                                OutputUtils.printVerbose("Connected parent " + child.getName()
                                        + " randomly to (back) child " + backStep.getName());
                                backStep.connectToChild(child);
                                graph.incBackEdges();
                            } else
                                OutputUtils.printVerbose("Back edge: could not find a suitable ancestor in frontier");
                        }
                        frontier.add(child);
                    }
                }
            }
        }
        /* cross edges */
        // Cross edges are made once the graph has been fully created
        for(int crossEdgesProgression = 0; crossEdgesProgression < depthness; crossEdgesProgression++) {
            final int cd = crossEdgesProgression;
            List<AttackStep> crossSteps = stepList.stream().filter(as -> as.getDepth() == cd).collect(Collectors.toList());
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

        // the entry steps are chosen randomly
        // As heuristic, we use a beta distribution to select the step to be converted based on their depth inside the graph
        // By default, there is no heuristic (a=1, b=1)
        List<AttackStep> allmidsteps = new LinkedList<>(graph.getMidSteps());
        allmidsteps.sort(Comparator.comparing(a -> a.getDepth()));
        BetaDistribution bd_entry = new BetaDistribution(aBetaEntries,bBetaEntries);
        bd_entry.reseedRandomGenerator(randomSeed);
        for (int iEntryStep = 1; iEntryStep < nEntrySteps; iEntryStep++) {
            int stepId = (int)(bd_entry.sample() * (allmidsteps.size()));
            AttackStep newEntryStep = allmidsteps.get(stepId);
            newEntryStep.setOrder(Order.ENTRYSTEP);
            newEntryStep.setName("entrystep"+iEntryStep);
            allmidsteps.remove(newEntryStep);
        }


        // the exit steps are chosen randomly
        // As heuristic, we use a beta distribution to select the step to be converted based on their depth inside the graph
        // By default, there is no heuristic (a=1, b=1)
        allmidsteps.sort(Comparator.comparing(a -> a.getDepth()));
        BetaDistribution bd_exits = new BetaDistribution(aBetaExits,bBetaExits);
        bd_exits.reseedRandomGenerator(randomSeed);
        for (int iExitStep = 1; iExitStep <= nExitSteps; iExitStep++) {
            int stepId = (int)(bd_exits.sample() * (allmidsteps.size()));
            AttackStep newExitStep = allmidsteps.get(stepId);
            newExitStep.setOrder(Order.EXITSTEP);
            newExitStep.setName("exitstep"+iExitStep);
            allmidsteps.remove(newExitStep);
        }


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
