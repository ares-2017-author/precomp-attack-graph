package components;


import attackGraph.*;
import datatypes.MaxNodesCalculationStrategies;
import support.Config;
import support.OutputUtils;

public class GraphComputer {
    BucketList bucketList;

    public GraphComputer () {}

    public void computeGraph(Graph graph) {

        int nReachedNodes = 0;
        this.bucketList = new BucketList(0, 1000, 4096);

        graph.zeroEntrySteps();

        for (AttackStep attackStep : graph.attackStepsAsList()) {
            if (attackStep instanceof Defense) {
                if (!((Defense) attackStep).isEnabled()) {
                    attackStep.setTtc(0);
                    attackStep.setExtremeTtcSoFar(0);
                    bucketList.append(attackStep);
                }
            }
        }

        graph.getEntrySteps().forEach(bucketList::append);

        AttackStep current;
        // Keep iterating over remaining nodes until the list of buckets is completely empty.
        while (!bucketList.isEmpty()) {
            try {
                current = bucketList.dialsRemove();
                if (current.getTtc() > 1000) {
                    // break;
                }
                nReachedNodes++;
                // Update the TTC of the selected node's children
                OutputUtils.printVeryVerbose(nReachedNodes + ": Updating attackStep " + current.getName());
                updateAttackStepChildren(current);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }

        }
    }

    private void updateAttackStepChildren(AttackStep as) {
        OutputUtils.printVeryVerbose("AttackStep " + as.getName() + " has " + as.getChildren().size() + " children.");
        for (AttackStep child : as.getChildren()) {
            updateTtc(child, as);
        }
    }

    private void updateTtcAndShiftBucket(AttackStep as) {
        OutputUtils.printVeryVerbose("Updating ttc of " + as.getName() + " from " + as.getTtc() + " to " + as.getExtremeTtcSoFar() + ".");
        as.setTtc(as.getExtremeTtcSoFar());
        bucketList.remove(as);
        bucketList.append(as);
    }

    private void updateTtc(AttackStep target, AttackStep caller) {
        if (target instanceof AttackStepMin) updateTtc((AttackStepMin)target,caller);
        else if (target instanceof AttackStepMax) updateTtc((AttackStepMax)target,caller);
    }

    private void updateTtc(AttackStepMin target, AttackStep caller) {
        if (!target.noRemainingParents()) {
            double totalTtc = caller.getTtc() + target.getLocalTtc();
            OutputUtils.printVeryVerbose("Calling " + target.getName() + " (ttc=" + target.getTtc() + ") from " + caller.getName() + " (ttc=" + caller.getTtc() + ").");
            if (target.isExtreme(totalTtc)) {
                target.setExtremeTtcSoFar(totalTtc);
                OutputUtils.printVeryVerbose("Most extreme TTC for " + target.getName() + " so far is " + target.getExtremeTtcSoFar() + ".");
                target.setDecisiveParent(caller);
                updateTtcAndShiftBucket(target);
            }
            target.removeRemainingParent(caller);
        }
    }

    private void updateTtc(AttackStepMax target, AttackStep caller) {
        if (!target.noRemainingParents()) {
            double totalTtc = caller.getTtc() + target.getLocalTtc();
            OutputUtils.printVeryVerbose("Calling " + target.getName() + " (ttc=" + target.getTtc() + ") from " + caller.getName() + " (ttc=" + caller.getTtc() + ").");
            if (target.isExtreme(totalTtc)) {
                target.setExtremeTtcSoFar(totalTtc);
                target.setExtremeParentSoFar(caller);
                OutputUtils.printVeryVerbose("Most extreme TTC for " + target.getName() + " so far is " + target.getExtremeTtcSoFar() + ".");
            }
            target.removeRemainingParent(caller);
            if (target.noRemainingParents()) {
                double finalTtc = 0;
                if (Config.MAX_NODES_CALCULATION_STRATEGIES.equals(MaxNodesCalculationStrategies.SUM)) {
                    for(AttackStep as: target.getExpectedParents()) {
                        finalTtc += as.getTtc();
                    }
                    finalTtc += target.getLocalTtc();
                } else if (Config.MAX_NODES_CALCULATION_STRATEGIES.equals(MaxNodesCalculationStrategies.MAX_1)) {
                    for(AttackStep as: target.getExpectedParents()) {
                        if (as.equals(target.getExtremeParentSoFar())) {
                            finalTtc += as.getTtc();
                        } else {
                            finalTtc += as.getLocalTtc();
                        }
                    }
                    finalTtc += target.getLocalTtc();
                } else {
                    finalTtc = target.getExtremeParentSoFar().getTtc() + target.getLocalTtc();
                }
                target.setExtremeTtcSoFar(finalTtc);
                updateTtcAndShiftBucket(target);
            }
        }
    }
}

