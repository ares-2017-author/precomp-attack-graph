package computation;

import attackgraph.AttackStep;
import attackgraph.AttackStepMax;
import attackgraph.AttackStepMin;
import attackgraph.Graph;
import datatypes.Order;
import support.OutputUtils;
import support.Tarjan;
import support.TimeWatch;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static computation.OrdinalOperations.plus;

public class GraphTransformer {

    private Graph graph;
    private GraphOrdinalComputer goc;

    public GraphTransformer(Graph g) {
        this.graph = g;
        goc = new GraphOrdinalComputer(g);
    }

    public void reduce(Graph graph) {
        this.graph = graph;
        OutputUtils.printVerbose("Reducing Graph " + graph.getOwnerComponentName() + " (" + graph.attackStepsAsSet().size() + " asteps)...\n");
        graph.hardReset();
        graph.sample();
        TimeWatch tm = TimeWatch.start();
        deleteAllRedundantEdges();
        OutputUtils.printTerseVerbose("deleteAllRedundantEdges: " + (tm.time(TimeUnit.MILLISECONDS)) + " ms.");
        int graph_size = graph.size();
        tm = TimeWatch.start();
        deleteUnproductiveLoops();
        OutputUtils.printTerseVerbose("deleteUnproductiveLoops: " + (tm.time(TimeUnit.MILLISECONDS)) + " ms; " + (graph_size - graph.size()) + " steps removed.");
        graph_size = graph.size();
        tm = TimeWatch.start();
        retainOnlyExitStepAncestors();
        OutputUtils.printTerseVerbose("retainOnlyExitStepAncestors: " + (tm.time(TimeUnit.MILLISECONDS)) + " ms; " + (graph_size - graph.size()) + " steps removed.");
        graph_size = graph.size();
        tm = TimeWatch.start();
        retainOnlyEntryStepProgeny();
        OutputUtils.printTerseVerbose("retainOnlyEntryStepProgeny: " + (tm.time(TimeUnit.MILLISECONDS)) + " ms; " + (graph_size - graph.size()) + " steps removed.");
        graph_size = graph.size();
        tm = TimeWatch.start();
        reduceAllSingleChildParent();
        OutputUtils.printTerseVerbose("reduceAllSingleChildParent: " + (tm.time(TimeUnit.MILLISECONDS)) + " ms; " + (graph_size - graph.size()) + " steps removed.\n");
    }

    /* **************************************

    STEP 1: Redundant Edges Removal

    *************************************** */

    // As opposed to other entry steps, which have an undefined "source" value,
    // defense steps will either have the value zero or infinite. This means that
    // we can reduce further based on these. We need thus, for ordinalCompute(),
    // to initialize defenses with these extreme values, and then make sure that
    // they are correctly propagated in ordinalCompute(), and finally introduce
    // reduction rules based on these value, so that an AttackStepMax with an
    // infinite-ttc parent will result in infinity, which can be disconnected
    // from an attackStepMin if there is any other parent.

    private void deleteAllRedundantEdges() {
        int iSource = 0;
        OutputUtils.printVerbose("STEP 1: Redundant Edges removal\n");
        // Let's compute the progeny of entry nodes.

        // TODO We should probably start from the entry nodes? Except roots that are not entry nodes because it means they are
        // not reachable. We'd probably have fastest results this way.
        while (iSource < graph.size()) {
            AttackStep source = graph.getAttackStep(iSource);
            OutputUtils.printVeryVerbose("Source is " + source.getName());
            goc.ordinalCompute(source);
            deleteRedundantEdgesFromSource(graph, source);
            iSource++;
        }
    }

    /*
     * Instead of randomly selecting a descendant, we move step by
     * step to children and grand children. As soon as a step in a line of
     * descendants became ANY, we stop exploring that thread. Thus,
     * we have a recursive method that: 1. Starts with the source. 2. Steps
     * into each child. 3. If all parents are ANY, terminate the recursion.
     * 4. Otherwise, delete all relevant edges to parents, and 5. Recursive
     * call to repeat steps 3-4 for each child.
     */

    protected void deleteRedundantEdgesFromSource(Graph graph, AttackStep source) {
        HashSet<AttackStep> visited = new HashSet<>();
//        visited.add(source);
        deleteRedundantEdgesFromSource(graph, source, source, visited);
    }

