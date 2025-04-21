package algorithms.search;

import java.util.*;

public class BreadthFirstSearch extends ASearchingAlgorithm {

    private Queue<AState> openList;

    @Override
    public Solution solve(ISearchable problem) {
        return null;
    }

    @Override
    public String getName() {
        return "Breadth First Search";
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



