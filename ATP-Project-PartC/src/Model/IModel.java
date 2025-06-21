package Model;

import java.io.File;
import java.util.Observer;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;


public interface IModel {
    void generateMaze(int rows, int cols);
    Maze getMaze();

    int getPlayerRow();
    int getPlayerCol();
    int getGoalRow();
    int getGoalCol();

    boolean updatePlayerLocation(MovementDirection direction);
    void assignObserver(Observer o);
    void solveMaze();
    Solution getSolution();
    boolean reachedGoal();
    void movePlayerTo(int row, int col);
//    public void setPlayerRow(int row);
//    public void setPlayerCol(int col);
    void restartMaze();
    void exit();
    void saveMaze(File file);
    void openMaze(File file);
}
