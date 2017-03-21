package support;

import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ConstantRealDistribution;
import org.junit.After;
import org.junit.Test;

import support.OutputUtils;
import support.TestDistribution;

public class SupportTest {

   @Test
   public void testTestDistribution() {
       OutputUtils.veryVerboseOn();
      TestDistribution testDistribution = new TestDistribution(.5, 10, 1);
      double mean = 0;
      double val;
      int n = 1000;
      for (int i = 1; i <= n; i++) {
         val = testDistribution.sample();
         mean += val / n;
         OutputUtils.printVeryVerbose("Sample: " + val);
      }
      OutputUtils.printVeryVerbose("Mean: " + mean);
      assertTrue(mean - 5 < 1);
   }

   @Test
   public void testTestDistribution2() {
      OutputUtils.veryVerboseOn();
      TestDistribution testDistribution = new TestDistribution(.9, 10, 1);
      double mean = 0;
      double val;
      int n = 1000;
      for (int i = 1; i <= n; i++) {
         val = testDistribution.sample();
         mean += val / n;
         OutputUtils.printVeryVerbose("Sample: " + val);
      }
      OutputUtils.printVeryVerbose("Mean: " + mean);
      assertTrue(mean - 9 < 1);
   }

   @Test
   public void testTestDistribution3() {
      OutputUtils.veryVerboseOn();
      TestDistribution testDistribution = new TestDistribution(.3, 10, 1);
      double mean = 0;
      double val;
      int n = 1000;
      for (int i = 1; i <= n; i++) {
         val = testDistribution.sample();
         mean += val / n;
         OutputUtils.printVeryVerbose("Sample: " + val);
      }
      OutputUtils.printVeryVerbose("Mean: " + mean);
      assertTrue(mean - 3 < 1);
   }

   @Test
   public void testTestDistribution4() {
      OutputUtils.veryVerboseOn();
      TestDistribution testDistribution = new TestDistribution(1, 20, 1);
      double mean = 0;
      double val;
      int n = 1000;
      for (int i = 1; i <= n; i++) {
         val = testDistribution.sample();
         mean += val / n;
         OutputUtils.printVeryVerbose("Sample: " + val);
      }
      OutputUtils.printVeryVerbose("Mean: " + mean);
      assertTrue(mean - 20 < 1);
   }

   @Test
   public void testConstantDistribution() {
      OutputUtils.veryVerboseOn();
      ConstantRealDistribution testDistribution = new ConstantRealDistribution(1);
      double mean = 0;
      double val;
      int n = 1000;
      for (int i = 1; i <= n; i++) {
         val = testDistribution.sample();
         mean += val / n;
         OutputUtils.printVeryVerbose("Sample: " + val);
      }
      OutputUtils.printVeryVerbose("Mean: " + mean);
      assertTrue(mean < 1.0001);
   }

   @Test
   public void testConstantDistribution2() {
      OutputUtils.veryVerboseOn();
      ConstantRealDistribution testDistribution = new ConstantRealDistribution(0);
      double mean = 0;
      double val;
      int n = 1000;
      for (int i = 1; i <= n; i++) {
         val = testDistribution.sample();
         mean += val / n;
         OutputUtils.printVeryVerbose("Sample: " + val);
      }
      OutputUtils.printVeryVerbose("Mean: " + mean);
      assertTrue(mean < 0.0001);
   }

   @Test
   public void testConstantDistribution3() {
      OutputUtils.veryVerboseOn();
      ConstantRealDistribution testDistribution = new ConstantRealDistribution(4.53);
      double mean = 0;
      double val;
      int n = 1000;
      for (int i = 1; i <= n; i++) {
         val = testDistribution.sample();
         mean += val / n;
         OutputUtils.printVeryVerbose("Sample: " + val);
      }
      OutputUtils.printVeryVerbose("Mean: " + mean);
      assertTrue(mean < 4.53001);
   }

   @After
   public void teardown () {OutputUtils.verboseOff();}
}
