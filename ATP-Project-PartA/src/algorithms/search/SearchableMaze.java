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
        Position start = maze.getStartPosition();
        MazeState mazeState = new MazeState(start.getRowIndex(), start.getColumnIndex());
        return mazeState;
    }

    @Override
    public AState getGoalState() {
        Position goal = maze.getGoalPosition();
        MazeState mazeState = new MazeState(goal.getRowIndex(), goal.getColumnIndex());
        return mazeState;
    }

    @Override
    public ArrayList<AState> getAllPossibleStates(AState aState) throws IllegalArgumentException{
        if (!(aState instanceof MazeState)) {
            throw new IllegalArgumentException("Expected MazeState");
        }
        MazeState mState = (MazeState) aState;

        ArrayList<AState> possibleStates = new ArrayList<AState>();
        int[][] direction = {{-1,0},{-1,1},{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1}};

        for (int i=0 ; i<8 ; i++){
            int newRow = mState.getRow() + direction[i][0];
            int newCol = mState.getCol() + direction[i][1];

            if (0<=newRow && newRow<maze.getRows() && 0<=newCol && newCol<maze.getColumns()
                    && maze.canPass(newRow,newCol)){
                MazeState newState = new MazeState(newRow,newCol);
                if (direction[i][0]==0 || direction[i][1]==0){
                    newState.setCost(mState.getCost() + 10);
                }
                else {
                    newState.setCost(mState.getCost() + 15);
                }
                newState.setCameFrom(aState);
                possibleStates.add(newState);
            }

        }

        return possibleStates;
    }
}
