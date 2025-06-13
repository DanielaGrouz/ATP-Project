package Model;

import algorithms.mazeGenerators.MyMazeGenerator;

import java.util.Observable;
import java.util.Observer;
import algorithms.search.Solution;


public class MyModel extends Observable implements IModel{
    private int[][] maze; //TODO: לשנות סוג
    private int playerRow;
    private int playerCol;
    private Solution solution;
    private MyMazeGenerator generator;

    public MyModel() {
        generator = new MyMazeGenerator();
    }

    @Override
    public void generateMaze(int rows, int cols) {
        maze = generator.generate(rows, cols).getMazeMatrix();
        setChanged();
        notifyObservers("maze generated");
        // start position:
        movePlayer(0, 0);
    }

    @Override
    public int[][] getMaze() {
        return maze;
    }

    @Override
    public void updatePlayerLocation(MovementDirection direction) {
        switch (direction) {
            case UP -> {
                if (playerRow > 0)
                    movePlayer(playerRow - 1, playerCol);
            }
            case DOWN -> {
                if (playerRow < maze.length - 1)
                    movePlayer(playerRow + 1, playerCol);
            }
            case LEFT -> {
                if (playerCol > 0)
                    movePlayer(playerRow, playerCol - 1);
            }
            case RIGHT -> {
                if (playerCol < maze[0].length - 1)
                    movePlayer(playerRow, playerCol + 1);
            }
        }

    }

    private void movePlayer(int row, int col){
        this.playerRow = row;
        this.playerCol = col;
        setChanged();
        notifyObservers("player moved");
    }

    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    @Override
    public int getPlayerCol() {
        return playerCol;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void solveMaze() {
        //solve the maze
        solution = new Solution();
        setChanged();
        notifyObservers("maze solved");
    }

    @Override
    public Solution getSolution() {
        return solution;
    }
}
