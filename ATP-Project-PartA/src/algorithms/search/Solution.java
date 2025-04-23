package algorithms.search;

import java.util.ArrayList;

public class Solution {

    private ArrayList<AState> solutionPath;

    public Solution(){
        solutionPath = new ArrayList<>();
    }

    public void addStateToSolution(AState state){
        if (state == null){
            throw new IllegalArgumentException("Cannot add null state to the solution");
        }
        solutionPath.add(0,state);
    }

    public ArrayList<AState> getSolutionPath(){
        return solutionPath;
    }


}
