package support;

import java.util.Random;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * This will sample a gamma distribution in samplingThreshold % of the cases,
 * otherwise return 0.
 */
public class TestDistribution extends AbstractRealDistribution {

   /** Serialization ID */
   private static final long serialVersionUID = -4157746166772046273L;
   private Random            random           = new Random();
   private GammaDistribution gammaDistribution;
   private double            samplingThreshold;

   /** Constant value of the distribution */

   /**
    * Create a constant real distribution with the given value.
    */
   public TestDistribution(double samplingThreshold, double alpha, double beta) {
      super(null); // Avoid creating RandomGenerator
      this.samplingThreshold = samplingThreshold;
      gammaDistribution = new GammaDistribution(alpha, beta);
   }

   /** {@inheritDoc} */
   @Override
   public double density(double x) {
      return x == 1 ? 1 : 0;
   }

   /** {@inheritDoc} */
   @Override
   public double cumulativeProbability(double x) {
      return x < 1 ? 0 : 1;
   }

   @Override
   public double inverseCumulativeProbability(final double p) throws OutOfRangeException {
      if (p < 0.0 || p > 1.0) {
         throw new OutOfRangeException(p, 0, 1);
      }
      return 1;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public double getNumericalMean() {
      return 1;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public double getNumericalVariance() {
      return 0;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public double getSupportLowerBound() {
      return 1;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public double getSupportUpperBound() {
      return 1;
   }

   /** {@inheritDoc} */
   @Override
   public boolean isSupportLowerBoundInclusive() {
      return true;
   }

   /** {@inheritDoc} */
   @Override
   public boolean isSupportUpperBoundInclusive() {
      return true;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isSupportConnected() {
      return true;
   }

   /** {@inheritDoc} */
   @Override
   public double sample() {
      double r = random.nextFloat();
      if (r < samplingThreshold)
         return gammaDistribution.sample();
      else
         return 0.0;

   }

   /**
    * Override with no-op (there is no generator).
    *
    * @param seed
    *           (ignored)
    */
   @Override
   public void reseedRandomGenerator(long seed) {
      gammaDistribution.reseedRandomGenerator(seed);
      random.setSeed(seed);
   }

}

