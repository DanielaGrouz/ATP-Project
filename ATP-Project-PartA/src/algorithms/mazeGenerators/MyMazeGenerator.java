package algorithms.mazeGenerators;

import java.util.ArrayList;
import java.util.Random;

public class MyMazeGenerator extends AMazeGenerator {

    /**
     * helper func to method generate
     * method to extract a logical maze from the generate maze func
     *
     * @param maze an empty array to contain the new logical maze
     * @param rows is the number of rows in the maze
     * @param columns is the number of columns in the maze
     * @return Maze object created
     */
    public Maze extractLogicalMaze(int[][] maze, int rows, int columns) throws IndexOutOfBoundsException, IllegalArgumentException {
        Maze result = new Maze(rows,columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result.setPosition(i,j,maze[2 * i + 1][2 * j + 1]);
            }
        }
        return result;
    }

    /**
     * method to generate a maze
     *
     * @param rows is the number of rows in the maze
     * @param columns is the number of columns in the maze
     * @return Maze object created
     */
    @Override
    public Maze generate(int rows, int columns) {
        if (rows <= 0 || columns <= 0){
            throw new IllegalArgumentException("row and column have to be positive numbers bigger than 0");
        }

        while(true) {
            int height = 2 * rows + 1;
            int width = 2 * columns + 1;
            int[][] wallMaze = new int[height][width];


            //Initialize all cells as walls (1)
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    wallMaze[i][j] = 1;
                }
            }

            //Pick a random starting cell
            Random rand = new Random();
            //make sure it is not out of bounds
            int startRow = 2 * rand.nextInt(rows) + 1;
            int startCol = 2 * rand.nextInt(columns) + 1;
            wallMaze[startRow][startCol] = 0;

            // Add surrounding walls to wall list
            ArrayList<Position> wallList = new ArrayList<>();
            addNeighboringWalls(startRow, startCol, wallList, height, width);

            while (!wallList.isEmpty()) {
                // Pick a random wall and remove it from the wallList
                Position wall = wallList.remove(rand.nextInt(wallList.size()));

                int[][] directions = {
                        {-1, 0}, {1, 0}, {0, -1}, {0, 1}
                };

                for (int[] dir : directions) {
                    int cell1Row = wall.getRowIndex() + dir[0];
                    int cell1Col = wall.getColumnIndex() + dir[1];
                    int cell2Row = wall.getRowIndex() - dir[0];
                    int cell2Col = wall.getColumnIndex() - dir[1];

                    if (isInBounds(cell1Row, cell1Col, height, width) &&
                            isInBounds(cell2Row, cell2Col, height, width)) {

                        if (wallMaze[cell1Row][cell1Col] + wallMaze[cell2Row][cell2Col] == 1) { //only one of the two sides is part of the maze (0)
                            wallMaze[wall.getRowIndex()][wall.getColumnIndex()] = 0; // Break the wall
                            if (wallMaze[cell1Row][cell1Col] == 1) { //this cell part of the maze
                                wallMaze[cell1Row][cell1Col] = 0;
                                addNeighboringWalls(cell1Row, cell1Col, wallList, height, width);
                            } else {
                                wallMaze[cell2Row][cell2Col] = 0; //the other cell is part of the maze
                                addNeighboringWalls(cell2Row, cell2Col, wallList, height, width);
                            }
                            break;
                        }
                    }
                }
            }
            Maze maze = extractLogicalMaze(wallMaze, rows, columns);
            //check if the maze has path
            if (hasPath(maze)) {
                return maze;
            } else {
                //makes a random path in the maze
                makeRandomPath(maze, maze.getStartPosition(), maze.getGoalPosition());
                //check if the maze has path
                if (hasPath(maze)) {
                    return maze;
                }
            }
        }
    }

    /**
     * helper func to method generate
     * method to check if the row and column in bounds of the WallMaze
     *
     * @param row is the row number to check
     * @param column is the column number to check
     * @param height is the height of the wall maze
     * @param width is the width of the wall maze
     * @return Maze object created
     */
    boolean isInBounds(int row, int column, int height, int width) {
        return row >= 0 && row < height && column >= 0 && column < width;
    }

    /**
     * helper func to method generate
     * method to add surrounding walls to wall list
     *
     * @param cellRow is the row number to add
     * @param cellCol is the column number to add
     * @param wallList id the list of walls
     * @param height is the height of the wall maze
     * @param width is the width of the wall maze
     * @return Maze object created
     */
    //helper fun to generate func - Add surrounding walls to wall list
    void addNeighboringWalls(int cellRow, int cellCol, ArrayList<Position> wallList, int height, int width) {
        int[][] directions = {
                {-2, 0}, // up
                {2, 0},  // down
                {0, -2}, // left
                {0, 2}   // right
        };

        for (int[] dir : directions) {
            int nextRow = cellRow + dir[0];
            int nextCol = cellCol + dir[1];

            // The wall is between the current cell and the next cell
            int wallRow = cellRow + dir[0] / 2;
            int wallCol = cellCol + dir[1] / 2;

            if (isInBounds(nextRow, nextCol, height, width)) {
                wallList.add(new Position(wallRow, wallCol));
            }
        }
    }
}