    private void deleteRedundantEdgesFromSource(Graph graph, AttackStep source, AttackStep current, HashSet<AttackStep> visited) {
        List<AttackStep> children = new LinkedList<>(current.getChildren());
        HashSet<AttackStep> sourceConnectedParentsFixed;
        HashSet<AttackStep> sourceConnectedParents;

        for (AttackStep child : children) {
            sourceConnectedParentsFixed = child.getParentsConnectedToSource(source);
            sourceConnectedParents = new HashSet<>(sourceConnectedParentsFixed);
            if (!visited.contains(child) && sourceConnectedParentsFixed.size() > 1
                    && !sourceConnectedParentsFixed.stream().allMatch(parent -> parent.isAny())) {
                OutputUtils.printVeryVerbose("Target is " + child.getName());
                if (sourceConnectedParentsFixed.size() > 1) {
                    // TODO refactor this!
                    for (AttackStep firstParent : sourceConnectedParentsFixed) {
                        for (AttackStep secondParent : sourceConnectedParentsFixed) {
                            if (!firstParent.equals(secondParent) &&
                                    sourceConnectedParents.contains(firstParent) &&
                                    sourceConnectedParents.contains(secondParent)) {
                                int removalStatus = tryOrdinalReduce(child, firstParent, secondParent);
                                if (removalStatus > 0)
                                    graph.updateDescendantsOfSource();
                                if (removalStatus == 1)
                                    sourceConnectedParents.remove(firstParent);
                                else if (removalStatus == 2)
                                    sourceConnectedParents.remove(secondParent);
                            }
                        }
                    }
                }
                visited.add(child);
                deleteRedundantEdgesFromSource(graph, source, child, visited);
            } else visited.add(child);
        }
    }

    /**
     * Same AttackSteps                     -> no reduction
     * p1.source and p2.source              -> remove second parent
     * p1.source and p2.GTESource and t.min -> remove second parent
     * p1.source and p2.LTESource and t.max -> remove second parent
     * p1.source and p2.GTESource and t.max -> remove first parent
     * p1.source and p2.LTESource and t.min -> remove first parent
     * p1.zero and t.min                    -> remove second parent
     * p1.zero and t.max                    -> remove first parent
     * p1.infinite and t.min                -> remove first parent
     * p1.infinite and t.max                -> remove second parent
     *
     * @param target       target attack step
     * @param firstParent  parent attackstep
     * @param secondParent another parent attackstep
     * @return int 0 -> no deletion, 1 -> first parent disconnected, 2 -> second parent disconnected
     */
    private int tryOrdinalReduce(AttackStep target, AttackStep firstParent, AttackStep secondParent) {
        if (!firstParent.equals(secondParent)) {
            if (firstParent.isSource()) {
                if ((secondParent.isSource())
                        || (secondParent.isGTESource() && target instanceof AttackStepMin)
                        || (secondParent.isLTESource() && target instanceof AttackStepMax)) {
                    deleteConnection(target, secondParent);
                    return 2;
                }
                if ((secondParent.isGTESource() && target instanceof AttackStepMax)
                        || (secondParent.isLTESource() && target instanceof AttackStepMin)) {
                    deleteConnection(target, firstParent);
                    return 1;
                }
            }
            if (firstParent.isZero()) {
                if (target instanceof AttackStepMax) {
                    deleteConnection(target, firstParent);
                    return 1;
                }
                if (target instanceof AttackStepMin) {
                    deleteConnection(target, secondParent);
                    return 2;
                }
            }
            if (firstParent.isInfinite()) {
                if (target instanceof AttackStepMax) {
                    deleteConnection(target, secondParent);
                    return 2;
                }
                if (target instanceof AttackStepMin) {
                    deleteConnection(target, firstParent);
                    return 1;
                }
            }
        }
        return 0;
    }

    private void deleteConnection(AttackStep child, AttackStep parent) {
        parent.removeChild(child);
        if (child instanceof AttackStepMax) {
            Set<AttackStep> intersect = graph.getEntrySteps().stream()
                    .filter(child.getAncestors()::contains).collect(Collectors.toSet());
            if (intersect.isEmpty()) ((AttackStepMax) child).setLocked(true);
        }
        OutputUtils.printVerbose("Cut the cord between " + parent.getName() + " and " + child.getName() + ".");
    }

    /* **************************************

    STEP 2: Unproductive Loops Deletion

    *************************************** */

