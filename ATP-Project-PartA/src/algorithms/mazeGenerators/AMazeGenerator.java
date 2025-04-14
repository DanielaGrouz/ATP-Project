package algorithms.mazeGenerators;

import java.util.ArrayList;

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

}
