package algorithms.search;

import java.util.ArrayList;

public class Solution {

    private ArrayList<AState> solutionPath;

    public Solution(){
        solutionPath = new ArrayList<AState>();
    }

    public void addStateToSolution(AState state){
        if (state != null){
            solutionPath.addFirst(state);
        }
        //שגיאה
    }

    public ArrayList<AState> getSolutionPath(){
        return solutionPath;
    }
}
