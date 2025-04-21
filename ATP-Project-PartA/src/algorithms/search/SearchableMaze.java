package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.util.ArrayList;

public class SearchableMaze implements ISearchable  {

    private Maze maze;

    public SearchableMaze(Maze maze){
        this.maze = maze;
    }

    @Override
    public AState getStartState() {
        Position Start = maze.getStartPosition();
        MazeState mazeState = new MazeState(Start.getRowIndex(), Start.getColumnIndex());
        return mazeState;
    }

    @Override
    public AState getGoleState() {
        Position gole = maze.getGoalPosition();
        MazeState mazeState = new MazeState(gole.getRowIndex(), gole.getColumnIndex());
        return mazeState;
    }

    @Override
    public ArrayList<AState> getAllPossibleStates(AState aState) {

        MazeState mState = (MazeState) aState;
        ArrayList<AState> possibleStates = new ArrayList<AState>();
        //int[] direction : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}})
        int[][] directions = {
                {-1, 0}, // up
                {1, 0},  // down
                {0, -1}, // left
                {0, 1},  // right
        };




        return possibleStates;
    }
}
