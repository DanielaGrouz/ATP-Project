package algorithms.search;

public interface ISearchingAlgorithm {
    /**
     * Solves the given searchable problem and returns the solution.
     *
     * @param problem the searchable domain to solve
     * @return a Solution object representing the path from the start state to the goal state
     */
    Solution solve(ISearchable problem);

    /**
     * Returns the name of the search algorithm.
     *
     * @return the name of the algorithm
     */
    String getName();

    /**
     * Returns the number of nodes that were evaluated during the search process.
     *
     * @return the number of nodes evaluated
     */
    int getNumberOfNodesEvaluated();

}
