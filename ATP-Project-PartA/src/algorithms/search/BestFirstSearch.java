package algorithms.search;

import java.util.*;

public class BestFirstSearch extends BreadthFirstSearch {

    private PriorityQueue<AState> openList;

    public BestFirstSearch(){
        super();
        name = "Best First Search";
    }


    @Override
    protected void createQueue() {
        queue = new PriorityQueue<>(Comparator.comparingInt(AState::getCost));
    }

}


