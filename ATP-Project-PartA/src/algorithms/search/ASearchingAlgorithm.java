package algorithms.search;

import java.util.ArrayList;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm{

    protected String name;
    protected ArrayList<AState> visitedNodes; //?
    protected Solution solution;
    private int numVisitedNodes;
    protected AState start;
    protected AState goal;

    public ASearchingAlgorithm(){
        visitedNodes = new ArrayList<>();
        solution = new Solution();
        numVisitedNodes = 0;
    }

    @Override
    public int getNumberOfNodesEvaluated() {
        return numVisitedNodes;
    }


    protected boolean prepareProblem(ISearchable problem) throws IllegalArgumentException{
        if (problem==null){
            throw new IllegalArgumentException("Problem cannot be null");
        }
        start = problem.getStartState();
        goal = problem.getGoalState();
        if (start==null || goal==null) {
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    protected void buildSolution(){
        AState current = goal;
        while (current != null) {
            solution.addStateToSolution(current);
            current = current.getCameFrom();
        }
        numVisitedNodes = visitedNodes.size();
    }

}
