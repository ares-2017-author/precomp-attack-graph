package support;

import attackGraph.AttackStep;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BFS {

    public static Set<AttackStep> breadthFirstAncestorsOf(AttackStep s) {
        Set<AttackStep> ancestors = breadthFirstSearchFrom(s,true);
        ancestors.remove(s);
        return ancestors;
    }

    public static Set<AttackStep> breadthFirstDescendantsOf(AttackStep s) {
        Set<AttackStep> descendants = breadthFirstSearchFrom(s,false);
        descendants.remove(s);
        return descendants;
    }

    public static Set<AttackStep> breadthFirstSearchFrom(AttackStep source, boolean ascending) {
        Set<AttackStep> visited = new HashSet<>();
        //if empty graph, then return.
        if (null == source) {
            return visited;
        }
        Queue<AttackStep> queue = new LinkedList<>();
        //add source to queue.
        queue.add(source);
        visited.add(source);
        while (!queue.isEmpty()) {
            AttackStep current = queue.poll();
            //Add all of unvisited neighbors to the queue. We add only unvisited nodes to avoid cycles.
            Set<AttackStep> adjacentASs;
            if (ascending)
                adjacentASs = current.getExpectedParents();
            else
                adjacentASs = current.getChildren();
            for (AttackStep child : adjacentASs) {
                if (!visited.contains(child)) {
                    visited.add(child);
                    queue.add(child);
                }
            }
        }
        return visited;
    }

}
