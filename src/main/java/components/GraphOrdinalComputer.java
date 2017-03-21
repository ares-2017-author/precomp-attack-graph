package components;

import attackGraph.*;
import datatypes.OrdinalTtcValue;
import support.OrdinalOperations;
import support.OutputUtils;

public class GraphOrdinalComputer {

    public void ordinalCompute(Graph graph, AttackStep source) {

        graph.ordinalReset();

        int nReachedNodes = 0;
        OrdinalBucketList bucketList = new OrdinalBucketList();

        if (!(source instanceof Defense)) {
            source.setOrdinalTtc(OrdinalTtcValue.SOURCE);
        } else {
            OutputUtils.printVerbose("Source is defense");
        }
        bucketList.append(source);

        // we only need the source's descendants and then compare the lists!
        // but do we need ancestors somewhere?
        graph.computeDescendantsOf(source);

        // only keep parents that are descendants of source
        source.getDescendants().stream().forEach(as -> as.setRemainingOrdinalParents(source));
        source.setRemainingOrdinalParents(source);
        AttackStep current;

        // TODO Only iterate until ANY is reached. After that, improvements cannot be made.
        while (!bucketList.isEmpty()) {
            current = bucketList.dialsRemove();
            nReachedNodes++;
            // Update the TTC of the selected node's children
            OutputUtils.printVeryVerbose(nReachedNodes + ": Updating attackStep " + current.getName());
            ordinalUpdateChildren(current, bucketList);
        }
        OutputUtils.mathematicaPlot(graph, 5);
    }

    private void ordinalUpdateChildren(AttackStep parent, OrdinalBucketList bucketList) {
        OutputUtils.printVeryVerbose("Ordinal: AttackStep " + parent.getName() + " has " + parent.getChildren().size() + " children.");
        for (AttackStep child : parent.getChildren()) {
            // To reduce computational time, we should stop graph update when
            // all parents are ANY (or UNDEFINED?).
            // So if an attack step reaches this point and is ANY
            // (can it be UNDEFINED? I don't think so.), we could reach down
            // into its children and reduce the noRemainingOrdinalParents by one,
            // and set hasParentUninfluencedBySource to true. Then, if another
            // parent of that child calls the child, it will assume that this
            // parent is ANY (I think), which is precisely what it is.
            // I tried this, but the performance improvement is negligible, so
            // commented it out below.
            if (parent.isAny()) {
                child.removeRemainingOrdinalParent(parent);
                parent.setHasParentUninfluencedBySource(true);
            } else {
                ordinalUpdateTtc(child, parent, bucketList);
            }
        }
    }

    private void ordinalUpdateTtc(AttackStep target, AttackStep caller, OrdinalBucketList bucketList) {
        OutputUtils.printVeryVerbose("Calling " + target.getName() + " (ttc=" + target.getOrdinalTtc() + ") from "
                + caller.getName() + " (ttc=" + caller.getOrdinalTtc() + ").");
        OrdinalTtcValue ordinalTtcIn = caller.getOrdinalTtc();
        OrdinalTtcValue totalOrdinalTtc = OrdinalOperations.plus(ordinalTtcIn,target.getOrdinalLocalTtc());
        if (!target.extremeOrdinalTtcSoFar().equals(totalOrdinalTtc)) {
            target.updateExtremeOrdinalTtcSoFar(totalOrdinalTtc);
        }
        target.removeRemainingOrdinalParent(caller);

        // TODO Is this condition, of noRemainingOrdinalParents, really correct for AttackStepMins?
        // AVE: I believe it is fine. It is actually better. Since all ordinal parents are connected to Source,
        //      it saves execution time to wait, for each node (AND and OR), that all parents have been visited.
        //    However, how can a target have no remainingOParent and has
        if (target.noRemainingOrdinalParents() && !target.hasUpdatedItsChildren()) {
            if (target.hasParentUninfluencedBySource()) {
                OutputUtils.printVeryVerbose("Updating ttc of " + target.getName() + " from " + target.getOrdinalTtc()
                        + " to " + OrdinalOperations.min(target.extremeOrdinalTtcSoFar(),OrdinalTtcValue.ANY) + ".");
                target.updateOrdinalTtc();
            } else {
                OutputUtils.printVeryVerbose("Updating ttc of " + target.getName() + " from " + target.getOrdinalTtc()
                        + " to " + target.extremeOrdinalTtcSoFar() + ".");
                target.setOrdinalTtc(target.extremeOrdinalTtcSoFar());
            }

            bucketList.remove(target);
            // When updated, the attack step should be shifted, or simply
            // added, to the proper bucket.
            // However, I had problems with loops previously, where attack
            // steps were added and dialsRemoved ad infinitum.
            // In the original algorithm, such loops were avoided as the
            // update terminated if the calling ttc surpassed the currently stored.
            // However, the current algorithm doesn't have that check.
            // A viable solution is perhaps to only allow a single update?
            // That has now been implemented.
            bucketList.append(target);
            target.setHasUpdatedItsChildren(true);
        }

    }

}
