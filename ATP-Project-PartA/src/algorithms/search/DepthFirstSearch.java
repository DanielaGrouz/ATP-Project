package algorithms.search;

import java.util.*;

public class DepthFirstSearch extends ASearchingAlgorithm {

    private Stack<AState> stack;

    /**
     * Constructs a new DepthFirstSearch object.
     * Initializes the algorithm name and the stack used for search.
     */
    public DepthFirstSearch(){
        name = "Depth First Search";
        stack = new Stack<>();
    }

    /**
     * Solves the given searchable problem using the Depth First Search algorithm.
     *
     * @param problem the searchable domain to solve
     * @return a Solution object representing the path from the start state to the goal state
     */
    @Override
    public Solution solve(ISearchable problem) {
        if (!prepareProblem(problem)){
            return solution; //empty solution
        }
        //push the start state to the stack
        stack.push(start);
        AState currState;
        //while the stack is not empty, continue searching
        while (!stack.isEmpty()) {
            //pop the top state from the stack
            currState = stack.pop();
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
                if (!stack.contains(neighbor) && !visitedNodes.contains(neighbor)) {
                    stack.push(neighbor);
                }
            }
        }
        //if no solution is found, return empty solution
        return solution;
    }

}


