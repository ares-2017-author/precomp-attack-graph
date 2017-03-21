package attackGraph;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import datatypes.OrdinalTtcValue;
import org.apache.commons.math3.distribution.AbstractRealDistribution;

import support.DFS;
import support.OutputUtils;

public abstract class AttackStep extends Observable {

   private static Random            random                  = new Random();
   private int                      randomSeed;
   private String                   name;
   private Set<Integer>             idSet                   = new HashSet<>();
   private Order                    order;

   private boolean                  hasUpdatedItsChildren   = false;
   private double                   ttc;
   private double                   defaultTtc;
   private double                   extremeTtcSoFar;
   private OrdinalTtcValue          ordinalTtc;
   private OrdinalTtcValue          extremeOrdinalTtcSoFar;

   private double                   localTtc;
   private OrdinalTtcValue          ordinalLocalTtc;
   private Set<AbstractRealDistribution> localTtcDistributions = new HashSet<>();

   private Bucket                   bucket_;
   private Set<AttackStep>          children                = new HashSet<>();

   private Set<AttackStep>          remainingParents;
   private Set<AttackStep>          remainingOrdinalParents = new HashSet<>();
   private boolean                  hasParentUninfluencedBySource;
   private Set<AttackStep>          expectedParents         = new HashSet<>();

   private Set<AttackStep>          descendants             = new HashSet<>();

   AttackStep(String name, AbstractRealDistribution localTtcDistribution, Order order) {
      this(name, Sets.newHashSet(localTtcDistribution),order);
   }

   AttackStep(String name, Set<AbstractRealDistribution> localTtcDistributions, Order order) {
      this.name = name;
      idSet.add(random.nextInt());
      randomSeed = this.name.hashCode();
      for (AbstractRealDistribution ard: localTtcDistributions) {
         ard.reseedRandomGenerator(randomSeed);
         this.localTtcDistributions.add(ard);
      }
      this.order = order;
      initTtc();
      hardReset();
   }

   protected abstract void initTtc();

   public void softReset() {
      // setOrdinalTtc(OrdinalTtc.getOrdinalTtcCorrespondingToReal(defaultTtc()));
      remainingParents = new HashSet<>(expectedParents);

      setTtc(getDefaultTtc());
      setExtremeTtcSoFar(getDefaultTtc());
      ordinalReset();
      // setExtremeOrdinalTtcSoFar(OrdinalTtc.getOrdinalTtcCorrespondingToReal(extremeTtcSoFar));
   }

   public void ordinalReset() {
      bucket_ = null;
      setOrdinalTtc(OrdinalTtcValue.UNDEFINED);
      setExtremeOrdinalTtcSoFar(OrdinalTtcValue.UNDEFINED);
      remainingOrdinalParents.clear();
      hasUpdatedItsChildren = false;
   }

   public void hardReset() {
      softReset();
      // The reseeding means that each reset will also reset the pseudo-random sequence
      this.getLocalTtcDistributions().stream().forEach(ltd -> ltd.reseedRandomGenerator(randomSeed));
   }

   public void mergeWith(AttackStep attackStep) {
      this.idSet.addAll(attackStep.idSet);
      OutputUtils.printVeryVerbose("Merging " + attackStep.getName() + " with id's "
              + attackStep.idSet + " into " + this.getName() + ", with id's " + idSet);
   }

   public boolean isEquivalentTo(AttackStep attackStep) {
      return attackStep.getIdSet().stream().anyMatch(idSet::contains);
   }

   /**
    * Sample the local TTC distribution of the attackstep and converts the sampled value to an ordinal value
    * UNDEFINED: the attackstep's TTC distribution has not been sampled yet
    * ZERO: the sampled TTC is equal to 0
    * INFINITE: the sampled TTC is infinite (Double's max value), meaning unachievable by the attacker
    * ANY: the sampled TTC is > 0 and not infinite
    */
   public void sample() {
      // if (this.order == Order.ENTRYSTEP) {
      // setLocalTtc(0);
      // }
      // else {
      setLocalTtc(getLocalTtcDistributions().stream().mapToDouble(ltd -> ltd.sample()).sum());
      // }
      if (getLocalTtc() == 0) {
         setOrdinalLocalTtc(OrdinalTtcValue.ZERO);
      }
      else {
         if (getLocalTtc() == Double.MAX_VALUE) {
            setOrdinalLocalTtc(OrdinalTtcValue.INFINITE);
         }
         else {
            setOrdinalLocalTtc(OrdinalTtcValue.ANY);
         }
      }
      OutputUtils.printVeryVerbose(" The TTC of " + this.name + " is sampled to " + getLocalTtc()
              + " (" + getOrdinalLocalTtc() + ").");
   }

   protected abstract boolean isExtreme(double totalTtc);