    /**
     * The goal is to remove loops that start AND finish from the same exit-step, without containing other entry and exitsteps
     * We first get the Strongly connected component of an exit step, then check for each node if their is a path
     *
     */
    private void deleteUnproductiveLoops() {
        OutputUtils.printVerbose("STEP 2: Loops removal\n");
        Set<AttackStep> toBeDeleted = new HashSet<>();
        Set<AttackStep> toBePreserved = new HashSet<>();
        for (AttackStep exitStep : graph.getExitSteps()) {
            Tarjan tarjan = new Tarjan();
            // SCC does not contain the exitStep itself. Removed in Tarjan
            Set<AttackStep> sccOfExStep = tarjan.getSccOf(exitStep);
            OutputUtils.printVeryVerbose("---------------------\nLoops from " + exitStep.getName() + ".\n---------------------");
            if (!sccOfExStep.isEmpty()
                    /*&& sccOfExStep.stream()
                    .filter(as -> as.getOrder().equals(Order.ENTRYSTEP) || as.getOrder().equals(Order.EXITSTEP))
                    .collect(Collectors.toList()).isEmpty()*/
                    ) {
                toBeDeleted.addAll(sccOfExStep.stream()
                        .filter(as -> !(as.getOrder().equals(Order.ENTRYSTEP) || as.getOrder().equals(Order.EXITSTEP)))
                        .collect(Collectors.toList()));
                graph.getExitSteps().stream().filter(exit -> !exit.equals(exitStep))
                        .forEach(es -> toBePreserved.addAll(exitStep.descendantsTo(es)));
                graph.getEntrySteps().stream().filter(entry -> !entry.equals(exitStep))
                        .forEach(es -> toBePreserved.addAll(exitStep.ancestorsTo(es)));
            }
        }
        for (AttackStep entryStep : graph.getEntrySteps()) {
            Tarjan tarjan = new Tarjan();
            // SCC does not contain the entryStep itself. Removed in Tarjan
            Set<AttackStep> sccOfEnStep = tarjan.getSccOf(entryStep);
            OutputUtils.printVeryVerbose("---------------------\nLoops from " + entryStep.getName() + ".\n---------------------");
            if (!sccOfEnStep.isEmpty()) {
                // Let's not remove entry and exit steps, shall we.
                toBeDeleted.addAll(sccOfEnStep.stream()
                        .filter(as -> !(as.getOrder().equals(Order.ENTRYSTEP) || as.getOrder().equals(Order.EXITSTEP)))
                        .collect(Collectors.toList()));
                graph.getExitSteps().stream().filter(es -> !es.equals(entryStep))
                        .forEach(es -> toBePreserved.addAll(entryStep.descendantsTo(es)));
                graph.getEntrySteps().stream().filter(es -> !es.equals(entryStep))
                        .forEach(es -> toBePreserved.addAll(entryStep.ancestorsTo(es)));
            }
        }
        toBeDeleted.removeAll(toBePreserved);
        if (!toBeDeleted.isEmpty()) {
            if (OutputUtils.isPrintVerbose()) {
                for (AttackStep as : toBeDeleted) {
                    OutputUtils.printVerbose("Deleting " + as.getName() + "[" + as.getOrder() + "], because in a loop.\n");
                }
            }
            graph.clearAttackSteps(toBeDeleted);
        }
    }

    /* **************************************

    STEP 3: Keep only Exit Steps' Ancestors

    *************************************** */

    // Attack steps that do not lead to the exit step can be deleted.
    private int retainOnlyExitStepAncestors() {
        OutputUtils.printVerbose("STEP 3: Loose attack step removal (From exit steps)\n");
        int nReduced = 0;
        Set<AttackStep> ancestors = new HashSet<>();
        for (AttackStep exitStep : graph.getExitSteps()) {
            ancestors.addAll(exitStep.getAncestors());
            ancestors.add(exitStep);
        }
        for (AttackStep attackStep : graph.attackStepsAsSet()) {
            if (!ancestors.contains(attackStep)) {
                // if we remove this attackstep, make sure that we lock a Max child that might become doable after removal of this step
                attackStep.getChildren().forEach(as -> {
                    if (as instanceof AttackStepMax) ((AttackStepMax) as).setLocked(true);
                });
                graph.clearAttackStep(attackStep);
                nReduced++;
                OutputUtils.printVerbose("Deleted attack step " + attackStep.getName() + ", as it does not lead to any exit step.");
            }
        }
        return nReduced;
    }

