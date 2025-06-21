package View;

import Model.MovementDirection;
import ViewModel.MyViewModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
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

    private MediaPlayer backgroundMusic;
    private MediaPlayer winMusic;

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
                boolean moved = viewModel.movePlayer(dir);
                if (moved) {
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
        mazeDisplayer.loadImagesOnce();
        if (rows<2 || cols<2)
            UIUtils.showInfo("The number of rows and columns must be at least 2. A default 10x10 maze is created.");
        viewModel.generateMaze(rows, cols);
        playBackgroundMusic();
    }

    public void solveMaze(ActionEvent actionEvent) {
        viewModel.solveMaze();
    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog(null);
    }

    public void saveFile(ActionEvent actionEvent) {
        //
    }

    public void newFile(ActionEvent actionEvent) {
        //
    }

    public void properties(ActionEvent actionEvent) {
        //
    }

    public void exit(Event event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit?");

        ButtonType yes = new ButtonType("Yes");
        ButtonType no  = new ButtonType("No");
        alert.getButtonTypes().setAll(yes, no);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yes) {
            Platform.exit(); //close JavaFX Application Thread
            System.exit(0);
        }
        else {
            if (event != null) {
                event.consume();
            }
        }

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

        if (change.startsWith("Error:")) {
            UIUtils.showError(change.substring(6).trim());
            return;
        }

        switch (change){
            case "maze generated" -> mazeGenerated();
            case "player moved" -> playerMoved();
            case "maze solved" -> mazeSolved();
            case "goal reached" -> goalReached();
            default -> System.out.println("Not implemented change: " + change);
        }
    }

    private void mazeSolved() {
        //UIUtils.showInfo("Solving maze...");
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

        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
        URL url = getClass().getResource("/sounds/MarioWin.mp3");
        if (url == null) {
            UIUtils.showError("Win music not found!");
            return;
        }
        String path = url.toExternalForm();
        Media media = new Media(path);
        winMusic = new MediaPlayer(media);
        winMusic.play();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Congratulations!");
        alert.setHeaderText(null);
        alert.setContentText("You've reached the goal!");

        ImageView star = new ImageView(new Image(getClass().getResource("/images/trophy.png").toExternalForm()));
        star.setFitWidth(60);
        star.setFitHeight(60);

        alert.setGraphic(star);
        alert.showAndWait();
    }

    public void mouseClicked(MouseEvent event) {
        int clickedRow = mazeDisplayer.getRowFromY(event.getY());
        int clickedCol = mazeDisplayer.getColFromX(event.getX());
        if (clickedRow == -1 || clickedCol == -1) return;

        int currentRow = viewModel.getPlayerRow();
        int currentCol = viewModel.getPlayerCol();

        int dRow = clickedRow - currentRow;
        int dCol = clickedCol - currentCol;

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
            viewModel.movePlayer(dir);
        }

        mazeDisplayer.requestFocus();

        event.consume();
    }


    private void playBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
        }

        if (winMusic != null) {
            winMusic.stop();
            winMusic.dispose();
        }

        URL url = getClass().getResource("/sounds/MarioBackground.mp3");
        if (url == null) {
            UIUtils.showError("Background music not found!");
            return;
        }

        String path = url.toExternalForm();
        Media media = new Media(path);
        backgroundMusic = new MediaPlayer(media);
        backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundMusic.play();
    }


}