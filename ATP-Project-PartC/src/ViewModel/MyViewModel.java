package ViewModel;

import Model.IModel;
import Model.MovementDirection;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 * The ViewModel class acts as an intermediary between the View and the Model.
 * It observes the Model for changes, exposes relevant data and actions to the View,
 * and translates user input into model operations.
 */
public class MyViewModel extends Observable implements Observer{
    private IModel model;

    /**
     * Constructs a new ViewModel and assigns itself as an observer of the Model.
     * @param model The model to observe and interact with.
     */
    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this); //observe the Model for it's changes
    }

    /**
     * Requests the model to generate a new maze with the received measurements.
     * @param rows Number of rows for the new maze.
     * @param cols Number of columns for the new maze.
     */
    public void generateMaze(int rows, int cols){
        model.generateMaze(rows, cols);
    }

    /**
     * Requests the model to restart the current maze.
     */
    public void restartMaze() {
        model.restartMaze();
    }

    /**
     * Returns the current maze.
     * @return The maze object from the model.
     */
    public Maze getMaze(){
        return model.getMaze();
    }

    /**
     * @return Current row of the player.
     */
    public int getPlayerRow(){
        return model.getPlayerRow();
    }

    /**
     * @return Current column of the player.
     */
    public int getPlayerCol(){
        return model.getPlayerCol();
    }

    /**
     * @return Row of the goal position.
     */
    public int getGoalRow(){
        return model.getGoalRow();
    }

    /**
     * @return Column of the goal position.
     */
    public int getGoalCol(){
        return model.getGoalCol();
    }

    /**
     * Returns the solution for the current maze.
     * @return The solution object from the model.
     */
    public Solution getSolution(){
        return model.getSolution();
    }

    /**
     * Requests the model to solve the current maze.
     */
    public void solveMaze(){
        model.solveMaze();
    }

    /**
     * Handles a key event and moves the player in the corresponding direction.
     * Supports both arrow keys and numpad/digit keys for diagonal movement.
     * @param keyEvent The key event representing the user's input.
     */
    public void movePlayer(KeyEvent keyEvent){
        MovementDirection direction = null;
        switch (keyEvent.getCode()){
            case UP, NUMPAD8, DIGIT8 -> direction = MovementDirection.UP;
            case DOWN, NUMPAD2, DIGIT2 -> direction = MovementDirection.DOWN;
            case LEFT, NUMPAD4, DIGIT4 -> direction = MovementDirection.LEFT;
            case RIGHT, NUMPAD6, DIGIT6 -> direction = MovementDirection.RIGHT;
            case NUMPAD7, DIGIT7 -> direction = MovementDirection.UP_LEFT;
            case NUMPAD9, DIGIT9 -> direction = MovementDirection.UP_RIGHT;
            case NUMPAD1, DIGIT1 -> direction = MovementDirection.DOWN_LEFT;
            case NUMPAD3, DIGIT3 -> direction = MovementDirection.DOWN_RIGHT;
            default -> {
                //no need to move the player
                return;
            }
        }
        if (direction != null) {
            model.updatePlayerLocation(direction);
        }
    }

    /**
     * Updates the player's position in the maze according to the given direction.
     * @param direction The direction in which to move the player.
     * @return true if the move was successful, false otherwise.
     */
    public boolean updatePlayerLocation(MovementDirection direction) {
        return model.updatePlayerLocation(direction);
    }

    /**
     * Requests the model to save the current maze and current player position to a file.
     * @param file The file to save to.
     */
    public void saveMaze(File file) {
        model.saveMaze(file);
    }

    /**
     * Requests the model to load a maze and player position from a file.
     * @param file The file to load from.
     */
    public void openMaze(File file) {
        model.openMaze(file);
    }

    /**
     * Called when the Model notifies its observers of a change.
     * Forwards the notification to this ViewModel's observers (the View).
     * @param o The observable object.
     * @param arg An argument passed by the notifyObservers method.
     */
    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    /**
     * Requests the model to exit and clean up resources.
     */
    public void exit(){
        model.exit();
    }
}
