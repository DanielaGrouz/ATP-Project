package algorithms.search;

import java.util.*;

public class BreadthFirstSearch extends ASearchingAlgorithm {

    protected Queue<AState> queue;

    public BreadthFirstSearch(){
        createQueue();
        name = "Breadth First Search";
    }

    @Override
    public Solution solve(ISearchable problem) {
        if (problem==null){
            return solution; //empty solution
        }
        start = problem.getStartState();
        goal = problem.getGoleState();

        queue.add(start);
        AState currState;
        while (!queue.isEmpty()) {
            currState = queue.poll();
            if (visitedNodes.contains(currState)){ continue; }
            visitedNodes.add(currState);

            if (currState.equals(goal)) {
                buildSolution();
                break;
            }
            for (AState neighbor : problem.getAllPossibleStates(currState)) {
                if (!visitedNodes.contains(neighbor)) {
                    neighbor.setCameFrom(currState);
                    queue.add(neighbor);
                }
            }

        }

        return solution;

    }

    protected void createQueue() {
        queue = new LinkedList<>();
    }


    /*private Map<Integer, List<Integer>> adjList;

    public BreadthFirstSearch() {
        adjList = new HashMap<>();
    }

    public void addEdge(int u, int v) {
        adjList.putIfAbsent(u, new ArrayList<>());
        adjList.putIfAbsent(v, new ArrayList<>());
        adjList.get(u).add(v);
        adjList.get(v).add(u); // כי החיבור הוא דו-כיווני
    }

    // חיפוש BFS
    public void breadthFirstSearch(int start) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        visited.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            int node = queue.poll();
            System.out.print(node + " ");

            for (int neighbor : adjList.get(node)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
    }*/
}



