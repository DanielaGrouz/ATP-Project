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

        if (!prepareProblem(problem)){
            return solution; //empty solution
        }

        stack.push(start);
        AState currState;
        while (!stack.isEmpty()) {
            currState = stack.pop();
            if (visitedNodes.contains(currState)){ continue; }
            visitedNodes.add(currState);

            if (currState.equals(goal)) {
                goal = currState; //goal.setCameFrom(currState.getCameFrom());
                buildSolution();
                return solution;
            }
            for (AState neighbor : problem.getAllPossibleStates(currState)) {
                if (!stack.contains(neighbor) && !visitedNodes.contains(neighbor)) {
                    neighbor.setCameFrom(currState);
                    stack.push(neighbor);
                }
            }

        }

        return solution;
    }

}


