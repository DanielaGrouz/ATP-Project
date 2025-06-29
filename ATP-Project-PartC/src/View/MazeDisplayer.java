package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.Objects;

/**
 * MazeDisplayer is a custom JavaFX Canvas that displays a maze, the player, the goal, and the solution path.
 * It supports zooming, mouse-based movement, and dynamic resizing.
 */
public class MazeDisplayer extends Canvas {
    private Maze maze;
    private Solution solution;
    //player's current position:
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
    //controls zooming and base size of each cell in the maze display
    private double zoomFactor = 1.0;
    private final double baseCellSize = 10;
    private final int cellSize = 10;

    /**
     * Constructor for MazeDisplayer.
     * Sets up event handlers for zooming, resizing, and mouse interactions.
     */
    public MazeDisplayer() {
        //handle zooming with Ctrl + mouse scroll
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

        //redraw maze when canvas size changes
        widthProperty().addListener(e -> redrawMaze());
        heightProperty().addListener(e -> redrawMaze());

        //move player with mouse click if adjacent and valid
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

        //change cursor to hand if hovering over a valid adjacent cell
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

        //allow dragging the player to adjacent valid cells with the mouse
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

    /**
     * Converts a Y coordinate (in pixels) to a row index in the maze.
     * @param y The Y coordinate.
     * @return The row index, or -1 if maze is null.
     */
    public int getRowFromY(double y) {
        if (maze == null) return -1;
        double cellHeight = getHeight() / maze.getRows();
        return (int)(y / cellHeight);
    }

    /**
     * Converts an X coordinate (in pixels) to a column index in the maze.
     * @param x The X coordinate.
     * @return The column index, or -1 if maze is null.
     */
    public int getColFromX(double x) {
        if (maze == null) return -1;
        double cellWidth = getWidth() / maze.getColumns();
        return (int)(x / cellWidth);
    }

    /**
     * Updates the canvas size based on the maze size and zoom factor.
     */
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

    /**
     * Redraws the maze grid (walls and paths) on the canvas.
     */
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

    /**
     * Draws the maze and all its elements (walls, player, goal, solution).
     * @param maze The maze to display.
     */
    public void drawMaze(Maze maze) {
        this.maze = maze;
        solution = null;
        updateCanvasSize();
        draw();
    }

    /**
     * Checks if the given cell is adjacent to the player's current position.
     * @param row The row index.
     * @param col The column index.
     * @return true if the cell is adjacent, false otherwise.
     */
    private boolean isAdjacent(int row, int col) {
        int dRow = Math.abs(playerRow - row);
        int dCol = Math.abs(playerCol - col);
        return (dRow <= 1 && dCol <= 1 && !(dRow == 0 && dCol == 0));
    }

    /**
     * Draws the entire maze, including walls, player, goal, and solution path (if available).
     */
    private void draw() {
        if (maze != null) {
            int rows = maze.getRows();
            int cols = maze.getColumns();

            double maxCanvasSize = 800;
            //calculate cell size based on zoom and canvas constraints
            double cellHeight = Math.min(baseCellSize, maxCanvasSize / rows) * zoomFactor;
            double cellWidth = Math.min(baseCellSize, maxCanvasSize / cols) * zoomFactor;
            //calculate full canvas dimensions
            double canvasHeight = cellHeight * rows;
            double canvasWidth = cellWidth * cols;
            //clear previous drawing
            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);

            //draw maze walls, player position, goal position and solution path if it exists
            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
            if (solution != null)
                drawSolution(graphicsContext, cellHeight, cellWidth);
            drawPlayer(graphicsContext, cellHeight, cellWidth);
            drawMazeGoal(graphicsContext, cellHeight, cellWidth);
        }
    }

