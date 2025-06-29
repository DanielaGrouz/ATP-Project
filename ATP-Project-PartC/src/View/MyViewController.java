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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Controller for the Maze game UI.
 * Handles user actions, updates the view, and communicates with the ViewModel.
 */
public class MyViewController implements IView ,Observer, Initializable{
    private MyViewModel viewModel; //used for communication between View and Model
    //UI controls for maze size input and displaying the maze
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    //labels for showing current player and goal positions
    public Label playerRow;
    public Label playerCol;
    public Label goalRow;
    public Label goalCol;
    //buttons for solving and restarting the maze
    public Button solveButton;
    public Button restartButton;
    //the main container for the UI
    public BorderPane mainPane;
    //properties to bind player and goal position to UI labels
    private StringProperty updatePlayerRow = new SimpleStringProperty();
    private StringProperty updatePlayerCol = new SimpleStringProperty();
    private StringProperty updateGoalRow = new SimpleStringProperty();
    private StringProperty updateGoalCol = new SimpleStringProperty();
    //music for the game
    private MediaPlayer backgroundMusic = null;
    private MediaPlayer winMusic = null;
    //UI status
    private boolean isDarkMode = false;
    private boolean dragging = false;
    //previous position used when dragging the player
    private int dragPrevRow = -1;
    private int dragPrevCol = -1;

