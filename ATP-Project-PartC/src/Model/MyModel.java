package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.util.Observable;
import java.util.Observer;
import algorithms.search.Solution;


public class MyModel extends Observable implements IModel{
    private Maze maze;
    private int playerRow;
    private int playerCol;
    private Solution solution;
    private MyMazeGenerator generator;
    private int goalRow;
    private int goalCol;
    private boolean reachedGoal;

    public MyModel() {
        generator = new MyMazeGenerator();
    }

    @Override
    public void generateMaze(int rows, int cols) {
        maze = generator.generate(rows, cols);
        setChanged();
        notifyObservers("maze generated");
        goalRow=maze.getGoalPosition().getRowIndex();
        goalCol=maze.getGoalPosition().getColumnIndex();
        reachedGoal = false;
        // start position:
        movePlayer(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());
    }

    @Override
    public Maze getMaze() {
        return maze;
    }

    @Override
    public void updatePlayerLocation(MovementDirection direction) {
        if (!reachedGoal){
            switch (direction) {
                case UP -> {
                    if (playerRow > 0 && maze.getMazeMatrix()[playerRow-1][playerCol] != 1) {
                        movePlayer(playerRow - 1, playerCol);
                        if(reachedGoal()){
                            reachedGoal = true;
                        }
                    }
                }
                case DOWN -> {
                    if (playerRow < maze.getRows() - 1  && maze.getMazeMatrix()[playerRow+1][playerCol] != 1){
                        movePlayer(playerRow + 1, playerCol);
                        if(reachedGoal()){
                            reachedGoal = true;
                        }
                    }
                }
                case LEFT -> {
                    if (playerCol > 0  && maze.getMazeMatrix()[playerRow][playerCol-1] != 1){
                        movePlayer(playerRow, playerCol - 1);
                        if(reachedGoal()){
                            reachedGoal = true;
                        }
                    }
                }
                case RIGHT -> {
                    if (playerCol < maze.getColumns() - 1 && maze.getMazeMatrix()[playerRow][playerCol+1] != 1){
                        movePlayer(playerRow, playerCol + 1);
                        if(reachedGoal()){
                            reachedGoal = true;
                        }
                    }
                }
            }
        }

    }

    private void movePlayer(int row, int col){
        this.playerRow = row;
        this.playerCol = col;
        setChanged();
        notifyObservers("player moved");
    }

    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    @Override
    public int getPlayerCol() {
        return playerCol;
    }

    @Override
    public int getGoalRow() {
        return goalRow;
    }

    @Override
    public int getGoalCol() {
        return goalCol;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void solveMaze() {
        //solve the maze
        solution = new Solution();
        setChanged();
        notifyObservers("maze solved");
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    @Override
    public boolean reachedGoal(){
        if(playerRow == goalRow && playerCol == goalCol){
            return true;
        }
        return false;
    }
}
