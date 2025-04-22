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
            return solution; //empty solution
        }
        start = problem.getStartState();
        goal = problem.getGoleState();

        stack.push(start);
        AState currState;
        while (!stack.isEmpty()) {
            currState = stack.pop();
            if (visitedNodes.contains(currState)){ continue; }
            visitedNodes.add(currState);

            if (currState.equals(goal)) {
                goal.setCameFrom(currState.getCameFrom()); //or goal= currState
                buildSolution();
                break;
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


