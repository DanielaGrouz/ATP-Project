package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {
    private Maze maze;
    private Solution solution;
    //player position:
    private int playerRow = 0;
    private int playerCol = 0;
    //goal position:
    private int goalRow = 0;
    private int goalCol = 0;
    //images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameGoal = new SimpleStringProperty();
    private Image wallImg  = null;
    private Image playerImg = null;
    private Image goalImg   = null;

    private double zoomFactor = 1.0;
    private final double baseCellSize = 10;
    private final int cellSize = 10;

    public MazeDisplayer() {
        setOnScroll(event -> {
            if (event.isControlDown()) {
                double delta = event.getDeltaY();
                double scale = (delta > 0) ? 1.1 : 0.9;
                zoomFactor *= scale;
                zoomFactor = Math.max(0.5, Math.min(zoomFactor, 3.0));

                updateCanvasSize();
                draw();
                event.consume();
            }
        });

        widthProperty().addListener(e -> redrawMaze());
        heightProperty().addListener(e -> redrawMaze());

        setOnMousePressed(event -> {
            double cellWidth = baseCellSize * zoomFactor;
            double cellHeight = baseCellSize * zoomFactor;

            int col = (int) (event.getX() / cellWidth);
            int row = (int) (event.getY() / cellHeight);

            if (maze != null &&
                    maze.canPass(row, col) &&
                    isAdjacent(row, col)) {
                setPlayerPosition(row, col);
            }
        });

        setOnMouseMoved(event -> {
            double cellWidth = baseCellSize * zoomFactor;
            double cellHeight = baseCellSize * zoomFactor;

            int col = (int) (event.getX() / cellWidth);
            int row = (int) (event.getY() / cellHeight);

            if (maze != null && maze.canPass(row, col) && isAdjacent(row, col)) {
                setCursor(javafx.scene.Cursor.HAND);
            } else {
                setCursor(javafx.scene.Cursor.DEFAULT);
            }
        });

        setOnMouseDragged(event -> {
            double cellWidth = baseCellSize * zoomFactor;
            double cellHeight = baseCellSize * zoomFactor;

            int col = (int) (event.getX() / cellWidth);
            int row = (int) (event.getY() / cellHeight);

            if (maze != null && maze.canPass(row, col) && isAdjacent(row, col)) {
                setPlayerPosition(row, col);
            }
        });
    }

    public int getRowFromY(double y) {
        if (maze == null) return -1;
        double cellHeight = getHeight() / maze.getRows();
        return (int)(y / cellHeight);
    }

    public int getColFromX(double x) {
        if (maze == null) return -1;
        double cellWidth = getWidth() / maze.getColumns();
        return (int)(x / cellWidth);
    }

    public void redrawMaze() {
        if (maze == null) return;

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        int rows = maze.getRows();
        int cols = maze.getColumns();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!maze.canPass(r, c)) {
                    gc.setFill(Color.BLACK);
                } else {
                    gc.setFill(Color.WHITE);
                }
                gc.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
            }
        }
    }

    private void updateCanvasSize() {
        if (maze == null) return;

        int rows = maze.getRows();
        int cols = maze.getColumns();

        double maxCanvasSize = 800;

        double cellHeight = Math.min(baseCellSize, maxCanvasSize / rows) * zoomFactor;
        double cellWidth = Math.min(baseCellSize, maxCanvasSize / cols) * zoomFactor;

        setWidth(cols * cellWidth);
        setHeight(rows * cellHeight);
    }


    public void drawMaze(Maze maze) {
        this.maze = maze;
        solution=null;
        updateCanvasSize();
        draw();
    }

    private boolean isAdjacent(int row, int col) {
        int dRow = Math.abs(playerRow - row);
        int dCol = Math.abs(playerCol - col);
        return (dRow <= 1 && dCol <= 1 && !(dRow == 0 && dCol == 0));
    }

    private void draw() {
        if (maze != null) {
            int rows = maze.getRows();
            int cols = maze.getColumns();

            double maxCanvasSize = 800;

            double cellHeight = Math.min(baseCellSize, maxCanvasSize / rows) * zoomFactor;
            double cellWidth = Math.min(baseCellSize, maxCanvasSize / cols) * zoomFactor;

            double canvasHeight = cellHeight * rows;
            double canvasWidth = cellWidth * cols;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);

            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
            if (solution != null)
                drawSolution(graphicsContext, cellHeight, cellWidth);
            drawPlayer(graphicsContext, cellHeight, cellWidth);
            drawMazeGoal(graphicsContext, cellHeight, cellWidth);
        }
    }

    private void paintPosition(int r, int c, GraphicsContext graphicsContext, double Height, double Width) {
        if (maze != null) {
            double cellHeight = Height;
            double cellWidth = Width;
            double x = c * cellWidth;
            double y = r * cellHeight;

            graphicsContext.setGlobalAlpha(0.5);
            graphicsContext.setFill(Color.GREEN);
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
            graphicsContext.setGlobalAlpha(1.0);
        }
    }

    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.RED);