    /**
     * Draws the maze walls as red rectangles or images.
     * @param graphicsContext The graphics context to draw on.
     * @param cellHeight The height of a cell.
     * @param cellWidth The width of a cell.
     * @param rows Number of rows in the maze.
     * @param cols Number of columns in the maze.
     */
    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.RED);

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

    /**
     * Draws the player as a blue rectangle or image.
     * @param graphicsContext The graphics context to draw on.
     * @param cellHeight The height of a cell.
     * @param cellWidth The width of a cell.
     */
    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.BLUE);

        if (playerImg == null) graphicsContext.fillRect(x,y,cellWidth, cellHeight);
        else graphicsContext.drawImage(playerImg,x,y,cellWidth, cellHeight);
   }

    /**
     * Draws the goal as a yellow rectangle or image.
     * @param graphicsContext The graphics context to draw on.
     * @param cellHeight The height of a cell.
     * @param cellWidth The width of a cell.
     */
    private void drawMazeGoal(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getGoalCol() * cellWidth;
        double y = getGoalRow() * cellHeight;
        graphicsContext.setFill(Color.YELLOW);

        if (goalImg == null) graphicsContext.fillRect(x,y,cellWidth, cellHeight);
        else graphicsContext.drawImage(goalImg,x,y,cellWidth, cellHeight);
    }

    /**
     * Draws the solution path (if available) as green cells.
     * @param graphicsContext The graphics context to draw on.
     * @param cellHeight The height of a cell.
     * @param cellWidth The width of a cell.
     */
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

    /**
     * Paints a cell in green (used for the solution path).
     * @param r The row index.
     * @param c The column index.
     * @param graphicsContext The graphics context to draw on.
     * @param Height The height of a cell.
     * @param Width The width of a cell.
     */
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

    /**
     * @return The current row index of the player.
     */
    public int getPlayerRow() {
        return playerRow;
    }

    /**
     * @return The current column index of the player.
     */
    public int getPlayerCol() {
        return playerCol;
    }

    /**
     * @return The row index of the goal.
     */
    public int getGoalRow() {
        return goalRow;
    }

    /**
     * @return The column index of the goal.
     */
    public int getGoalCol() {
        return goalCol;
    }

    /**
     * Sets the player's position and redraws the maze.
     * @param row The new row index.
     * @param col The new column index.
     */
    public void setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        draw();
    }

    /**
     * Sets the goal's position and redraws the maze.
     * @param row The new row index for the goal.
     * @param col The new column index for the goal.
     */
    public void setGoalPosition(int row, int col) {
        this.goalRow = row;
        this.goalCol = col;
        draw();
    }

    /**
     * Sets the solution and redraws the maze.
     * @param solution The solution to display.
     */
    public void setSolution(Solution solution) {
        this.solution = solution;
        draw();
    }

    /**
     * @return The file name for the wall image.
     */
    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    /**
     * @return The wall image file name property.
     */
    public String imageFileNameWallProperty() {
        return imageFileNameWall.get();
    }

    /**
     * Sets the file name for the wall image.
     * @param imageFileNameWall The file name.
     */
    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    /**
     * @return The file name for the player image.
     */
    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    /**
     * @return The player image file name property.
     */
    public String imageFileNamePlayerProperty() {
        return imageFileNamePlayer.get();
    }

    /**
     * Sets the file name for the player image.
     * @param imageFileNamePlayer The file name.
     */
    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    /**
     * @return The file name for the goal image.
     */
    public String getImageFileNameGoal() {
        return imageFileNameGoal.get();
    }

    /**
     * @return The goal image file name property.
     */
    public String imageFileNameGoalProperty() {
        return imageFileNameGoal.get();
    }

    /**
     * Sets the file name for the goal image.
     * @param imageFileNameGoal The file name.
     */
    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.imageFileNameGoal.set(imageFileNameGoal);
    }

    /**
     * Loads the images for player, wall, and goal if they are not already loaded.
     * Shows an error dialog if an image file is not found.
     */
    public void loadImagesOnce() {
        if (playerImg == null) {
            try {
                playerImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(getImageFileNamePlayer())));
                if (playerImg.isError()) throw new Exception();
            } catch (Exception e) {
                UIUtils.showError("Player image not found");
            }
        }

        if (wallImg == null) {
            try {
                wallImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(getImageFileNameWall())));
                if (wallImg.isError()) throw new Exception();
            } catch (Exception e) {
                UIUtils.showError("Wall image not found");
            }
        }

        if (goalImg == null) {
            try {
                goalImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(getImageFileNameGoal())));
                if (goalImg.isError()) throw new Exception();
            } catch (Exception e) {
                UIUtils.showError("Goal image not found");
            }
        }
    }

}
