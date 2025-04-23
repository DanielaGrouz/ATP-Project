package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.util.ArrayList;

public class SearchableMaze implements ISearchable  {

    private Maze maze;

    /**
     * Constructs a SearchableMaze based on a given Maze object.
     *
     * @param maze the Maze object to adapt for searching
     */
    public SearchableMaze(Maze maze){
        this.maze = maze;
    }

    /**
     * Returns the start state of the maze.
     *
     * @return the start state as a AState
     */
    @Override
    public AState getStartState() {
        Position start = maze.getStartPosition();
        AState mazeState = new MazeState(start.getRowIndex(), start.getColumnIndex());
        return mazeState;
    }

    /**
     * Returns the goal state of the maze.
     *
     * @return the goal state as a AState
     */
    @Override
    public AState getGoalState() {
        Position goal = maze.getGoalPosition();
        AState mazeState = new MazeState(goal.getRowIndex(), goal.getColumnIndex());
        return mazeState;
    }

    /**
     * Returns all possible states that can be reached from the given state.
     *
     * @param aState the current state (must be a MazeState)
     * @return a list of all possible successor states
     * @throws IllegalArgumentException if the given state is not a MazeState
     */
    @Override
    public ArrayList<AState> getAllPossibleStates(AState aState) throws IllegalArgumentException{
        //check if the given state is a MazeState
        if (!(aState instanceof MazeState)) {
            throw new IllegalArgumentException("Expected MazeState");
        }
        //cast the state to MazeState
        MazeState mState = (MazeState) aState;

        //define the possible directions to move in the maze
        int[][] direction = {{-1,0},{-1,1},{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1}};
        ArrayList<AState> possibleStates = new ArrayList<AState>();

        //iterate over the possible directions
        for (int i=0 ; i<8 ; i++){
            int newRow = mState.getRow() + direction[i][0];
            int newCol = mState.getCol() + direction[i][1];
            //check if the new position is valid (within the maze and not a wall)
            if (0<=newRow && newRow<maze.getRows() && 0<=newCol && newCol<maze.getColumns()
                    && maze.canPass(newRow,newCol)){
                MazeState newState = new MazeState(newRow,newCol);
                //set the cost of the new state
                if (direction[i][0]==0 || direction[i][1]==0){ //moving horizontally or vertically
                    newState.setCost(mState.getCost() + 10);
                }
                else { //moving diagonally
                    newState.setCost(mState.getCost() + 15);
                }
                //add the new state to the list, linking it back to its predecessor
                newState.setCameFrom(aState);
                possibleStates.add(newState);
            }
        }

        return possibleStates;
    }
}
