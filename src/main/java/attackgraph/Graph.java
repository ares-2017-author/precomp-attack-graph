package attackgraph;

import java.util.*;

import computation.GraphComputer;
import computation.GraphOrdinalComputer;
import computation.GraphTransformer;
import datatypes.Order;
import org.apache.commons.math3.distribution.AbstractRealDistribution;

import support.OutputUtils;

public class Graph implements Observer {

   private List<AttackStep>         attackSteps             = new ArrayList<>();

   private GraphTransformer graphTransformer        = new GraphTransformer(this);
   private GraphComputer graphComputer           = new GraphComputer();
   private GraphOrdinalComputer graphOrdinalComputer    = new GraphOrdinalComputer();
   private static int               randomSeed              = 2;
   public static Random             rand                    = new Random(randomSeed);
   private String                   ownerComponent;
   private boolean                  upToDate                = false;
   private AttackStep               source;
   private int                      treeEdges = 0;
   private int                      forwardEdges = 0;
   private int                      backEdges = 0;
   private int                      crossEdges = 0;
   private int                      edgescount = 0;

   public Graph(String name) {this.ownerComponent = name;}

   public void sample() {
      // TODO Introduce defenses.
      // TODO We might also want to sample existence. But the introduction of defenses might be quite similar in effec.
      attackStepsAsSet().forEach(AttackStep::sample);
   }

   public void reduce() {
      graphTransformer.reduce(this);
   }

   public void compute() {graphComputer.computeGraph(this); }

   public void ordinalCompute(AttackStep source) { graphOrdinalComputer.ordinalCompute(this, source); }

   public void updateDescendantsOfSource() {
      if (!upToDate) {
         OutputUtils.printVerbose("Descendants of "+source.getName()+" are being updated");
         source.clearDescendants();
         source.addDescendants(source.getProgenyI());
         upToDate = true;
      }
   }

   public void computeDescendantsOf(AttackStep source) {
      upToDate = false;
      this.source = source;
      updateDescendantsOfSource();
   }

   /* Resets */

   public void softReset() {
      attackSteps.forEach(AttackStep::softReset);
   }

   public void ordinalReset() {
      attackSteps.forEach(AttackStep::ordinalReset);
   }

   // hardReset() also reseeds the PRNG.
   public void hardReset() {
      attackSteps.forEach(AttackStep::hardReset);
   }

   /* AttackStep Builders */

   public AttackStep newAttackStepMin(String name, AbstractRealDistribution localTtcDistribution, Order order) {
      AttackStepMin attackStep = new AttackStepMin(name, localTtcDistribution, order);
      addAttackStep(attackStep);
      return attackStep;
   }

   public AttackStep newAttackStepMax(String name, AbstractRealDistribution localTtcDistribution, Order order) {
      AttackStepMax attackStep = new AttackStepMax(name, localTtcDistribution, order);
      addAttackStep(attackStep);
      return attackStep;
   }

   public Defense newDefense(String name, boolean isEnabled, boolean isExitStep) {
      Defense defense = new Defense(name, isEnabled, isExitStep);
      addAttackStep(defense);
      return defense;
   }


   /* Getters and Setters */

   public boolean containsStep(AttackStep candidateAttackStep) {
      for (AttackStep existingAttackStep : attackStepsAsSet()) {
         if (candidateAttackStep.getName().equals(existingAttackStep.getName())) {
            return true;
         }
      }
      return false;
   }

   public AttackStep getAttackStep(String name) {
      for (AttackStep attackStep : attackStepsAsSet()) {
         if (attackStep.getName().equals(name)) {
            return attackStep;
         }
      }
      return null;
   }

   public AttackStep getEquivalentAttackStep(AttackStep mergedAttackStep) {
      for (AttackStep attackStep : attackStepsAsSet()) {
         if (attackStep.isEquivalentTo(mergedAttackStep)) {
            return attackStep;
         }
      }
      System.out.println("There is no cloned or reduced attack step with an id that matches " + mergedAttackStep.getName() + ".");
      return null;
   }

   public AttackStep getAttackStepContainingSubString(String subString) {
      for (AttackStep attackStep : attackStepsAsSet()) {
         if (attackStep.getName().matches(".*" + subString + ".*")) {
            return attackStep;
         }
      }
      throw new IllegalArgumentException("There is no attack step with the name " + subString + ".");
   }

   public List<AttackStep> getEntrySteps() {
      List<AttackStep> entrySteps = new ArrayList<>();
      for (AttackStep attackStep : attackSteps) {
         if (attackStep.isEntryStep()) {
            entrySteps.add(attackStep);
         }
      }
      return entrySteps;
   }

   public List<AttackStep> getMidSteps() {
      List<AttackStep> midSteps = new ArrayList<>();
      for (AttackStep attackStep : attackSteps) {
         if (attackStep.isMidStep()) {
            midSteps.add(attackStep);
         }
      }
      return midSteps;
   }

   public List<AttackStep> getExitSteps() {
      List<AttackStep> exitSteps = new ArrayList<>();
      for (AttackStep attackStep : attackSteps) {
         if (attackStep.isExitStep()) {
            exitSteps.add(attackStep);
         }
      }
      return exitSteps;
   }

