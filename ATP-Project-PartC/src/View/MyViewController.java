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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

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
    public Button solveButton;
    public Button restartButton;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();
    StringProperty updateGoalRow = new SimpleStringProperty();
    StringProperty updateGoalCol = new SimpleStringProperty();

    private MediaPlayer backgroundMusic = null;
    private MediaPlayer winMusic = null;

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
        solveButton.setDisable(true);
        restartButton.setDisable(true);

        mazeDisplayer.setOnMousePressed(event -> {
            dragging = true;

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


    }

    public void solveMaze(ActionEvent actionEvent) {
        viewModel.solveMaze();
    }

    public void restartMaze(ActionEvent actionEvent) {
        viewModel.restartMaze();
        mazeDisplayer.setSolution(null);
        setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
        UIUtils.showInfo("Maze restarted! Good luck 😊");
        playBackgroundMusic();
    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.txt)", "*.txt"));
        fc.setInitialDirectory(new File("./ATP-Project-PartC/resources"));

        File file = fc.showOpenDialog(null);
        if (file != null) {
            viewModel.openMaze(file);
        }
    }

    public void saveFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Save  maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.txt)", "*.txt"));
        fc.setInitialDirectory(new File("./ATP-Project-PartC/resources"));

        //set a default file name
        String defaultFileName = "myMaze.txt"; //file name
        fc.setInitialFileName(defaultFileName);

        File file = fc.showSaveDialog(null);
        if (file != null) {
            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }
            viewModel.saveMaze(file);
        }
    }

    public void newFile(ActionEvent actionEvent) {
        //
    }

    public void properties(ActionEvent actionEvent) {
        Properties props = loadProperties();
        if (props.size()!=0){
            StringBuilder sb = new StringBuilder();
            for (String key : props.stringPropertyNames()) {
                sb.append(key).append(" = ").append(props.getProperty(key)).append("\n");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Application Properties");
            alert.setHeaderText("Configuration");
            alert.setContentText(sb.toString());
            alert.showAndWait();
        }
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
            viewModel.exit();
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
            case "maze loaded" -> mazeLoaded();
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
        playBackgroundMusic();
        solveButton.setDisable(false);
        restartButton.setDisable(false);
    }

    private void goalReached() {
        System.out.println(">> ViewController: Reached goal!");
        playerMoved();

        try {
            if (backgroundMusic != null && backgroundMusic.getStatus() != MediaPlayer.Status.DISPOSED) {
                backgroundMusic.stop();
                backgroundMusic.dispose();
                backgroundMusic=null;
            }

            URL url = getClass().getResource("/sounds/MarioWin.mp3");
            if (url == null) {
                UIUtils.showError("Win music not found!");
                return;
            }

            String path = url.toExternalForm();
            Media media = new Media(path);
            winMusic = new MediaPlayer(media);
            winMusic.setOnReady(() -> {
                winMusic.play();
            });

        } catch (Exception e) {
            UIUtils.showError("Error playing win music: " + e.getMessage());
        }

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

    private void mazeLoaded() {
        mazeDisplayer.loadImagesOnce();
        mazeGenerated();
        UIUtils.showInfo("Maze file loaded successfully. Have fun \uD83D\uDE0A");
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
        try {
            if (backgroundMusic != null && backgroundMusic.getStatus() != MediaPlayer.Status.DISPOSED) {
                backgroundMusic.stop();
                backgroundMusic.dispose();
                backgroundMusic=null;
            }

            if (winMusic != null && winMusic.getStatus() != MediaPlayer.Status.DISPOSED) {
                winMusic.stop();
                winMusic.dispose();
                winMusic=null;
            }

            URL url = getClass().getResource("/sounds/MarioBackground.mp3");
            if (url == null) {
                UIUtils.showError("Background music not found!");
                return;
            }

            String path = url.toExternalForm();
            Media media = new Media(path);
            backgroundMusic = new MediaPlayer(media);
            backgroundMusic.setOnReady(() -> {
                backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
                backgroundMusic.play();
            });
        } catch (Exception e) {
            UIUtils.showError("Error playing background music: " + e.getMessage());
        }

    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getResourceAsStream("/config.properties");) {
            if (input == null) {
                UIUtils.showError("Could not find config.properties file");
                return props;
            }
            props.load(input);
        } catch (IOException ex) {
            UIUtils.showError("Error: " + ex.getMessage());
        }
        return props;
    }

    public void help(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("How to Play the Maze Game?");

        String helpText = """
        ▶ Goal:
        Reach the goal position on the maze board (the stat ⭐).

        ▶ Controls:
        - Use arrow keys, number keys (Numpad), or drag the player with the mouse.
        - Click cells around the player to move.

        ▶ Maze Symbols:
        - 🟦 Wall: Cannot pass.
        - ⬜ Path: Can move.
        - 🚶 Player: Your current location (Mario).
        - 🎯 Goal: The target to reach.

        ▶ Additional:
        - Press 'Solve Maze' to display the shortest path.
        - The solution path will be shown in green.
        - You can continue playing even after the solution is shown!

        Good luck! 🍄
        """;

        alert.setContentText(helpText);
        alert.showAndWait();
    }

    public void about(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("About the Project");

        String content = """
            👩‍💻 Developers:
            - Rinat Hadad
            - Daniela Grouz
            
            🧩 Maze Generation Algorithm:
            - Prim's Algorithm

            🧠 Maze Solving Algorithm:
            - Best First Search

            🧪 Technologies:
            - JavaFX
            - Multithreading for performance
            - CSS Styling and FXML for UI

            🎮 Features:
            - Keyboard and mouse control
            - Sound effects and background music
            - Dynamic resizing

            💡 Motivation:
            This game was created as part of a university course on 
            Advanced Programming, aiming to combine algorithms, UI 
            and software design into one interactive experience.
            
            Thank you for playing! 👏
            """;

        alert.setContentText(content);
        alert.showAndWait();
    }


}