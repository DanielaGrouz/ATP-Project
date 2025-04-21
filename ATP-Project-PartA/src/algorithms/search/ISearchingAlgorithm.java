package algorithms.search;

public interface ISearchingAlgorithm {

    Solution solve(ISearchable problem);

    String getName();

    int getNumberOfNodesEvaluated();

}
