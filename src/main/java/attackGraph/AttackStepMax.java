package attackGraph;

import datatypes.OrdinalTtcValue;
import org.apache.commons.math3.distribution.AbstractRealDistribution;

import support.OrdinalOperations;
import support.OutputUtils;

import java.util.Set;

public class AttackStepMax extends AttackStep {

   private AttackStep extremeParentSoFar;

   public AttackStepMax(String name, AbstractRealDistribution localTtcDistribution, Order order) {
      super(name, localTtcDistribution, order);
   }

   public AttackStepMax(String name, Set<AbstractRealDistribution> localTtcDistributions, Order order) {
      super(name, localTtcDistributions, order);
   }

   @Override
   protected void initTtc() {
      setDefaultTtc(0);
   }

   @Override
   public boolean isExtreme(double totalTtc) {
      return getExtremeTtcSoFar() < totalTtc;
   }

   @Override
   public void updateOrdinalTtc() {
      setOrdinalTtc(OrdinalOperations.max(extremeOrdinalTtcSoFar(), OrdinalTtcValue.ANY));
   }

   @Override
   public void updateExtremeOrdinalTtcSoFar(OrdinalTtcValue totalOrdinalTtc) {
      OutputUtils.printVeryVerbose("Highest ordinal TTC for " + getName() + " was " + extremeOrdinalTtcSoFar() + ".");
      setExtremeOrdinalTtcSoFar(OrdinalOperations.max(extremeOrdinalTtcSoFar(),totalOrdinalTtc));
      OutputUtils.printVeryVerbose("Highest ordinal TTC for " + getName() + " so far is " + extremeOrdinalTtcSoFar() + ".");
   }

   public void setExtremeParentSoFar(AttackStep extremeParentSoFar) {
      this.extremeParentSoFar = extremeParentSoFar;
   }

   public AttackStep getExtremeParentSoFar() {
      return extremeParentSoFar;
   }
}
