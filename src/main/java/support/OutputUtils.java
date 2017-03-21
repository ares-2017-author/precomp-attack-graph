package support;

import attackGraph.AttackStep;
import attackGraph.AttackStepMax;
import attackGraph.Defense;
import components.Graph;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OutputUtils {

   static private boolean terseVerbose    = false;
   static private boolean verbose         = false;
   static private boolean veryVerbose     = false;
   static private boolean mathematicaPlot = false;

   static public void printVeryVerbose(String s) {
      if (veryVerbose) {
         System.out.println(s);
      }
   }

   static public void printVerbose(String s) {
      if (verbose || veryVerbose) {
         System.out.println(s);
      }
   }

   static public void printTerseVerbose(String s) {
      if (terseVerbose || verbose || veryVerbose) {
         System.out.println(s);
      }
   }

   static public void mathematicaPlot(Graph graph, int showLabels) {
      if (mathematicaPlot) {
         System.out.println(mathematicaPlotString(graph,showLabels, false));
      }
   }

   static public void partialMathematicaPlot(Set<AttackStep> attackSteps, int showLabels) {
      if (mathematicaPlot) {
         Graph subG = new Graph("loose");
         attackSteps.stream().forEach(as -> subG.addAttackStep(as));
         System.out.println(mathematicaPlotString(subG,showLabels, true));
      }
   }

   private static String mathematicaPlotString(Graph graph, int showLabels, boolean partial) {

      String edgeSetString = "{";
      String edgeString;
      String vertexLabelString = "";
      String vertexShapeFunctionString = "";
      String vertexStyleString = "";

      if (graph.attackStepsAsList().size() < 2)
         return "Graph is either empty or has only one node. Not worth drawing.";

      for (AttackStep parent : graph.attackStepsAsSet()) {
         if (parent.getChildren().size() != 0 || parent.getExpectedParents().size() != 0) {
            Set<AttackStep> children = new HashSet<>(parent.getChildren());
            if (partial) children = children.stream().filter(graph.attackStepsAsSet()::contains).collect(Collectors.toSet());
            for (AttackStep child : children) {
               edgeString = parent.getName() + " \\[DirectedEdge] " + child.getName() + ", ";
               edgeSetString = edgeSetString + edgeString;
            }
            String localTtcString = ttcString(parent.getLocalTtc());
            String ttcString = ttcString(parent.getTtc());

            switch (showLabels) {
               case 0:
                  if (parent.getLocalTtc() == 0) {
                     vertexLabelString = vertexLabelString + parent.getName() + "-> 0, ";
                  }
                  break;
               case 1:
                  // Only localTtc
                  vertexLabelString = vertexLabelString + parent.getName() + "-> \"" + localTtcString + " (" + parent.getName() + ")\", ";
                  break;
               case 2:
                  // ttc & localTtc
                  vertexLabelString = vertexLabelString + parent.getName() + "-> \"" + ttcString + "[" + localTtcString + "](" + parent.getName() + ")\", ";
                  break;
               case 3:
                  // localTtc & ordinalTtc
                  vertexLabelString = vertexLabelString + parent.getName() + "-> \"" + localTtcString + "/" + parent.getOrdinalTtc() + " (" + parent.getName() + ")\", ";
                  break;
               case 4:
                  // ttc, localTtc & ordinalTtc
                  vertexLabelString = vertexLabelString + parent.getName() + "-> \"" + ttcString + "[" + localTtcString + "] " + parent.getOrdinalTtc() + " (" + parent.getName() + ")\", ";
                  break;
               case 5:
                  // ordinalTtc & ordinalLocalTtc
                  vertexLabelString = vertexLabelString + parent.getName() + "-> \"" + parent.getOrdinalTtc() + "[" + parent.getOrdinalLocalTtc() + "] (" + parent.getName() + ")\", ";
                  break;
            }

            if (parent instanceof Defense) {
               vertexShapeFunctionString = vertexShapeFunctionString + parent.getName() + " -> \"FiveDown\", ";
            } else if (parent.isEntryStep()) {
               vertexShapeFunctionString = vertexShapeFunctionString + parent.getName() + " -> \"Diamond\", ";
               vertexStyleString = vertexStyleString + parent.getName() + " -> Red, ";
            } else if (parent.isExitStep()) {
               vertexShapeFunctionString = vertexShapeFunctionString + parent.getName() + " -> \"Star\", ";
               vertexStyleString = vertexStyleString + parent.getName() + " -> Red, ";
            } else if (parent instanceof AttackStepMax) {
               vertexShapeFunctionString = vertexShapeFunctionString + parent.getName() + " -> \"Square\", ";
            }
         }
      }

      edgeSetString = edgeSetString.substring(0, edgeSetString.length() - 2) + "}";
      if (vertexShapeFunctionString.length() > 24)
         vertexShapeFunctionString = "VertexShapeFunction -> {" + vertexShapeFunctionString.substring(0, vertexShapeFunctionString.length() - 2) + "}, ";

      vertexLabelString = "VertexLabels -> {" + vertexLabelString.substring(0, vertexLabelString.length() - 2) + "}";
      vertexStyleString = "VertexStyle -> {" + vertexStyleString.substring(0, vertexStyleString.length() - 2) + "}";
      String plotString = "Graph[" + edgeSetString;
      plotString = plotString + ", " + vertexLabelString;
      plotString = plotString + ", " + vertexStyleString;
      plotString = plotString + ", " + vertexShapeFunctionString + "VertexSize -> Large, EdgeShapeFunction -> GraphElementData[{\"CarvedArrow\", \"ArrowSize\" -> .02}]]";
      return plotString;
   }

   private static String ttcString(double ttc) {
      DecimalFormat df = new DecimalFormat("#");
      if (ttc == Double.MAX_VALUE)
         return "infinite";
      else
         return df.format(ttc);

   }

   static public void verboseOn() {
      verbose = true;
   }

   static public void terseVerboseOn() {
      terseVerbose = true;
   }

   static public void plotOn() {
      mathematicaPlot = true;
   }

   static public void plotOff() {
      mathematicaPlot = false;
   }

   static public void veryVerboseOn() {
      veryVerbose = true;
   }

   public static void verboseOff() {
      verbose = false;
      veryVerbose = false;
      terseVerbose = false;
   }

   public static boolean isPrintVerbose() {
      return verbose;
   }
}
