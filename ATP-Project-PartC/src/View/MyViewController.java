package View;

import Model.MovementDirection;
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

    // דגל שמורה אם הגרירה פעילה
    private boolean dragging = false;
    private int dragPrevRow = -1;
    private int dragPrevCol = -1;

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

        mazeDisplayer.setOnMousePressed(event -> {
            dragging = true;

            // שמור את המיקום ההתחלתי בעת לחיצה
            dragPrevRow = viewModel.getPlayerRow();
            dragPrevCol = viewModel.getPlayerCol();

            event.consume();
        });

        mazeDisplayer.setOnMouseDragged(event -> {
            if (!dragging) return;

            int currentRow = dragPrevRow;
            int currentCol = dragPrevCol;

            int mouseRow = mazeDisplayer.getRowFromY(event.getY());
            int mouseCol = mazeDisplayer.getColFromX(event.getX());

            int dRow = mouseRow - currentRow;
            int dCol = mouseCol - currentCol;

            MovementDirection dir = null;

            if (dRow == -1 && dCol == 0) dir = MovementDirection.UP;
            else if (dRow == 1 && dCol == 0) dir = MovementDirection.DOWN;
            else if (dRow == 0 && dCol == -1) dir = MovementDirection.LEFT;
            else if (dRow == 0 && dCol == 1) dir = MovementDirection.RIGHT;
            else if (dRow == -1 && dCol == -1) dir = MovementDirection.UP_LEFT;
            else if (dRow == -1 && dCol == 1) dir = MovementDirection.UP_RIGHT;
            else if (dRow == 1 && dCol == -1) dir = MovementDirection.DOWN_LEFT;
            else if (dRow == 1 && dCol == 1) dir = MovementDirection.DOWN_RIGHT;

            if (dir != null) {
                boolean moved = viewModel.movePlayer(dir); // שים לב: צריך ש-movePlayer תחזיר boolean להצלחת המהלך
                if (moved) {
                    // עדכן את המיקום הקודם כדי לא לזוז יותר מפעם אחת לתא מסוים
                    dragPrevRow = viewModel.getPlayerRow();
                    dragPrevCol = viewModel.getPlayerCol();
                }
            }

            event.consume();
        });

        mazeDisplayer.setOnMouseReleased(event -> {
            dragging = false;
            mazeDisplayer.requestFocus();
            event.consume();
        });
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
        System.out.println("Player moved to: " + viewModel.getPlayerRow() + "," + viewModel.getPlayerCol());
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

    public void mouseClicked(MouseEvent event) {
        // 1. קבל את תא השורה והעמודה שנלחצו בעכבר
        int clickedRow = mazeDisplayer.getRowFromY(event.getY());
        int clickedCol = mazeDisplayer.getColFromX(event.getX());

        // 2. קבל את מיקום השחקן הנוכחי
        int currentRow = viewModel.getPlayerRow();
        int currentCol = viewModel.getPlayerCol();

        // 3. חשב הפרש בין הלחיצה למיקום הנוכחי
        int dRow = clickedRow - currentRow;
        int dCol = clickedCol - currentCol;

        // 4. תרגם את ההפרש ל-MovementDirection
        MovementDirection dir = null;

        if (dRow == -1 && dCol == 0) dir = MovementDirection.UP;
        else if (dRow == 1 && dCol == 0) dir = MovementDirection.DOWN;
        else if (dRow == 0 && dCol == -1) dir = MovementDirection.LEFT;
        else if (dRow == 0 && dCol == 1) dir = MovementDirection.RIGHT;
        else if (dRow == -1 && dCol == -1) dir = MovementDirection.UP_LEFT;
        else if (dRow == -1 && dCol == 1) dir = MovementDirection.UP_RIGHT;
        else if (dRow == 1 && dCol == -1) dir = MovementDirection.DOWN_LEFT;
        else if (dRow == 1 && dCol == 1) dir = MovementDirection.DOWN_RIGHT;

        // 5. אם יש כיוון חוקי – תעדכן את המיקום
        if (dir != null) {
            viewModel.movePlayer(dir);
        }

        // 6. בקש פוקוס לעכבר על המאז
        mazeDisplayer.requestFocus();

        event.consume(); // עצור התפשטות האירוע אם צריך
    }





}