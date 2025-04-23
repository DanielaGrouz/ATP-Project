package algorithms.search;

import java.util.ArrayList;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm{

    protected String name;
    protected ArrayList<AState> visitedNodes; //?
    protected Solution solution;
    private int numVisitedNodes;
    protected AState start;
    protected AState goal;

    /**
     * Constructs a new ASearchingAlgorithm.
     * Initializes the visited nodes list, the solution object, and the visited nodes count.
     */
    public ASearchingAlgorithm(){
        visitedNodes = new ArrayList<>();
        solution = new Solution();
        numVisitedNodes = 0;
    }

    /**
     * Returns the number of nodes that were evaluated during the search process.
     *
     * @return the number of nodes evaluated
     */
    @Override
    public int getNumberOfNodesEvaluated() {
        return numVisitedNodes;
    }

    /**
     * Prepares the search problem by setting the start and goal states.
     *
     * @param problem the searchable problem
     * @return true if the problem was successfully prepared, false otherwise (if start or goal is null)
     * @throws IllegalArgumentException if the problem is null
     */
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

    /**
     * Returns the name of the search algorithm.
     *
     * @return the name of the algorithm
     */
    public String getName() {
        return name;
    }

    /**
     * Builds the solution path by traversing back from the goal state to the start state,
     */
    protected void buildSolution(){
        AState current = goal;
        while (current != null) {
            solution.addStateToSolution(current);
            current = current.getCameFrom();
        }
        numVisitedNodes = visitedNodes.size();
    }

}
