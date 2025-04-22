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