   public void setRemainingOrdinalParents(AttackStep source) {
      remainingOrdinalParents.clear();
      remainingOrdinalParents.addAll(getExpectedParents().stream()
              .filter(p -> source.getDescendants().contains(p) || p.equals(source)).collect(Collectors.toSet()));

      hasParentUninfluencedBySource = !remainingOrdinalParents.containsAll(getExpectedParents()) || isEntryStep();
   }

   public void connectToChild(AttackStep child) {
      if (child != null) {
         getChildren().add(child);
         child.addExpectedParent(this);
      }
   }

   private void addExpectedParent(AttackStep parent) {
      expectedParents.add(parent);
      remainingParents.add(parent);
   }

   public void removeRemainingParent(AttackStep parent) {
      if (remainingParents.contains(parent)) {
         remainingParents.remove(parent);
      }
   }

   public void removeRemainingOrdinalParent(AttackStep parent) {
      if (remainingOrdinalParents.contains(parent)) {
         remainingOrdinalParents.remove(parent);
      }
   }

   public void clear() {
      setChanged();
      this.notifyObservers(-1);
      Set<AttackStep> children = new HashSet<>(getChildren());
      Set<AttackStep> parents = new HashSet<>(getExpectedParents());
      children.forEach(this::removeChild);
      parents.forEach(this::removeExpectedParent);

      remainingParents.clear();
      remainingOrdinalParents.clear();
      expectedParents.clear();
      this.deleteObservers();
   }

   /**
    * Returns ancestors that originate in source.
    * @param source the source AttackStep
    * @return Set of ancestors AttackStep
     */
   public Set<AttackStep> ancestorsTo(AttackStep source) {
      Set<AttackStep> ancestors = DFS.depthFirstAncestorsTo(this,source);//source, new HashSet<>());
      ancestors.remove(this);
      return ancestors;
   }

   /**
    * Get All ancestors of this.
    * @return A list of attack steps, the ancestors of this.
     */
   public Set<AttackStep> getAncestors() {
      Set<AttackStep> ancestors = DFS.depthFirstAncestorsOf(this);
      ancestors.remove(this);
      return ancestors;
   }

   public Set<AttackStep> getDescendants() {
      return descendants;
   }

   /**
    * Get all descendants from which target is reachable
    * @param target
    * @return
     */
   public Set<AttackStep> descendantsTo(AttackStep target) {
      Set<AttackStep> progeny = DFS.depthFirstDescendantsTo(this,target);//source, new HashSet<>());
      progeny.remove(this);
      return progeny;
   }

   /**
    * Get All descendants
    * @return
     */
   public Set<AttackStep> getProgenyI() {
      Set<AttackStep> progeny = DFS.depthFirstDescendantsOf(this);
      progeny.remove(this);
      return progeny;
   }

   public Set<AttackStep> getProgenyR() {
      Set<AttackStep> progeny = progenyRecurser(new HashSet<>());
      progeny.remove(this);
      return progeny;
   }

   private Set<AttackStep> progenyRecurser(Set<AttackStep> previouslyCollectedProgeny) {
      Set<AttackStep> prog = new HashSet<>();
      prog.addAll(previouslyCollectedProgeny);
      prog.add(this);
      for (AttackStep child : getChildren()) {
         if (!prog.contains(child)) {
            prog.addAll(child.progenyRecurser(prog));
         }
      }
      return prog;
   }

   /**
    * Retrieve the expected parents of "This" that are direct/indirect children of source
    * I.E. parents of this that are descendants of source
    * @param source attack step
    * @return list of parents of "this" connected to "source"
     */
   public HashSet<AttackStep> getParentsConnectedToSource(AttackStep source) {

      HashSet<AttackStep> sourceConnectedParents = new HashSet<>(source.getDescendants());
      sourceConnectedParents.add(source);
      sourceConnectedParents.retainAll(getExpectedParents());
      return  sourceConnectedParents;
   }


   /* Getters and Setters */

   public abstract void updateOrdinalTtc();

   public abstract void updateExtremeOrdinalTtcSoFar(OrdinalTtcValue totalOrdinalTtc);

   public double getTtc() {
      return ttc;
   }

   public void setTtc(double ttc) {
      this.ttc = ttc;
   }

   public double getDefaultTtc() {
      return defaultTtc;
   }

   public Bucket getBucket() {
      return bucket_;
   }

   public void setBucket(Bucket bucket) {
      bucket_ = bucket;
   }

   public void resetBucket() {
      bucket_ = null;
   }

   void setDefaultTtc(double defaultTtc) {
      this.defaultTtc = defaultTtc;
   }

   public boolean noRemainingParents() {
      return remainingParents.size() == 0;
   }

   public boolean noRemainingOrdinalParents() {
      return remainingOrdinalParents.size() == 0;
   }

   public boolean isSource() {
      return getOrdinalTtc() == OrdinalTtcValue.SOURCE;
   }

