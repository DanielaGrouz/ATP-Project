package algorithms.mazeGenerators;

import java.util.ArrayList;
import java.util.Random;

public class MyMazeGenerator extends AMazeGenerator {

    /**
     * helper func to method generate
     * method to extract a logical maze from the generate maze func
     *
     * @param maze    an empty array to contain the new logical maze
     * @param rows    is the number of rows in the maze
     * @param columns is the number of columns in the maze
     * @return Maze object created
     * @throws IndexOutOfBoundsException if arguments rows and columns in func "setPosition" are invalid
     * @throws IllegalArgumentException  if arguments rows and columns in func "setPosition" are invalid
     */
    private Maze extractLogicalMaze(int[][] maze, int rows, int columns) throws IndexOutOfBoundsException, IllegalArgumentException {
        Maze result = new Maze(rows, columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result.setPosition(i, j, maze[2 * i + 1][2 * j + 1]);
            }
        }
        return result;
    }


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

        while (true) {
            int height = 2 * rows + 1;
            int width = 2 * columns + 1;
            int[][] wallMaze = new int[height][width];

            //initialize all cells as walls - value : 1
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    wallMaze[i][j] = 1;
                }
            }

            //pick a random starting cell
            Random rand = new Random();
            int startRow = 2 * rand.nextInt(rows) + 1;
            int startCol = 2 * rand.nextInt(columns) + 1;
            wallMaze[startRow][startCol] = 0;  //starting point

            //Add surrounding walls to the wall list
            ArrayList<Position> wallList = new ArrayList<>();
            addNeighboringWalls(startRow, startCol, wallList, height, width, wallMaze);

            while (!wallList.isEmpty()) {
                //pick a random wall and remove it from the wall list
                Position wall = wallList.remove(rand.nextInt(wallList.size()));

                int[][] directions = {
                        {-2, 0}, {2, 0}, {0, -2}, {0, 2}
                };

                for (int[] dir : directions) {
                    int cell1Row = wall.getRowIndex() + dir[0];
                    int cell1Col = wall.getColumnIndex() + dir[1];
                    int cell2Row = wall.getRowIndex() - dir[0];
                    int cell2Col = wall.getColumnIndex() - dir[1];

                    if (isInBounds(cell1Row, cell1Col, height, width) &&
                            isInBounds(cell2Row, cell2Col, height, width)) {

                        //connect the wall only if it separates two unvisited areas (one side has path, other side has wall)
                        if (wallMaze[cell1Row][cell1Col] == 1 && wallMaze[cell2Row][cell2Col] == 1) {
                            wallMaze[wall.getRowIndex()][wall.getColumnIndex()] = 0;  //break the wall

                            //make one of the cells a path
                            if (wallMaze[cell1Row][cell1Col] == 1) {
                                wallMaze[cell1Row][cell1Col] = 0;
                                addNeighboringWalls(cell1Row, cell1Col, wallList, height, width, wallMaze);
                            } else {
                                wallMaze[cell2Row][cell2Col] = 0;
                                addNeighboringWalls(cell2Row, cell2Col, wallList, height, width, wallMaze);
                            }
                            break;
                        }
                    }
                }
            }

            Maze maze = extractLogicalMaze(wallMaze, rows, columns);
            // check if the maze has a path
            if (hasPath(maze)) {
                return maze;
            } else {
                // makes a random path in the maze
                makeRandomPath(maze, maze.getStartPosition(), maze.getGoalPosition());
                // check if the maze has a path
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
        return row > 0 && row < height - 1 && column > 0 && column < width - 1;
    }


    /**
     * helper func to method generate
     * method to add surrounding walls to wall list
     *
     * @param row  is the row number to add
     * @param col  is the column number to add
     * @param wallList id the list of walls
     * @param height   is the height of the wall maze
     * @param width    is the width of the wall maze
     * @return Maze object created
     */
    private void addNeighboringWalls(int row, int col, ArrayList<Position> wallList, int height, int width, int[][] wallMaze) {
        int[][] directions = {
                {-2, 0}, // up
                {2, 0},  // down
                {0, -2}, // left
                {0, 2}   // right
        };

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (isInBounds(newRow, newCol, height, width) && wallMaze[newRow][newCol] == 1) {
                //the wall is between the current cell and the next cell
                int wallRow = row + dir[0] / 2;
                int wallCol = col + dir[1] / 2;
                wallList.add(new Position(wallRow, wallCol));
            }
        }
    }

}



