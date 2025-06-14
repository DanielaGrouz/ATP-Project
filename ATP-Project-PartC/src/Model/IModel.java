package Model;

import java.util.Observer;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;


public interface IModel {
    public void generateMaze(int rows, int cols);
    public Maze getMaze();

    public int getPlayerRow();
    public int getPlayerCol();
    public int getGoalRow();
    public int getGoalCol();

    public void updatePlayerLocation(MovementDirection direction);
    public void assignObserver(Observer o);
    public void solveMaze();
    public Solution getSolution();
    public boolean reachedGoal();
//    public void setPlayerRow(int row);
//    public void setPlayerCol(int col);
}
