package algorithms.search;

import algorithms.mazeGenerators.Maze;

import java.util.ArrayList;

public class SearchableMaze implements ISearchable {

    private Maze maze;

    public SearchableMaze(Maze maze){
        this.maze = maze;
    }

    public ArrayList<AState> getAllPossibleStates(AState state){
        //?????????
        return null;
    }

    @Override
    public AState getStartState() {

        //maze.getStartPosition();??
        return null;
    }

    @Override
    public AState getGoleState() {

        //maze.getGoalPosition();??
        return null;
    }

    @Override
    public ArrayList<AState> getSuccessors(AState s) {
        return null;
    }
}
