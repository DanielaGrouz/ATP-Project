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

        if (!prepareProblem(problem)){
            return solution; //empty solution
        }

        queue.add(start);
        AState currState;
        while (!queue.isEmpty()) {
            currState = queue.poll();
            if (visitedNodes.contains(currState)){ continue; }
            visitedNodes.add(currState);

            if (currState.equals(goal)) {
                goal = currState; //
                buildSolution();
                return solution;
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

}



