package ViewModel;
import Model.IModel;
import Model.MovementDirection;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer{
    private IModel model;

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this); //Observe the Model for it's changes
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    public Maze getMaze(){
        return model.getMaze();
    }

    public int getPlayerRow(){
        return model.getPlayerRow();
    }

    public int getPlayerCol(){
        return model.getPlayerCol();
    }

    public int getGoalRow(){
        return model.getGoalRow();
    }

    public int getGoalCol(){
        return model.getGoalCol();
    }

    public Solution getSolution(){
        return model.getSolution();
    }

    public void generateMaze(int rows, int cols){
        model.generateMaze(rows, cols);
    }

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
                // no need to move the player...
                return;
            }
        }
        if (direction != null) {
            model.updatePlayerLocation(direction);
        }
    }

    public void solveMaze(){
        model.solveMaze();
    }

    public void movePlayerTo(int row, int col) {
        model.movePlayerTo(row, col);
    }

    public boolean movePlayer(MovementDirection direction) {
        return model.updatePlayerLocation(direction);
    }

    public void exit(){
        model.exit();
    }

    public void saveMaze(File file) {
        model.saveMaze(file);
    }

    public void openMaze(File file) {
        model.openMaze(file);
    }

    public void restartMaze() {
        model.restartMaze();
    }

}
