package algorithms.search;

import java.util.HashSet;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm{

    protected HashSet<AState> visitedNodes;
    protected Solution solution;
    private int countVisitedNodes;
    protected AState start;
    protected AState goal;

    public ASearchingAlgorithm(){
        visitedNodes = new HashSet<>();
        solution = new Solution();
        countVisitedNodes = 0;
    }

    @Override
    public int getNumberOfNodesEvaluated() {
        return countVisitedNodes;
    }


    protected void bildSolution(){
        AState current = goal;
        while (current != null) {
            solution.addStateToSolution(current);
            current = current.cameFrom;
        }
    }


}
