package algorithms.mazeGenerators;

import java.util.ArrayList;
import java.util.Random;

public class MyMazeGenerator extends AMazeGenerator {

    /**
     * method to generate a maze
     *
     * @param rows    is the number of rows in the maze
     * @param columns is the number of columns in the maze
     * @return Maze object created
     */
    @Override
    public Maze generate(int rows, int columns) {
        if (rows <= 1 || columns <= 1) {
            return defaultMaze();
        }

        Maze wallMaze = new Maze(rows, columns);

        //initialize all cells as walls - value : 1
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                wallMaze.getMazeMatrix()[i][j] = 1;
            }
        }

        int startRow = wallMaze.getStartPosition().getRowIndex();
        int startCol = wallMaze.getStartPosition().getColumnIndex();
        wallMaze.getMazeMatrix()[startRow][startCol] = 0;  //starting point

        //Add surrounding walls to the wall list
        ArrayList<Position> wallList = new ArrayList<>();
        addNeighboringWalls(startRow, startCol, wallList, rows, columns);

        Position goal = wallMaze.getGoalPosition();
        while (!wallList.isEmpty() || wallMaze.getMazeMatrix()[goal.getRowIndex()][goal.getColumnIndex()] == 1) {

            if (wallList.isEmpty()) {
                makePath(wallMaze, goal);
                return wallMaze;
            }

            //pick a random wall and remove it from the wall list
            Random rand = new Random();
            Position wall = wallList.remove(rand.nextInt(wallList.size()));
            if (countPathNeighbors(wallMaze, wall) == 1) {
                wallMaze.getMazeMatrix()[wall.getRowIndex()][wall.getColumnIndex()] = 0;
                addNeighboringWalls(wall.getRowIndex(), wall.getColumnIndex(), wallList, rows, columns);
            }
        }
        return wallMaze;
    }


    /**
     * helper func to method generate
     * method to check if the row and column in bounds of the WallMaze
     *
     * @param row    is the row number to check
     * @param column is the column number to check
     * @param height is the height of the wall maze
     * @param width  is the width of the wall maze
     * @return Maze object created
     */
    boolean isInBounds(int row, int column, int height, int width) {
        return row >= 0 && row < height && column >= 0 && column < width;
    }

    /**
     * helper func to method generate
     * method to add surrounding walls to wall list
     *
     * @param row      is the row number to add
     * @param col      is the column number to add
     * @param wallList id the list of walls
     */

    private void addNeighboringWalls(int row, int col, ArrayList<Position> wallList, int height, int width) {
        int[][] directions = {
                {-1, 0}, // up
                {1, 0},  // down
                {0, -1}, // left
                {0, 1}   // right
        };

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (isInBounds(newRow, newCol,height,width)) {
                wallList.add(new Position(newRow, newCol));
            }
        }
    }

    /**
     * helper func to method generate
     * method to count the surrounding paths to a specific position
     *
     * @param wallMaze is the maze object
     * @param position is the relevant position to check
     * @return int that represents the number of neighbors valued 0 to the given position
     */
    private int countPathNeighbors(Maze wallMaze, Position position){
        int count = 0;
        ArrayList<Position> pathNeighbors = new ArrayList<>();
        addNeighboringWalls(position.getRowIndex(), position.getColumnIndex(), pathNeighbors, wallMaze.getRows(), wallMaze.getColumns());
        for (Position neighbor : pathNeighbors) {
            if (wallMaze.getMazeMatrix()[neighbor.getRowIndex()][neighbor.getColumnIndex()] == 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * helper func to method generate
     * method to ensure that there is a valid path to the goal position
     *
     * @param wallMaze is the maze object
     * @param goal is the goal position
     */
//    private void makePath(Maze wallMaze, Position goal) {
//        int row = goal.getRowIndex();
//        int column = goal.getColumnIndex();
//        ArrayList<Position> neighbors = new ArrayList<>();
//        addNeighboringWalls(row, column, neighbors, wallMaze.getRows(), wallMaze.getColumns());
//        Position firstNeighbor = null;
//        for (Position neighbor : neighbors){
//            int r = neighbor.getRowIndex();
//            int c = neighbor.getColumnIndex();
//            if (wallMaze.getMazeMatrix()[r][c] == 0){
//                wallMaze.getMazeMatrix()[row][column] = 0;
//                return;
//            }
//            if (firstNeighbor == null){
//                firstNeighbor = neighbor;
//            }
//        }
//        if(firstNeighbor != null){
//            wallMaze.getMazeMatrix()[firstNeighbor.getRowIndex()][firstNeighbor.getColumnIndex()] = 0;
//        }
//        wallMaze.getMazeMatrix()[row][column] = 0;
//    }

    /**
     * Guarantees that the goal cell is connected to the maze by
     * either linking to an existing open neighbor or digging through one wall.
     *
     * @param maze  the maze to modify
     * @param goal  the destination cell that must be reachable
     */
    private void makePath(Maze maze, Position goal) {
        int gRow = goal.getRowIndex();
        int gCol = goal.getColumnIndex();

        // Collect all valid neighbors around the goal
        ArrayList<Position> neighbors = new ArrayList<>();
        addNeighboringWalls(gRow, gCol, neighbors,
                maze.getRows(), maze.getColumns());

        /* ---------- Phase 1: search for *any* open neighbor ---------- */
        ArrayList<Position> openNeighbors = new ArrayList<>();
        for (Position p : neighbors) {
            if (maze.getMazeMatrix()[p.getRowIndex()][p.getColumnIndex()] == 0) {
                openNeighbors.add(p);
            }
        }

        /* ---------- Phase 2: ensure connectivity ---------- */
        if (openNeighbors.isEmpty()) {
            // No open neighbor exists → choose one wall neighbor at random and open it
            Position toOpen = neighbors.get(new Random().nextInt(neighbors.size()));
            maze.getMazeMatrix()[toOpen.getRowIndex()][toOpen.getColumnIndex()] = 0;
        }
        // Whether we opened a wall or not, mark the goal cell itself as open
        maze.getMazeMatrix()[gRow][gCol] = 0;
    }


}
