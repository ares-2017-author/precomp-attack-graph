package support;

import attackGraph.AttackStep;

import java.util.*;

public class DFS {

    /**
     * All descendants between a target node and its potential descendant s.
     * Starts from s and then ascend to each parent, until reaching t.
     * @param s source node
     * @param t target node
     * @return all nodes that are part of a path between t and s
     */

    public static Set<AttackStep> depthFirstAncestorsTo(AttackStep s, AttackStep t) {
        return depthFirstToInit(s,t,true);
    }

    /**
     * All descendants between a source node and its potential descendant t.
     * Starts from s and then descend to each child, until reaching t.
     * @param s source node
     * @param t target node
     * @return all nodes that are part of a path between s and t
     */
    public static Set<AttackStep> depthFirstDescendantsTo(AttackStep s, AttackStep t) {
        return depthFirstToInit(s,t,false);
    }

    private static Set<AttackStep> depthFirstToInit(AttackStep s, AttackStep t, boolean ascending) {
        LinkedList<AttackStep> visited = new LinkedList<>();
        Set<AttackStep> matchingASs = new HashSet<>();
        visited.push(s);
        depthFirstTo(t,s,visited, matchingASs, ascending);
        matchingASs.remove(s);
        return matchingASs;
    }

    private static void depthFirstTo(AttackStep t, AttackStep s, LinkedList<AttackStep> visited, Set<AttackStep> matchingASs, boolean ascending) {
        AttackStep current = visited.getFirst();
        Set<AttackStep> adjacentASs;
        if (ascending)
            adjacentASs = current.getExpectedParents();
        else
            adjacentASs = current.getChildren();

        for (AttackStep adjNode : adjacentASs) {
            // We don't want looped paths
            if (adjNode.equals(s)) continue;
            if (visited.contains(adjNode) && !matchingASs.contains(adjNode)) {
                    continue;
            }
            if (adjNode.equals(t) || matchingASs.contains(adjNode)) {
                matchingASs.add(adjNode);
                matchingASs.addAll(visited);
                visited.removeAll(matchingASs);
                continue;
            }
            if ((visited.contains(adjNode) && !matchingASs.contains(adjNode)) || adjNode.equals(t)) {
                continue;
            }
            visited.push(adjNode);
            depthFirstTo(t, s, visited, matchingASs, ascending);
            if (!visited.isEmpty()) visited.pop();
        }
    }

    public static Set<AttackStep> depthFirstAncestorsOf(AttackStep as) {
        return depthFirstOfInit(as, true);
    }

    public static Set<AttackStep> depthFirstDescendantsOf(AttackStep as) {
        return depthFirstOfInit(as, false);
    }

    private static Set<AttackStep> depthFirstOfInit(AttackStep s, boolean ascending) {
        Set<AttackStep> visited = new HashSet<>();
        depthFirstOf(s,visited, ascending);
        // if cycle
        visited.remove(s);
        return visited;
    }

    private static void depthFirstOf(AttackStep s, Set<AttackStep> visited, boolean ascending) {
        Set<AttackStep> adjacentASs;
        if (ascending)
            adjacentASs = s.getExpectedParents();
        else
            adjacentASs = s.getChildren();

        for (AttackStep adjNode : adjacentASs) {
            if (visited.contains(adjNode)) {
                continue;
            } else {
                visited.add(adjNode);
                depthFirstOf(adjNode, visited, ascending);
            }
        }
    }

}
