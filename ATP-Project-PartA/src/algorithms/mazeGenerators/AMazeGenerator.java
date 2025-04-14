package algorithms.mazeGenerators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public abstract class AMazeGenerator implements IMazeGenerator {

    public abstract Maze generate(int rows, int columns);

    public long measureAlgorithmTimeMillis(int rows, int columns) {
        long start = System.currentTimeMillis();
        generate(rows, columns);
        long end = System.currentTimeMillis();
        return end - start;
    }

    public boolean hasPath(Maze maze) {
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

    public void makeRandomPath(Maze maze, Position start, Position goal) {
        Random rand = new Random();
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
                    maze.SetPosition(currentRow, currentCol, 0); // Mark the new position as part of the path
                    break; //we move to a new position
                }
            }
        }
    }


}