   private void removeAttackStep(AttackStep as) {
      attackSteps.remove(as);
   }

   public void clearAttackStep(AttackStep as) {
      removeAttackStep(as);
      as.clear();
   }

   public void clearAttackSteps(Set<AttackStep> attackSteps) {
      for (AttackStep attackStep : attackSteps) {
         clearAttackStep(attackStep);
      }
   }

   public void zeroEntrySteps() {
// TODO remove this loop by keeping track of the entrysteps?
      for (AttackStep attackStep : attackSteps) {
         if (attackStep.isEntryStep()) {
            OutputUtils.printVeryVerbose("Zeroing TTC of " + attackStep.getName() + " to " + attackStep.getLocalTtc());
            attackStep.setTtc(attackStep.getLocalTtc());
            attackStep.setExtremeTtcSoFar(attackStep.getLocalTtc());
            attackStep.removeAllRemainingParent();
         }
         else {
            attackStep.setTtc(attackStep.getDefaultTtc());
            attackStep.setExtremeTtcSoFar(attackStep.getDefaultTtc());
         }
      }
   }

   public void integrateSubgraph(Graph subGraph) {
      subGraph.attackSteps.stream().forEach(as -> addAttackStep(as));
      hardReset();
   }

   /* For statistics */

   public float meanChildrenNbr() {
      int childrenCount = 0;
      for(AttackStep as : attackStepsAsSet()) childrenCount += as.getChildren().size();
      return ((float)childrenCount / attackStepsAsSet().size());
   }

   public int maxChildrenNbr() {
      int maxChildren = 0;
      for(AttackStep as : attackStepsAsSet())
         if (as.getChildren().size() > maxChildren)
            maxChildren = as.getChildren().size();
      return maxChildren;
   }

   /**
    * See formula for
    * @return
     */
   public float getGraphDensity() {
      int V = attackStepsAsSet().size();
      int E = getTotalNbrOfChildren();
      return ((float)E/ (V*(V-1)));
   }

   public int getTotalNbrOfChildren() {
      int maxChildren = 0;
      for(AttackStep as : attackStepsAsSet())
            maxChildren += as.getChildren().size();
      return maxChildren;
   }

   public int minChildrenNbr() {
      int minChildren = Integer.MAX_VALUE;
      for(AttackStep as : attackStepsAsSet())
         if (as.getChildren().size() < minChildren)
            minChildren = as.getChildren().size();
      return minChildren;
   }
   public float meanParentNbr() {
      int parentsCount = 0;
      for(AttackStep as : attackStepsAsSet()) parentsCount += as.getExpectedParents().size();
      return ((float)parentsCount / attackStepsAsSet().size());
   }

   public int maxParentsNbr() {
      int maxParents = 0;
      for(AttackStep as : attackStepsAsSet())
         if (as.getExpectedParents().size() > maxParents)
            maxParents = as.getExpectedParents().size();
      return maxParents;
   }

   public int minParentsNbr() {
      int minParents = Integer.MAX_VALUE;
      for(AttackStep as : attackStepsAsSet())
         if (as.getExpectedParents().size() < minParents)
            minParents = as.getExpectedParents().size();
      return minParents;
   }

   /* **************************************** */

   public int size() {
      return attackStepsAsSet().size();
   }

   public void addAttackStep(AttackStep attackStep) {
      attackStep.addObserver(this);
      attackSteps.add(attackStep);
   }

   public List<AttackStep> attackStepsAsList() {
      return attackSteps;
   }

   public Set<AttackStep> attackStepsAsSet() {
      return new HashSet<>(attackSteps);
   }

   public AttackStep getAttackStep(int index) {
      return attackSteps.get(index);
   }

   public String getOwnerComponentName() {
      return ownerComponent;
   }

   public void addAttackSteps(AttackStep... atts) {
      for (AttackStep as: atts) {
         addAttackStep(as);
      }
   }

   @Override
   public void update(Observable o, Object arg) {
      if (arg.equals(-1)) this.removeAttackStep((AttackStep) o);
      if (upToDate) {
         assert (o instanceof AttackStep);
         OutputUtils.printVerbose("Progenies have changed says " + o + "! updating graph " + getOwnerComponentName() + "...");
         this.upToDate = false;
      }
   }



   public int getTreeEdges() {
      return treeEdges;
   }

   public void incTreeEdges() {
      this.treeEdges++; this.edgescount++;
   }

   public int getForwardEdges() {
      return forwardEdges;
   }

   public void incForwardEdges() {
      this.forwardEdges++; this.edgescount++;
   }

   public int getBackEdges() {
      return backEdges;
   }

   public void incBackEdges() {
      this.backEdges++; this.edgescount++;
   }

   public int getCrossEdges() {
      return crossEdges;
   }

   public void incCrossEdges() {
      this.crossEdges++; this.edgescount++;
   }

   public float getMeanCrossEdges() {
      return  ((float)crossEdges/this.edgescount);
   }
   public float getMeanBackEdges() {
      return ((float)backEdges/this.edgescount);
   }
   public float getMeanForwardEdges() {
      return ((float)forwardEdges/this.edgescount);
   }
   public float getMeanTreeEdges() {
      return ((float)treeEdges/this.edgescount);
   }

   public int getEdgescount() {
      return edgescount;
   }


}
