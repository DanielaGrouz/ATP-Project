package algorithms.search;

import java.util.*;

public class DepthFirstSearch extends ASearchingAlgorithm {

    private Stack<AState> stack;

    public DepthFirstSearch(){
        stack = new Stack<>();

    }

    @Override
    public Solution solve(ISearchable problem) {

        start = problem.getStartState();
        goal = problem.getGoleState();
        boolean finish = false;


        stack.push(start);
        while (!stack.isEmpty() && !finish) {
            AState currState = stack.pop();

            if (visitedNodes.contains(currState)){ continue; }

            visitedNodes.add(currState);
            if (currState.equals(goal)) {
                finish = true;
            }
            for (AState neighbor : problem.getAllPossibleStates(currState)) {
                if (!visitedNodes.contains(neighbor)) {
                    neighbor.cameFrom = currState;
                    stack.push(neighbor);
                }
            }

        }
        if (!finish){
            return null;
        }

        //finish is true
        buildSolution();
        return solution;


    }

    @Override
    public String getName() {
        return "Depth First Search";
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


