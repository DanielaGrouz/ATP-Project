package algorithms.mazeGenerators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public abstract class AMazeGenerator implements IMazeGenerator {

    /**
     * An abstract method to generate maze
     *
     * @param rows is the number of rows in the maze
     * @param columns is the number of columns in the maze
     * @return Maze object
     */
    public abstract Maze generate(int rows, int columns);

    /**
     * measuring Algorithm Time in Millis
     *
     * @param rows is the number of rows in the maze
     * @param columns is the number of columns in the maze
     * @return long with the result
     */
    public long measureAlgorithmTimeMillis(int rows, int columns) {
        long start = System.currentTimeMillis();
        generate(rows, columns);
        long end = System.currentTimeMillis();
        return end - start;
    }

    /**
     * Checks whether there is a valid path from the start position to the goal position in the given maze.
     *
     * @param maze the maze to be checked
     * @return true if there is a valid path from the start to the goal position, otherwise false
     * @throws IndexOutOfBoundsException if arguments rows and columns in func "canPass" are invalid
     * @throws IllegalArgumentException if arguments rows and columns in func "canPass" are invalid
     */
    public boolean hasPath(Maze maze) throws IndexOutOfBoundsException, IllegalArgumentException {
        int rows = maze.getRows();
        int columns = maze.getColumns();

        ArrayList<Position> list = new ArrayList<>();
        boolean[][] visited = new boolean[rows][columns];//all the cells are false

        list.add(maze.getStartPosition());
        visited[maze.getStartPosition().getRowIndex()][maze.getStartPosition().getColumnIndex()] = true;

        while (!list.isEmpty()) {
            Position current = list.remove(0);
            int currentRow = current.getRowIndex();
            int currentColumn = current.getColumnIndex();

            //we are at the goal position
            if (currentRow == maze.getGoalPosition().getRowIndex() && currentColumn == maze.getGoalPosition().getColumnIndex()) {
                return true;
            }

            for (int[] direction : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
                int newRow = currentRow + direction[0];
                int newColumn = currentColumn + direction[1];

                if (newRow >= 0 && newRow < maze.getRows() && //row in bounds
                    newColumn >= 0 && newColumn < maze.getColumns() && //column in bounds
                    maze.canPass(newRow, newColumn) && !visited[newRow][newColumn]) { //cell that we can pass and never passed before
                    list.add(new Position(newRow, newColumn));
                    visited[newRow][newColumn] = true;
                }
            }
        }
        return false; //there is no valid pass
    }

    /**
     * receives a maze and start and end positions and makes a random path in the maze
     *
     * @param maze is the current maze
     * @param start is the satrat position in the maze
     * @param goal is the end position in the maze
     * @throws IndexOutOfBoundsException if arguments rows and columns in func "setPosition" are invalid
     * @throws IllegalArgumentException if arguments rows and columns in func "setPosition" are invalid
     */
    public void makeRandomPath(Maze maze, Position start, Position goal) throws IndexOutOfBoundsException, IllegalArgumentException {
        int currentRow = start.getRowIndex();
        int currentCol = start.getColumnIndex();

        // Continue until we reach the goal position
        while (currentRow != goal.getRowIndex() || currentCol != goal.getColumnIndex()) {
            ArrayList<int[]> possibleMoves = new ArrayList<>();

            //valid moves
            if (currentRow < goal.getRowIndex()) possibleMoves.add(new int[]{1, 0}); // Move down
            if (currentRow > goal.getRowIndex()) possibleMoves.add(new int[]{-1, 0}); // Move up
            if (currentCol < goal.getColumnIndex()) possibleMoves.add(new int[]{0, 1}); // Move right
            if (currentCol > goal.getColumnIndex()) possibleMoves.add(new int[]{0, -1}); // Move left

            // Randomly shuffle possible moves to introduce randomness in the path
            Collections.shuffle(possibleMoves);

            for (int[] move : possibleMoves) {
                int newRow = currentRow + move[0];
                int newCol = currentCol + move[1];

                // If the new position is valid and inside maze bounds
                if (newRow >= 0 && newRow < maze.getRows() && newCol >= 0 && newCol < maze.getColumns()) {
                    // Move to the new position
                    currentRow = newRow;
                    currentCol = newCol;
                    maze.setPosition(currentRow, currentCol, 0); // Mark the new position as part of the path
                    break; //we move to a new position
                }
            }
        }
    }

    /**
     * method to create a default maze in case of invalid input of rows or columns
     *
     * @return default maze in size 10*10 with only walls inside
     */
    public Maze defaultMaze(){
        Maze MazeMatrix = new Maze(10, 10);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                MazeMatrix.setPosition(i,j,1);
            }
        }
        return MazeMatrix;
    }

}
