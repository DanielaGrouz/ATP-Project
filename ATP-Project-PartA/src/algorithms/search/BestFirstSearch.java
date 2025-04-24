package algorithms.search;

import java.util.*;

public class BestFirstSearch extends BreadthFirstSearch {

    /**
     * Constructs a new BestFirstSearch object.
     * Initializes the algorithm name and the queue used for search.
     */
    public BestFirstSearch(){
        super();
        name = "Best First Search";
    }

    /**
     * Creates the queue used for the Best First Search algorithm.
     * This implementation uses a PriorityQueue to prioritize states based on their cost.
     */
    @Override
    protected void createQueue() {
        queue = new PriorityQueue<>(Comparator.comparingInt(AState::getCost));
    }

}


