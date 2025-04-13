package algorithms.search;
import java.util.*;
public class DepthFirstSearch {

        private Map<Integer, List<Integer>> adjList;

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
        }
    }


