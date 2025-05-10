package algorithms.search;

import java.util.ArrayList;

public class Solution {

    private ArrayList<AState> solutionPath;

    /**
     * Constructs a new empty Solution.
     */
    public Solution(){
        solutionPath = new ArrayList<>();
    }

    /**
     * Adds a state to the beginning of the solution path.
     *
     * @param state the state to add to the solution, must not be null
     * @throws IllegalArgumentException if the given state is null
     */
    public void addStateToSolution(AState state){
        if (state == null){
            throw new IllegalArgumentException("Cannot add null state to the solution");
        }
        solutionPath.add(0,state);
    }

    /**
     * Returns the solution path as an ArrayList of AState objects.
     *
     * @return the solution path
     */
    public ArrayList<AState> getSolutionPath(){
        return solutionPath;
    }

}