    /* **************************************

    STEP 4: Keep only Entry Steps' Descendants

    *************************************** */

    private int retainOnlyEntryStepProgeny() {
        OutputUtils.printVerbose("STEP 4: Loose attack step removal (From entry steps)\n");
        int nReduced = 0;
        Set<AttackStep> progeny = new HashSet<>();
        for (AttackStep entryStep : graph.getEntrySteps()) {
            progeny.addAll(entryStep.getProgenyI());
            progeny.add(entryStep);

        }
        for (AttackStep attackStep : graph.attackStepsAsSet()) {
            if (!progeny.contains(attackStep)) {
                // if we remove this attackstep, make sure that we lock a Max child that might become doable after removal of this step
                attackStep.getChildren().forEach(as -> {
                    if (as instanceof AttackStepMax) ((AttackStepMax) as).setLocked(true);
                });
                graph.clearAttackStep(attackStep);
                nReduced++;
                OutputUtils.printVerbose("Deleted attack step " + attackStep.getName() + ", as it does not emanate from any entry step.");
            }
        }
        return nReduced;
    }

    /* **************************************

    STEP 5: Edge Contraction

    *************************************** */

    private void reduceAllSingleChildParent() {
        boolean didReduce = true;
        OutputUtils.printVerbose("STEP 5: reduceAllSingleChildParent");
        while (didReduce) {
            didReduce = singleChildParentOnePass(graph);
        }
    }

    private boolean singleChildParentOnePass(Graph graph) {
        int iAttackStep = 0;
        boolean didReduce = false;
        while (iAttackStep < graph.size()) {
            AttackStep attackStep = graph.getAttackStep(iAttackStep);
            if (singleParent(attackStep, graph))
                didReduce = true;
            iAttackStep++;
        }
        return didReduce;
    }

    // TODO careful, the reduction has changed with the refactoring of the "singleParent" method
    // See test case TransformationTest.reduceThreeAS(...)
    private boolean singleParent(AttackStep attackStep, Graph graph) {
        if (attackStep.getExpectedParents().size() == 1) {
            AttackStep parent = attackStep.getExpectedParents().iterator().next();
            // No need to call ancestry and progeny here, right? because
            if (!(attackStep instanceof AttackStepMax && ((AttackStepMax)attackStep).isLocked()) &&
                    (parent.getExpectedParents().size() > 0 || attackStep.getChildren().size() > 0)
                    && !parent.isExitStep() && !attackStep.isEntryStep()
                    && (parent.isMidStep() || attackStep.isMidStep())) { // one of the nodes must be normal
                if (parent.getChildren().size() == 1) {
                    OutputUtils.printVerbose("Found single-parent-single-child: " + parent.getName() + "-" + attackStep.getName());
                    edgeContractionBetweenChildAndParent(attackStep, graph, parent);
                    return true;
                } else if (attackStep.getLocalTtc() == 0) {
                    OutputUtils.printVerbose("Found child " + attackStep.getName() + " with localTTC==0 and single parent: " + parent.getName() + ".");
                    edgeContractionBetweenChildAndParent(attackStep, graph, parent);
                    return true;
                }
            }
        }
        return false;
    }

    private void edgeContractionBetweenChildAndParent(AttackStep child, Graph graph, AttackStep parent) {
        OutputUtils.printVerbose("Merging " + parent.getName() + " with " + child.getName() + " (singleChildWithSingleParent)");
        parent.setLocalTtc(parent.getLocalTtc() + child.getLocalTtc());
        parent.addLocalTtcDistributions(child.getLocalTtcDistributions());
        parent.setOrdinalLocalTtc(plus(parent.getOrdinalLocalTtc(), child.getOrdinalLocalTtc()));
        parent.setName(parent.getName() + "-" + child.getName());
        parent.mergeWith(child);
        parent.removeChild(child);

        new HashSet<>(child.getChildren()).stream().filter(gchi -> !gchi.equals(parent)).
                forEach(grandChild -> {
                    child.removeChild(grandChild);
                    parent.connectToChild(grandChild);
                });

        if (child.isExitStep()) {
            parent.setOrder(Order.EXITSTEP);
        }
        graph.clearAttackStep(child);
    }
}
