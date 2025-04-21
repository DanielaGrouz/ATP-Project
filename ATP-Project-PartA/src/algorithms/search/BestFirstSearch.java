package algorithms.search;

import java.util.*;

public class BestFirstSearch extends ASearchingAlgorithm {

    private PriorityQueue<AState> openList;


    @Override
    public Solution solve(ISearchable problem) {
        return null;
    }

    @Override
    public String getName() {
        return "Best First Search";
    }




    /*private Map<Integer, List<Integer>> adjList;

    public BestFirstSearch() {
        adjList = new HashMap<>();
    }

    public void addEdge(int u, int v) {
        adjList.putIfAbsent(u, new ArrayList<>());
        adjList.putIfAbsent(v, new ArrayList<>());
        adjList.get(u).add(v);
        adjList.get(v).add(u);
    }

    // חיפוש Best-First Search
    public void bestFirstSearch(int start, Map<Integer, Integer> heuristic) {
        Set<Integer> visited = new HashSet<>();
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(heuristic::get));

        pq.add(start);

        while (!pq.isEmpty()) {
            int node = pq.poll();
            if (!visited.contains(node)) {
                visited.add(node);
                System.out.print(node + " ");

                for (int neighbor : adjList.get(node)) {
                    if (!visited.contains(neighbor)) {
                        pq.add(neighbor);
                    }
                }
            }
        }
    }*/
}