//        Image wallImage = null;
//        try {
//            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
//        } catch (FileNotFoundException e) {
//            System.out.println("There is no wall image file");
//        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze.getMazeMatrix()[i][j] == 1) {
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    if (wallImg == null) graphicsContext.fillRect(x,y,cellWidth, cellHeight);
                    else graphicsContext.drawImage(wallImg,x,y,cellWidth, cellHeight);
                }
            }
        }
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.BLUE);

        if (playerImg == null) graphicsContext.fillRect(x,y,cellWidth, cellHeight);
        else graphicsContext.drawImage(playerImg,x,y,cellWidth, cellHeight);

//        Image playerImage = null;
//        try {
//            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
//        } catch (FileNotFoundException e) {
//            showError("Error: There is no player image file");
//        }
//        if (playerImage == null)
//            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
//        else
//            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
   }

    private void drawMazeGoal(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getGoalCol() * cellWidth;
        double y = getGoalRow() * cellHeight;
        graphicsContext.setFill(Color.YELLOW);

        if (goalImg == null) graphicsContext.fillRect(x,y,cellWidth, cellHeight);
        else graphicsContext.drawImage(goalImg,x,y,cellWidth, cellHeight);

//        Image goalImage = null;
//        try {
//            goalImage = new Image(new FileInputStream(getImageFileNameGoal()));
//        } catch (FileNotFoundException e) {
//            showError("Error: There is no goal image");
//        }
//        if (goalImage == null) {
//            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
//        } else {
//            graphicsContext.drawImage(goalImage, x, y, cellWidth, cellHeight);
//        }
    }


    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public int getGoalRow() {
        return goalRow;
    }

    public int getGoalCol() {
        return goalCol;
    }

    public void setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        draw();
    }

    public void setGoalPosition(int row, int col) {
        this.goalRow = row;
        this.goalCol = col;
        draw();
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
        draw();
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public String imageFileNameWallProperty() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public String imageFileNamePlayerProperty() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    public String getImageFileNameGoal() {
        return imageFileNameGoal.get();
    }

    public String imageFileNameGoalProperty() {
        return imageFileNameGoal.get();
    }

    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.imageFileNameGoal.set(imageFileNameGoal);
    }

    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        System.out.println("drawing solution...");

        for(int i=0; i<solution.getSolutionPath().size();i++){
            //move on the solution path
            int row = ((MazeState) solution.getSolutionPath().get(i)).getRow();
            int col = ((MazeState) solution.getSolutionPath().get(i)).getCol();
            //if this point in the solution path is not a wall
            if(maze.getMazeMatrix()[row][col]==0){
                //call the function that add jellybean
                paintPosition(row, col, graphicsContext, cellHeight, cellWidth);
            }
        }
    }

    public void loadImagesOnce() {
        if (playerImg == null) {
            try { playerImg = new Image(new FileInputStream(getImageFileNamePlayer())); }
            catch (FileNotFoundException e) { showError("Player image not found"); }
        }
        if (wallImg == null) {
            try { wallImg = new Image(new FileInputStream(getImageFileNameWall())); }
            catch (FileNotFoundException e) { showError("Wall image not found"); }
        }
        if (goalImg == null) {
            try { goalImg = new Image(new FileInputStream(getImageFileNameGoal())); }
            catch (FileNotFoundException e) { showError("Goal image not found"); }
        }
    }


    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
