package algorithms.mazeGenerators;

import java.util.Random;

public class Maze {
    private int[][] mazeMatrix;
    private int rows;
    private int columns;
    private Position start;
    private Position end;

    public Maze(int rows, int columns) throws IllegalArgumentException {
        if(rows < 0 || columns < 0){
            throw new IllegalArgumentException("rows and columns have to be positive numbers");
        }
        this.mazeMatrix = new int[rows][columns];
        this.rows = rows;
        this.columns = columns;
        this.start = calcPosition();
        this.end = calcPosition();
    }

    public int getRows(){
        return this.rows;
    }

    public int getColumns(){
        return this.columns;
    }

    public void setPosition(int row, int column, int num) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (row >= rows || column >= columns){
            throw new IndexOutOfBoundsException("index out of range");
        }
        else if (row < 0 || column < 0){
            throw new IllegalArgumentException("row and column have to be positive numbers");
        }
        this.mazeMatrix[row][column] = num;
    }

    //return true if it is a valid move, else return false
    public boolean canPass(int row, int column) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (row >= rows || column >= columns){
            throw new IndexOutOfBoundsException("index out of range");
        }
        else if (row < 0 || column < 0){
            throw new IllegalArgumentException("row and column have to be positive numbers");
        }
        if (this.mazeMatrix[row][column] == 0) {
            return true;
        }
        return false;
    }

    //calculates a random position in the maze walls
    public Position calcPosition(){
        Random rand = new Random();
        int row = rand.nextInt(rows);
        int column = rand.nextInt(columns);
        while (this.mazeMatrix[row][column] == 1 || (row != 0 && column != 0) ||
                (start != null && start.getRowIndex() == row && start.getColumnIndex() == column)) {
            row = rand.nextInt(rows);
            column = rand.nextInt(columns);
        }

        return new Position(row, column);
    }

    public Position getStartPosition(){
        return this.start;
    }

    public Position getGoalPosition() {
        return this.end;
    }

    public void print() throws IndexOutOfBoundsException, IllegalArgumentException{
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                if (i == this.getStartPosition().getRowIndex() && j == this.getStartPosition().getColumnIndex()) {
                    System.out.print("S ");
                }
                else if (i == this.getGoalPosition().getRowIndex() && j == this.getGoalPosition().getColumnIndex()) {
                    System.out.print("E ");
                }
                else if (this.canPass(i,j)) {
                    System.out.print("0 ");
                }

                else {
                    System.out.print("1 ");
                }
            }
            System.out.println();
        }
    }

}
