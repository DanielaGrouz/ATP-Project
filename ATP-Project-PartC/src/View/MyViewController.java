package View;

import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController implements IView ,Observer, Initializable{
    public MyViewModel viewModel;

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addObserver(this);
    }

    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    public Label goalRow;
    public Label goalCol;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();
    StringProperty updateGoalRow = new SimpleStringProperty();
    StringProperty updateGoalCol = new SimpleStringProperty();

    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }

    public String getUpdateGoalRow() {
        return updateGoalRow.get();
    }

    public void setUpdateGoalRow(int updateGoalRow) {
        this.updateGoalRow.set(updateGoalRow + "");
    }

    public String getUpdateGoalCol() {
        return updateGoalCol.get();
    }

    public void setUpdateGoalCol(int updateGoalCol) {
        this.updateGoalCol.set(updateGoalCol + "");
    }
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
        goalRow.textProperty().bind(updateGoalRow);
        goalCol.textProperty().bind(updateGoalCol);
    }

    public void generateMaze(ActionEvent actionEvent) {
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());

        viewModel.generateMaze(rows, cols);
    }

    public void solveMaze(ActionEvent actionEvent) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setContentText("Solving maze...");
//        alert.show();
        viewModel.solveMaze();
    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog(null);
        //...
    }

    public void keyPressed(KeyEvent keyEvent) {
        viewModel.movePlayer(keyEvent);
        keyEvent.consume();
    }

    public void setPlayerPosition(int row, int col){
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }

    public void setGoalPosition(int row, int col){
        mazeDisplayer.setGoalPosition(row, col);
        setUpdateGoalRow(row);
        setUpdateGoalCol(col);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    @Override
    public void update(Observable o, Object arg) {
        String change = (String) arg;
        switch (change){
            case "maze generated" -> mazeGenerated();
            case "player moved" -> playerMoved();
            case "maze solved" -> mazeSolved();
            case "goal reached" -> goalReached();
            default -> System.out.println("Not implemented change: " + change);
        }
    }

    private void mazeSolved() {
        mazeDisplayer.setSolution(viewModel.getSolution());
    }

    private void playerMoved() {
        setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
    }

    private void mazeGenerated() {
        mazeDisplayer.drawMaze(viewModel.getMaze());
        setGoalPosition(viewModel.getGoalRow(), viewModel.getGoalCol());
        mazeDisplayer.setGoalPosition(viewModel.getGoalRow(), viewModel.getGoalCol());
    }

    private void goalReached() {
        System.out.println(">> ViewController: Reached goal!");
        playerMoved();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Congratulations!");
        alert.setHeaderText(null);
        alert.setContentText("You've reached the goal!");
        alert.showAndWait();
    }






}