   public boolean isGTESource() {
      return getOrdinalTtc() == OrdinalTtcValue.GTESOURCE;
   }

   public boolean isLTESource() {
      return getOrdinalTtc() == OrdinalTtcValue.LTESOURCE;
   }

   public boolean isZero() {
      return getOrdinalTtc() == OrdinalTtcValue.ZERO;
   }

   public boolean isInfinite() {
      return getOrdinalTtc() == OrdinalTtcValue.INFINITE;
   }

   public boolean isUndefined() {
      return getOrdinalTtc() == OrdinalTtcValue.UNDEFINED;
   }

   public boolean isAny() {
      return getOrdinalTtc() == OrdinalTtcValue.ANY;
   }

   public double getLocalTtc() {
      return localTtc;
   }

   public void setLocalTtc(double localTtc) {
      this.localTtc = localTtc;
   }

   public void addLocalTtcDistributions(Set<AbstractRealDistribution> localTtcDistributions) {
      this.localTtcDistributions.addAll(localTtcDistributions);
   }

   public void setLocalTtcDistributions(Set<AbstractRealDistribution> localTtcDistributions) {
      this.localTtcDistributions = localTtcDistributions;
   }

   public void setLocalTtcDistribution(AbstractRealDistribution localTtcDistribution) {
      this.localTtcDistributions = Sets.newHashSet(localTtcDistribution);
   }

   public OrdinalTtcValue getOrdinalLocalTtc() {
      return ordinalLocalTtc;
   }

   public void setOrdinalLocalTtc(OrdinalTtcValue ordinalLocalTtc) {
      this.ordinalLocalTtc = ordinalLocalTtc;
   }

   public double getExtremeTtcSoFar() {
      return extremeTtcSoFar;
   }

   public void setExtremeTtcSoFar(double extremeTtcSoFar) {
      this.extremeTtcSoFar = extremeTtcSoFar;
   }

   public OrdinalTtcValue extremeOrdinalTtcSoFar() {
      return extremeOrdinalTtcSoFar;
   }

   void setExtremeOrdinalTtcSoFar(OrdinalTtcValue extremeOrdinalTtcSoFar) {
      this.extremeOrdinalTtcSoFar = extremeOrdinalTtcSoFar;
   }

   public boolean isEntryStep() {
      return (order == Order.ENTRYSTEP || order == Order.ENTRYANDEXITSTEP);
   }

   public boolean isMidStep() {
      return (order == Order.MIDSTEP);
   }

   public boolean isExitStep() {
      return (order == Order.EXITSTEP || order == Order.ENTRYANDEXITSTEP);
   }

   public Order getOrder() {
      return order;
   }

   public void setOrder(Order order) {
      this.order = order;
   }

   public Set<AttackStep> getChildren() {
      return children;
   }

   public Set<AttackStep> getExpectedParents() {
      return expectedParents;
   }

   public OrdinalTtcValue getOrdinalTtc() {
      return ordinalTtc;
   }

   public void setOrdinalTtc(OrdinalTtcValue ordinalTtc) {
      this.ordinalTtc = ordinalTtc;
   }

   private void removeExpectedParent(AttackStep as) {
      expectedParents.remove(as);
      remainingParents.remove(as);
      // TODO can an attackstep go from no parents to one parent? I don't think so?
      setChanged();
      notifyObservers(this);
      if (as.getChildren().contains(this)) {
         as.removeChild(this);
      }
   }

   public void removeChild(AttackStep child) {
      children.remove(child);
      child.removeExpectedParent(this);
   }

   @Override
   public String toString() {
      return "AS-"+name;
   }

   public int getRandomSeed() {
      return randomSeed;
   }

   public void setRandomSeed(int randomSeed) {
      this.randomSeed = randomSeed;
   }

   public Set<AbstractRealDistribution> getLocalTtcDistributions() {
      return localTtcDistributions;
   }

   public static void reseedIdGenerator(int randomSeed) {
      random.setSeed(randomSeed);
   }

   public boolean hasParentUninfluencedBySource() {
      return hasParentUninfluencedBySource;
   }

   public void setHasParentUninfluencedBySource(boolean hasParentUninfluencedBySource) {
      this.hasParentUninfluencedBySource = hasParentUninfluencedBySource;
   }

   public void setHasUpdatedItsChildren(boolean hasUpdatedItsChildren) {
      this.hasUpdatedItsChildren = hasUpdatedItsChildren;
   }


   public String getName() {
      return name;
   }

   public Set<Integer> getIdSet() {
      return idSet;
   }

   public void setIdSet(Set<Integer> idSet) {
      this.idSet.clear();
      this.idSet.addAll(idSet);
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean hasUpdatedItsChildren() {
      return hasUpdatedItsChildren;
   }

   public void addDescendants(Set<AttackStep> descendants) {
      this.descendants.addAll(descendants);
   }

   public void clearDescendants() {
      this.descendants.clear();
   }

}
