package attackGraph;

import java.util.LinkedList;
import java.util.List;

public class Bucket {
   private int              id;
   // A bucket is essentially a doubly linked list of attack steps
   private List<AttackStep> attackSteps = new LinkedList<>();

   public Bucket(int id) {
      this.id = id;
   }

   public boolean isEmpty() {
      return attackSteps.isEmpty();
   }

   public void append(AttackStep attackStep) {
      attackSteps.add(attackStep);
      attackStep.setBucket(this);
   }

   public AttackStep extractFirst() {
      AttackStep first = attackSteps.remove(0);
      first.resetBucket();
      return first;
   }

   public void remove(AttackStep attackStep) {
      attackSteps.remove(attackStep);
      attackStep.resetBucket();
   }

   @Override
   public String toString() {
      return String.format("Bucket %s=%s", id, attackSteps.toString());
   }

   public void reset() {
      attackSteps.forEach(AttackStep::resetBucket);
      attackSteps.clear();
   }
}
