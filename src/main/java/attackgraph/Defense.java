package attackgraph;

import datatypes.Order;
import datatypes.OrdinalTtcValue;
import org.apache.commons.math3.distribution.ConstantRealDistribution;

public class Defense extends AttackStepMin {

   private boolean isEnabled;
   private boolean isInitializedAsExitStep;

   public Defense(String name, boolean isEnabled, boolean isInitializedAsExitStep) {
      super(name, new ConstantRealDistribution(0.0), Order.ENTRYSTEP);
      this.isEnabled = isEnabled;
      this.isInitializedAsExitStep = isInitializedAsExitStep;
      if (isEnabled) {
         setLocalTtcDistribution(new ConstantRealDistribution(Double.MAX_VALUE));
         if (isInitializedAsExitStep)
            setOrder(Order.ENTRYANDEXITSTEP);
         else
            setOrder(Order.MIDSTEP);
      }
      else {
         if (isInitializedAsExitStep)
            setOrder(Order.ENTRYANDEXITSTEP);
      }
   }

   @Override
   public void ordinalReset() {
      super.ordinalReset();
      if (isEnabled) {
         setOrdinalTtc(OrdinalTtcValue.INFINITE);
         setExtremeOrdinalTtcSoFar(OrdinalTtcValue.INFINITE);
      }
      else {
         setOrdinalTtc(OrdinalTtcValue.ZERO);
         setExtremeOrdinalTtcSoFar(OrdinalTtcValue.ZERO);
      }
   }

   public boolean isInitializedAsExitStep() {
      return isInitializedAsExitStep;
   }

   public void setExitStep(boolean isExitStep) {
      this.isInitializedAsExitStep = isExitStep;
   }

   public boolean isEnabled() {
      return isEnabled;
   }

   public void setEnabled(boolean enabled) {
      this.isEnabled = enabled;
   }

}
