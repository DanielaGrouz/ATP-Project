package algorithms.search;

import java.util.*;

public class DepthFirstSearch extends ASearchingAlgorithm {

    private Stack<AState> stack;

    public DepthFirstSearch(){
        name = "Depth First Search";
        stack = new Stack<>();
    }

    @Override
    public Solution solve(ISearchable problem) {
        if (problem==null){
            return null;
        }
        start = problem.getStartState();
        goal = problem.getGoleState();

        stack.push(start);
        visitedNodes.add(start);
        AState currState;
        while (!stack.isEmpty()) {
            currState = stack.pop();
            if (visitedNodes.contains(currState)){ continue; }
            visitedNodes.add(currState);

            if (currState.equals(goal)) {
                buildSolution();
                break;
            }
            for (AState neighbor : problem.getAllPossibleStates(currState)) {
                if (!visitedNodes.contains(neighbor)) {
                    neighbor.setCameFrom(currState);
                    stack.push(neighbor);
                }
            }

        }

        return solution;
    }



    /*private Map<Integer, List<Integer>> adjList;

    public DepthFirstSearch() {
        adjList = new HashMap<>();
    }

    public void addEdge(int u, int v) {
        adjList.putIfAbsent(u, new ArrayList<>());
        adjList.putIfAbsent(v, new ArrayList<>());
        adjList.get(u).add(v);
        adjList.get(v).add(u); // כי החיבור הוא דו-כיווני
    }

    // חיפוש DFS
    public void depthFirstSearch(int start) {
        Set<Integer> visited = new HashSet<>();
        dfsHelper(start, visited);
    }

    private void dfsHelper(int node, Set<Integer> visited) {
        visited.add(node);
        System.out.print(node + " ");

        for (int neighbor : adjList.get(node)) {
            if (!visited.contains(neighbor)) {
                dfsHelper(neighbor, visited);
            }
        }
    }*/
}


