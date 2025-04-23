package algorithms.search;

import java.util.*;

public class BestFirstSearch extends BreadthFirstSearch {

    public BestFirstSearch(){
        super();
        name = "Best First Search";
    }

    @Override
    protected void createQueue() {
        queue = new PriorityQueue<>(Comparator.comparingInt(AState::getCost));
    }

}


