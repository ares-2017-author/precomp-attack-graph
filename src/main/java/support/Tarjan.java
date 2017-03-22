package support;

import attackgraph.AttackStep;

import java.util.*;

// code from https://github.com/1123/johnson/blob/master/src/main/java/jgraphalgos/tarjan/Tarjan.java

public class Tarjan {

    int index;
    Stack<AttackStep> stack;
    Map<AttackStep, Integer> indexMap;
    Map<AttackStep, Integer> lowLinkMap;

    public Tarjan() {
        this.index = 0;
        this.indexMap = new HashMap<>();
        this.lowLinkMap = new HashMap<>();
        stack = new Stack<>();
    }

    private void init() {
        this.index = 0;
        this.indexMap = new HashMap<>();
        this.lowLinkMap = new HashMap<>();
        stack = new Stack<>();
    }

    public Set<AttackStep> getSccOf(AttackStep v) {
        Set<Set<AttackStep>> results =  strongConnect(v,true);
        Set<AttackStep> sccOfV = new HashSet<>();
        for (Set<AttackStep> scc: results) {
            if (scc.contains(v)) {
                sccOfV = scc;
                break;
            }
        }
        sccOfV.remove(v);
        return sccOfV;
    }

    private Set<Set<AttackStep>> strongConnect(AttackStep v, boolean init) {
        if(init) init();
        indexMap.put(v, index);
        lowLinkMap.put(v, index);
        index++;
        stack.push(v);
        Set<Set<AttackStep>> result = new HashSet<>();
        for (AttackStep w : v.getProgenyI()) {
            if (indexMap.get(w) == null) {
                result.addAll(strongConnect(w,false));
                lowLinkMap.put(v, Math.min(lowLinkMap.get(v), lowLinkMap.get(w)));
            } else {
                if (stack.contains(w)) {
                    lowLinkMap.put(v, Math.min(lowLinkMap.get(v), indexMap.get(w)));
                }
            }
        }

        if (lowLinkMap.get(v).equals(indexMap.get(v))) {
            Set<AttackStep> sccList = new HashSet<>();
            while (true) {
                AttackStep w = stack.pop();
                sccList.add(w);
                if (w.equals(v)) {
                    break;
                }
            }
            if (sccList.size() > 1) { result.add(sccList); } // don't return trivial sccs in the form of single nodes.
        }
        return result;
    }

}
