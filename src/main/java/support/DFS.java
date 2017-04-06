package support;

import attackgraph.AttackStep;
import com.google.common.collect.Sets;

import java.util.*;

public class DFS {

    HashMap<AttackStep,Set<AttackStep>> grey;
    Set<AttackStep> results;
    HashMap<AttackStep,Boolean> black;

    /**
     * All ancestors between a target node and its potential ancestor s.
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
            // We don't want looped paths directly starting from source
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

    public Set<AttackStep> depthFirstDescendantsTo2(AttackStep s, AttackStep t) {
        grey = new HashMap<>();
        black = new HashMap<>();
        Set<AttackStep> descendants = new HashSet<>();
        for(AttackStep child: s.getChildren()) descendants.addAll(depthFirstTo(child,t, Sets.newHashSet(s), false));
        descendants.remove(s);
        return descendants;
    }

    public Set<AttackStep> depthFirstAncestorsTo2(AttackStep s, AttackStep t) {
        grey = new HashMap<>();
        black = new HashMap<>();
        Set<AttackStep> ancestors = new HashSet<>();
        for(AttackStep parent: s.getExpectedParents()) {
            ancestors.addAll(depthFirstTo(parent,t, Sets.newHashSet(s), true));
        }
        ancestors.remove(s);
        return ancestors;
    }

    private Set<AttackStep> depthFirstTo(AttackStep current, AttackStep t, Set<AttackStep> currentPath, boolean ascending) {
        // Found target!
        if (current.equals(t)) return currentPath;
        // loop? No, we don't want loops.
        if (currentPath.contains(current)) return new HashSet<>();

        // all the node's children have been visited already
        if (black.keySet().contains(current)) {
            // the node is in a path that leads to t
            if(black.get(current) == true) return currentPath;
            // no path including this node lead to t
            else return new HashSet<>();
        }

        // already being explored? let's way for the various outcomes then
        if (grey.keySet().contains(current)) {
            grey.get(current).addAll(currentPath);
            return new HashSet<>();
        }

        grey.put(current,new HashSet<>());
        Set<AttackStep> localVisited = new HashSet<>(currentPath);
        localVisited.add(current);
        Set<AttackStep> adjacentASs;
        if (ascending)
            adjacentASs = current.getExpectedParents();
        else
            adjacentASs = current.getChildren();

        Set<AttackStep> results = new HashSet<>();
        for (AttackStep adjNode : adjacentASs) {
            results.addAll(depthFirstTo(adjNode,t, localVisited, ascending));
        }
        if (results.isEmpty()) black.put(current,false);
        else {
            black.put(current, true);
            results.addAll(grey.get(current));
        }
        return results;
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
