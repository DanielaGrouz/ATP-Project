package Model;

import java.io.File;
import java.util.Observer;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

//Interface that defining the functionality of the Model in the Maze application
public interface IModel {

    //generates a new maze with the received measurements
    void generateMaze(int rows, int cols);
    //resets the player position in the maze
    void restartMaze();

    //returns the current maze
    Maze getMaze();

    //getters for the player's position and the target position in the maze
    int getPlayerRow();
    int getPlayerCol();
    int getGoalRow();
    int getGoalCol();

    //updates player position according to the given direction
    boolean updatePlayerLocation(MovementDirection direction);

    //solves the maze using a search algorithm
    void solveMaze();
    //returns the solution of the maze
    Solution getSolution();
    //checks if the player has reached the goal in the maze
    boolean reachedGoal();

    //saves the current maze to a file
    void saveMaze(File file);
    //loads a maze from a file
    void openMaze(File file);

    //assigns an observer to observe model updates
    void assignObserver(Observer o);

    //exits the game and releases resources
    void exit();

}
