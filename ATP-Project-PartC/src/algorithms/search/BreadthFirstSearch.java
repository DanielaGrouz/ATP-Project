package algorithms.search;

import java.util.*;

public class BreadthFirstSearch extends ASearchingAlgorithm {

    protected Queue<AState> queue;

    /**
     * Constructs a new BreadthFirstSearch object.
     * Initializes the algorithm name and the queue used for search.
     */
    public BreadthFirstSearch(){
        createQueue();
        name = "Breadth First Search";
    }

    /**
     * Solves the given searchable problem using the Breadth First Search algorithm.
     *
     * @param problem the searchable domain to solve
     * @return a Solution object representing the path from the start state to the goal state
     */
    @Override
    public Solution solve(ISearchable problem) {
        if (!prepareProblem(problem)){
            return solution; //empty solution
        }
        //add the start state to the queue
        queue.add(start);
        AState currState;
        //while the queue is not empty, continue searching
        while (!queue.isEmpty()) {
            //poll the next state from the queue
            currState = queue.poll();
            //skip visited nodes; otherwise, mark as visited.
            if (visitedNodes.contains(currState)){ continue; }
            visitedNodes.add(currState);
            //check if we reached the goal state.
            if (currState.equals(goal)) {
                goal = currState; //save the goal state to restore the solution later
                buildSolution();
                return solution;
            }
            //iterate over all possible next states
            for (AState neighbor : problem.getAllPossibleStates(currState)) {
                //skip if already visited
                if (!visitedNodes.contains(neighbor)) {
                    queue.add(neighbor);
                }
            }
        }
        //if no solution is found, return empty solution
        return solution;
    }

    /**
     * Creates the queue used for the Breadth First Search algorithm.
     * This implementation uses a LinkedList as the queue.
     */
    protected void createQueue() {
        queue = new LinkedList<>();
    }

}



