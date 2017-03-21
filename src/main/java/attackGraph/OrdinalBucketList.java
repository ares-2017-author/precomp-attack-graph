package attackGraph;

import java.util.ArrayList;
import java.util.List;

import support.OutputUtils;
// TODO Refactor OrdinalBucketList by extending BucketList?
// but there seems to be some variations in their respective code
public class OrdinalBucketList {
   private int          nAttackSteps = 0;
   private List<Bucket> buckets;
   private int          nBuckets;

   public OrdinalBucketList() {
      // buckets[0] is for all attack steps that have ttc==0.            --> ZERO
      // buckets[1] is for all attack steps that have ttc<=source.ttc(). --> LTESOURCE
      // buckets[2] is for all attack steps that have ttc==source.ttc(). --> SOURCE
      // buckets[3] is for all attack steps that have ttc>=source.ttc(). --> GTESOURCE
      // buckets[4] is for all attack steps that have ttc==infinity.     --> INFINITE
      // buckets[5] is for all attack steps that have ttc==anything.     --> ANY
      // buckets[6] is for all attack steps that have ttc==undefined.    --> UNDEFINED
      this.nBuckets = 7;
      buckets = new ArrayList<>(nBuckets);
      for (int i = 0; i < nBuckets; i++) {
         Bucket bucket = new Bucket(i);
         buckets.add(bucket);
      }
   }

   public void append(AttackStep attackStep) {
      buckets.get(attackStep.getOrdinalTtc().ordinal()).append(attackStep);
      OutputUtils.printVeryVerbose("Added " + attackStep.getName() + " to bucket " + attackStep.getOrdinalTtc().ordinal());
      nAttackSteps++;
   }

   public void remove(AttackStep attackStep) {
      Bucket bucket = attackStep.getBucket();
      if (bucket != null) {
         bucket.remove(attackStep);
         nAttackSteps--;
      }
   }

   /**
    * Buckets are emptied in an ordered fashion, from those with low values to those with high.
    * The name alludes to the creator of the algorithm, Mr. Dial.
    *
    * @return An arbitrary attack step from the lowest filled bucket.
    */
   public AttackStep dialsRemove() {
      for (int i = 0; i <= nBuckets + 1; i++) {
         if (!buckets.get(i).isEmpty()) {
            AttackStep extracted = buckets.get(i).extractFirst();
            nAttackSteps--;
            return extracted;
         }
      }
      return null;
   }

   public boolean isEmpty() {
      return nAttackSteps == 0;
   }

   public void reset() {
      nAttackSteps = 0;
      for (Bucket bucket : buckets) {
         bucket.reset();
      }
   }

}