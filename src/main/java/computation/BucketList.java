package computation;

import attackgraph.AttackStep;

import java.util.ArrayList;
import java.util.List;

public class BucketList {
   private int          nAttackSteps       = 0;
   private List<Bucket> buckets;
   private double       bucketSize;
   private int          nBuckets;
   private int          lowestFilledBucket = 0;

   public BucketList(double min, double max, int nBuckets) {
      this.nBuckets = nBuckets;
      this.bucketSize = (max - min) / (nBuckets);
      buckets = new ArrayList<>(nBuckets);
      for (int i = 0; i <= nBuckets + 1; i++) {
         Bucket bucket = new Bucket(i);
         buckets.add(bucket);
      }
   }

   public boolean isEmpty() {
      return nAttackSteps == 0;
   }

   public void append(AttackStep attackStep) {
      double ttc = attackStep.getTtc();
      int index = Integer.min((int) (ttc / bucketSize), nBuckets + 1);
      Bucket bucket = buckets.get(index);
      bucket.append(attackStep);
      // OutputUtils.print("Added " + attackStep.name() + " to bucket " + index);
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
    * Buckets are emptied in an ordered fashion, from those with low values to
    * those with high. The name alludes to the creator of the algorithm, Mr.
    * Dial.
    *
    * @return An arbitrary attack step from the lowest filled bucket.
    */
   public AttackStep dialsRemove() throws Exception {
      // OutputUtils.print("dialsRemove(). BucketList contains " + nAttackSteps + "
      // attackSteps.");
      for (int i = lowestFilledBucket; i <= nBuckets + 1; i++) {
         if (!buckets.get(i).isEmpty()) {
            // OutputUtils.print("Bucket " + i + " isn't empty. Checking.");
            AttackStep extracted = buckets.get(i).extractFirst();
            nAttackSteps--;
            return extracted;
         }
         else {
            lowestFilledBucket = i + 1;
         }
      }
      throw new Exception("Buckets empty but there is an attackstep somewhere??"+ buckets.stream()
              .filter(b -> !b.isEmpty()));
   }

   public void reset() {
      nAttackSteps = 0;
      for (Bucket bucket : buckets) {
         bucket.reset();
      }
      lowestFilledBucket = 0;
   }

   public Bucket getBucket(int aId) {
      return buckets.get(aId);
   }

   public List<Bucket> getBuckets() {
      return buckets;
   }
}