    /**
     * Sets the ViewModel and registers this controller as an observer.
     * @param viewModel the ViewModel to use
     */
    @Override
    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addObserver(this);
    }

    /**
     * Updates the player row label with the new value.
     * @param updatePlayerRow the new row index of the player
     */
    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }
    /**
     * Updates the player column label with the new value.
     * @param updatePlayerCol the new column index of the player
     */
    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }
    /**
     * Updates the goal row label with the new value.
     * @param updateGoalRow the new row index of the goal
     */
    public void setUpdateGoalRow(int updateGoalRow) {
        this.updateGoalRow.set(updateGoalRow + "");
    }
    /**
     * Updates the goal column label with the new value.
     * @param updateGoalCol the new column index of the goal
     */
    public void setUpdateGoalCol(int updateGoalCol) {
        this.updateGoalCol.set(updateGoalCol + "");
    }

    /**
     * Initializes the controller after FXML loading.
     * Binds labels, disables buttons, and sets up music and mouse handlers.
     */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //bind label properties to show player and goal positions
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
        goalRow.textProperty().bind(updateGoalRow);
        goalCol.textProperty().bind(updateGoalCol);

        //disable buttons until maze is generated
        solveButton.setDisable(true);
        restartButton.setDisable(true);

        //initialize background and win music
        initializeBackgroundMusic();
        initializeWinMusic();

        //set up mouse drag handlers for moving the player
        initializeMouseHandlers();
    }

    /**
     * Initializes the background music player.
     */
    private void initializeBackgroundMusic() {
        if (backgroundMusic != null) return;

        try {
            URL url = getClass().getResource("/sounds/MarioBackground.mp3");
            if (url == null) {
                UIUtils.showError("Background music not found!");
                return;
            }
            String path = url.toExternalForm();
            Media media = new Media(path);
            backgroundMusic = new MediaPlayer(media);
            backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);

        } catch (Exception e) {
            UIUtils.showError("Error initializing background music: " + e.getMessage());
        }
    }

    /**
     * Initializes the win music player.
     */
    private void initializeWinMusic() {
        if (winMusic != null) return;

        try {
            URL url = getClass().getResource("/sounds/MarioWin.mp3");
            if (url == null) {
                UIUtils.showError("Win music not found!");
                return;
            }
            String path = url.toExternalForm();
            Media media = new Media(path);
            winMusic = new MediaPlayer(media);
            winMusic.setOnEndOfMedia(() -> winMusic.stop());
        } catch (Exception e) {
            UIUtils.showError("Error initializing win music: " + e.getMessage());
        }
    }

    /**
     * Sets up mouse handlers for dragging the player in the maze.
     */
    private void initializeMouseHandlers(){
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
            //determine direction based on mouse movement
            if (dRow == -1 && dCol == 0) dir = MovementDirection.UP;
            else if (dRow == 1 && dCol == 0) dir = MovementDirection.DOWN;
            else if (dRow == 0 && dCol == -1) dir = MovementDirection.LEFT;
            else if (dRow == 0 && dCol == 1) dir = MovementDirection.RIGHT;
            else if (dRow == -1 && dCol == -1) dir = MovementDirection.UP_LEFT;
            else if (dRow == -1 && dCol == 1) dir = MovementDirection.UP_RIGHT;
            else if (dRow == 1 && dCol == -1) dir = MovementDirection.DOWN_LEFT;
            else if (dRow == 1 && dCol == 1) dir = MovementDirection.DOWN_RIGHT;

            //move player if direction is valid
            if (dir != null) {
                boolean moved = viewModel.updatePlayerLocation(dir);
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

    /**
     * Generates a new maze based on user input for rows and columns.
     * @param actionEvent the event from the generate button
     */
    public void generateMaze(ActionEvent actionEvent) {
        try {
            int rows = Integer.valueOf(textField_mazeRows.getText());
            int cols = Integer.valueOf(textField_mazeColumns.getText());
            mazeDisplayer.loadImagesOnce();
            if (rows<2 || cols<2){
                UIUtils.showInfo("The number of rows and columns must be at least 2. A default 10x10 maze is created.");
                viewModel.generateMaze(10, 10);
            }
            else {
                viewModel.generateMaze(rows, cols);
            }
        }
        catch (Exception e){
            UIUtils.showError("Please enter whole numbers only in the rows and columns fields.");
        }
    }

    /**
     * Requests the ViewModel to solve the current maze.
     * @param actionEvent the event from the solve button
     */
    public void solveMaze(ActionEvent actionEvent) {
        viewModel.solveMaze();
    }

    /**
     * Restarts the current maze, resets solution and player position, and plays music.
     * @param actionEvent the event from the restart button
     */
    public void restartMaze(ActionEvent actionEvent) {
        viewModel.restartMaze();
        mazeDisplayer.setSolution(null); //clear solution path
        setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
        UIUtils.showInfo("Maze restarted! Good luck 😊");
        playBackgroundMusic();
    }

    /**
     * Load a maze from a file.
     * @param actionEvent the event from the open file menu item
     */
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

    /**
     * Save the current maze to a file.
     * @param actionEvent the event from the save file menu item
     */
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

    /**
     * Creates a new maze file after user confirmation and input.
     * Prompts for maze size and file name, and saves the new maze.
     * @param actionEvent the event from the "New File" menu/button
     */
    public void newFile(ActionEvent actionEvent) {
        //ask the user to confirm creating a new maze file
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("New Maze File");
        alert.setHeaderText("Create a new empty maze?");
        alert.setContentText("This will clear the current maze. Unsaved work will be lost.");
        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");
        alert.getButtonTypes().setAll(yes, no);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty() || result.get() != yes) {
            actionEvent.consume();
            return;
        }

        //set up file chooser for saving the new maze
        FileChooser fc = new FileChooser();
        fc.setTitle("Create New Maze File");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.txt)", "*.txt"));
        fc.setInitialDirectory(new File("./ATP-Project-PartC/resources"));
        fc.setInitialFileName("newMaze.txt");

        //prompt for maze size
        TextField rowsInput = new TextField("50");
        TextField colsInput = new TextField("50");
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.add(new Label("Rows:"), 0, 0);
        inputGrid.add(rowsInput, 1, 0);
        inputGrid.add(new Label("Columns:"), 0, 1);
        inputGrid.add(colsInput, 1, 1);
        Alert inputDialog = new Alert(Alert.AlertType.CONFIRMATION);
        inputDialog.setTitle("Maze Size");
        inputDialog.setHeaderText("Enter number of rows and columns");
        inputDialog.getDialogPane().setContent(inputGrid);
        Optional<ButtonType> sizeResult = inputDialog.showAndWait();
        if (sizeResult.isEmpty() || sizeResult.get() != ButtonType.OK)
            return;

        try {
            int rows = Integer.parseInt(rowsInput.getText());
            int cols = Integer.parseInt(colsInput.getText());
            if (rows < 2 || cols < 2) {
                UIUtils.showError("Maze size must be at least 2x2.");
                return;
            }
            //stop any playing music before creating a new maze
            if (backgroundMusic != null)  backgroundMusic.stop();
            if (winMusic != null)  winMusic.stop();

            //let the user choose where to save the new maze file
            File file = fc.showSaveDialog(null);
            if (file != null) {
                if (!file.getName().toLowerCase().endsWith(".txt")) {
                    file = new File(file.getAbsolutePath() + ".txt");
                }
                mazeDisplayer.loadImagesOnce();
                viewModel.generateMaze(rows, cols);
                viewModel.saveMaze(file);
                UIUtils.showInfo("New maze created and saved to: " + file.getName());
            }
        } catch (NumberFormatException e) {
            UIUtils.showError("Rows and columns must be whole numbers.");
        } catch (Exception e) {
            UIUtils.showError("Failed to create new file: " + e.getMessage());
        }
    }

    /**
     * Handles the exit action and asks the user for confirmation before closing.
     * @param event the event from the close button or menu
     */
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
            viewModel.exit(); //call ViewModel exit for cleanup
            System.exit(0); //forcefully exit the program
        }
        else {
            if (event != null) { //if user cancels, consume the event to prevent closing
                event.consume();
            }
        }
    }

    /**
     * Handles key presses for moving the player in the maze.
     * @param keyEvent the key event
     */
    public void keyPressed(KeyEvent keyEvent) {
        viewModel.movePlayer(keyEvent);
        keyEvent.consume();
    }

    /**
     * Updates the player position on the maze and in the labels.
     * @param row the new row
     * @param col the new column
     */
    public void setPlayerPosition(int row, int col){
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }

    /**
     * Updates the goal position on the maze and in the labels.
     * @param row the new row
     * @param col the new column
     */
    public void setGoalPosition(int row, int col){
        mazeDisplayer.setGoalPosition(row, col);
        setUpdateGoalRow(row);
        setUpdateGoalCol(col);
    }

    /**
     * Called when the ViewModel notifies of a change.
     * Handles updates to the maze, player, or goal.
     * @param o the observable object
     * @param arg the update argument
     */
    @Override
    public void update(Observable o, Object arg) {
        String change = (String) arg;
        //check if the update is an error message
        if (change.startsWith("Error:")) {
            UIUtils.showError(change.substring(6).trim());
            return;
        }
        //handle the update type and call the relevant method
        switch (change){
            case "maze generated" -> mazeGenerated();
            case "player moved" -> playerMoved();
            case "maze solved" -> mazeSolved();
            case "goal reached" -> goalReached();
            case "maze loaded" -> mazeLoaded();
            default -> System.out.println("Not implemented change: " + change);
        }
    }

    /**
     * Shows the solution path on the maze.
     */
    private void mazeSolved() {
        mazeDisplayer.setSolution(viewModel.getSolution());
    }

    /**
     * Updates the player's position on the maze and in the UI.
     */
    private void playerMoved() {
        setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
    }

    /**
     * Called when a new maze is generated.
     * Draws the maze, updates the goal, enables buttons, and starts music.
     */
    private void mazeGenerated() {
        mazeDisplayer.drawMaze(viewModel.getMaze());
        setGoalPosition(viewModel.getGoalRow(), viewModel.getGoalCol());
        playBackgroundMusic();
        solveButton.setDisable(false);
        restartButton.setDisable(false);
    }

    /**
     * Called when the player reaches the goal.
     * Plays win music and shows a congratulation dialog.
     */
    private void goalReached() {
        playerMoved();
        playWinMusic();

        //show a congratulation dialog with a trophy image
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Congratulations!");
        alert.setHeaderText(null);
        alert.setContentText("You've reached the goal!");
        ImageView trophy = new ImageView(new Image(getClass().getResource("/images/trophy.png").toExternalForm()));
        trophy.setFitWidth(60);
        trophy.setFitHeight(60);
        alert.setGraphic(trophy);
        alert.showAndWait();
    }

    /**
     * Called after loading a maze from a file.
     * Loads images, draws the maze, and shows a success message.
     */
    private void mazeLoaded() {
        mazeDisplayer.loadImagesOnce();
        mazeGenerated();
        UIUtils.showInfo("Maze file loaded successfully. Have fun \uD83D\uDE0A");
    }

    /**
     * Handles mouse clicks on the maze. Moves the player if the clicked cell is adjacent and valid.
     * @param event MouseEvent from the mazeDisplayer
     */
    public void mouseClicked(MouseEvent event) {
        //get the row and column clicked by the user
        int clickedRow = mazeDisplayer.getRowFromY(event.getY());
        int clickedCol = mazeDisplayer.getColFromX(event.getX());
        if (clickedRow == -1 || clickedCol == -1) return;

        int currentRow = viewModel.getPlayerRow();
        int currentCol = viewModel.getPlayerCol();

        int dRow = clickedRow - currentRow;
        int dCol = clickedCol - currentCol;

        MovementDirection dir = null;
        //determine direction based on mouse movement
        if (dRow == -1 && dCol == 0) dir = MovementDirection.UP;
        else if (dRow == 1 && dCol == 0) dir = MovementDirection.DOWN;
        else if (dRow == 0 && dCol == -1) dir = MovementDirection.LEFT;
        else if (dRow == 0 && dCol == 1) dir = MovementDirection.RIGHT;
        else if (dRow == -1 && dCol == -1) dir = MovementDirection.UP_LEFT;
        else if (dRow == -1 && dCol == 1) dir = MovementDirection.UP_RIGHT;
        else if (dRow == 1 && dCol == -1) dir = MovementDirection.DOWN_LEFT;
        else if (dRow == 1 && dCol == 1) dir = MovementDirection.DOWN_RIGHT;
        //move the player if the direction is valid
        if (dir != null) {
            viewModel.updatePlayerLocation(dir);
        }
        mazeDisplayer.requestFocus();
        event.consume();
    }

    /**
     * Plays the background music in a loop.
     */
    private void playBackgroundMusic() {
        if (winMusic != null) {
            winMusic.stop();
        }

        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.play();
        }
    }

    /**
     * Plays the win music once.
     */
    private void playWinMusic() {
        initializeWinMusic();
        if (winMusic == null) return;

        if (backgroundMusic != null && backgroundMusic.getStatus() == MediaPlayer.Status.PLAYING)
            backgroundMusic.stop();

        winMusic.stop();
        winMusic.play();
    }

    /**
     * Shows the application properties in an information dialog.
     * @param actionEvent the event from the properties menu item
     */
    public void showProperties(ActionEvent actionEvent) {
        Properties props = loadProperties();
        if (!props.isEmpty()){
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

    /**
     * Loads application properties from the config.properties file.
     * @return Properties object with the loaded properties (empty if not found)
     */
    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getResourceAsStream("/config.properties")) {
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

    /**
     * Shows a Help dialog with instructions for the user.
     * @param actionEvent the event from the Help menu item
     */
    public void showHelp(ActionEvent actionEvent) {
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

        ▶ File Options:
        - 🆕 New: Create a new maze and save it to a new file.
        - 📂 Load: Load a saved maze from file.
        - 💾 Save: Save the current maze (including the player's current position).
        - ⚙ Generate Maze: Generate a new maze in memory without saving a file.
        
        ▶ Additional:
        - Press 'Solve Maze' to display the shortest path.
        - The solution path will be shown in green.
        - You can continue playing even after the solution is shown!

        Good luck! 🍄
        """;

        alert.getDialogPane().setMinWidth(500);
        alert.setContentText(helpText);
        alert.showAndWait();
    }

    /**
     * Shows an About dialog with application information.
     * @param actionEvent the event from the About menu item
     */
    public void showAbout(ActionEvent actionEvent) {
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
            - Best First Search (set in config file)

            🧪 Technologies:
            - JavaFX
            - Multithreading for performance
            - CSS Styling and FXML for UI

            🎮 Features:
            - Keyboard and mouse control
            - Sound effects and background music
            - Dynamic resizing

            💡 Motivation:
            This game was created as part of a university course on Advanced Programming,
            aiming to combine algorithms, UI and software design into one interactive experience.
            
            Thank you for playing! 👏
            """;

        alert.getDialogPane().setMinWidth(500);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Shows the settings window, allowing the user to adjust volume and dark mode.
     */
    @FXML
    public void showSettings() {
        //create the settings window layout
        Label title = new Label("Settings \uD83D\uDEE0");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        //volume slider for music
        Label volumeLabel = new Label("Music Volume:");
        Slider volumeSlider = new Slider(0, 100, 60);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        if (backgroundMusic != null)
            volumeSlider.setValue(backgroundMusic.getVolume() * 100);
        if (winMusic != null)
            volumeSlider.setValue(winMusic.getVolume() * 100);
        volumeSlider.valueProperty().addListener((obs, o, n) -> {
            double v = n.doubleValue() / 100.0;
            if (backgroundMusic != null) backgroundMusic.setVolume(v);
            if (winMusic != null)        winMusic.setVolume(v);
        });

        //dark mode checkbox
        CheckBox darkMode = new CheckBox("Enable Dark Mode");
        darkMode.setSelected(isDarkMode);
        darkMode.setOnAction(e -> {
            if (darkMode.isSelected()) {
                isDarkMode = true;
                if (!mainPane.getStyleClass().contains("dark"))
                    mainPane.getStyleClass().add("dark");
            } else {
                isDarkMode = false;
                mainPane.getStyleClass().remove("dark");
            }
        });

        //close button
        Button closeButton = new Button("Close");
        closeButton.setPrefWidth(100);
        closeButton.setStyle("-fx-background-radius: 8; -fx-padding: 6 12;");
        closeButton.setOnAction(e -> ((Stage) closeButton.getScene().getWindow()).close());
        VBox layout = new VBox(15, title, new HBox(10, volumeLabel, volumeSlider), darkMode, closeButton);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        //open settings window
        Stage settingsStage = new Stage();
        settingsStage.setTitle("Settings");
        settingsStage.setScene(new Scene(layout, 300, 200));
        settingsStage.initModality(Modality.APPLICATION_MODAL); //block the main window
        settingsStage.show();
    }
}