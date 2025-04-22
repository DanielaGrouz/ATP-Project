package algorithms.mazeGenerators;

import java.util.ArrayList;
import java.util.Random;

public class MyMazeGenerator extends AMazeGenerator {

    Maze extractLogicalMaze(int[][] maze, int rows, int columns) throws IndexOutOfBoundsException, IllegalArgumentException {
        Maze result = new Maze(rows,columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result.setPosition(i,j,maze[2 * i + 1][2 * j + 1]);
            }
        }
        return result;
    }

    @Override
    public Maze generate(int rows, int columns) {
        if (rows <= 0 || columns <= 0){
            return null;
        }

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
        return extractLogicalMaze(wallMaze, rows, columns);
    }

    boolean isInBounds(int row, int column, int height, int width) {
        return row >= 0 && row < height && column >= 0 && column < width;
    }

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
