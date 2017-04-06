package attackgraph;

import datatypes.Order;
import datatypes.OrdinalTtcValue;
import org.apache.commons.math3.distribution.AbstractRealDistribution;

import computation.OrdinalOperations;
import support.OutputUtils;

import java.util.Set;

public class AttackStepMin extends AttackStep {

   public AttackStepMin(String name, AbstractRealDistribution localTtcDistribution, Order order) {
      super(name, localTtcDistribution, order);
   }

   public AttackStepMin(String name, Set<AbstractRealDistribution> localTtcDistributions, Order order) {
      super(name, localTtcDistributions, order);
   }

   @Override
   protected void initTtc() {
      setDefaultTtc(Double.MAX_VALUE);
   }

   // Make sure no step is called more than once!!

   @Override
   public boolean isExtreme(double totalTtc) {
      return getExtremeTtcSoFar() > totalTtc;
   }

   @Override
   public void updateOrdinalTtc() {
      setOrdinalTtc(OrdinalOperations.min(extremeOrdinalTtcSoFar(),OrdinalTtcValue.ANY));
   }

   @Override
   public void updateExtremeOrdinalTtcSoFar(OrdinalTtcValue totalOrdinalTtc) {
      OutputUtils.printVeryVerbose("Lowest ordinal TTC for " + getName() + " was " + extremeOrdinalTtcSoFar() + ".");
      setExtremeOrdinalTtcSoFar(OrdinalOperations.min(extremeOrdinalTtcSoFar(),totalOrdinalTtc));
      OutputUtils.printVeryVerbose("Lowest ordinal TTC for " + getName() + " is updated to " + extremeOrdinalTtcSoFar() + ".");
   }

   public boolean hasAMaxDecisiveAncestor() {
      AttackStepMin current = this;
      if (getExpectedParents().size() == 0) return false;
      while(!(current.getDecisiveParent() == null)) {
         if (current.getDecisiveParent() instanceof AttackStepMax)
            return true;
         current = (AttackStepMin) current.getDecisiveParent();
      }
      return false;
   }


}
