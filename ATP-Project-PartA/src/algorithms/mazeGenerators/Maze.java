package algorithms.mazeGenerators;

import java.util.Random;

public class Maze {
    private int[][] mazeMatrix;
    private int rows;
    private int columns;
    private Position start;
    private Position end;

    public Maze(int rows, int columns){
        this.mazeMatrix = new int[rows][columns];
        this.rows = rows;
        this.columns = columns;
    }

    public int SetPosition(int row, int column, int num){
        return this.mazeMatrix[row][column] = num;
    }

    public boolean canPass(int row, int column) {
        if (this.mazeMatrix[row][column] == 0) {
            return true;
        }
        return false;
    }

    public Position calcPosition(){
        Random rand = new Random();
        int row = rand.nextInt(rows);
        int column = rand.nextInt(columns);
        while (this.mazeMatrix[row][column] == 1 || (row != 0 && column != 0)) {
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

    public void print(){
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
