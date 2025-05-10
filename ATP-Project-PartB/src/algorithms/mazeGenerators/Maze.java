package algorithms.mazeGenerators;

import java.nio.ByteBuffer;
import java.util.Random;

public class Maze {
    private int[][] mazeMatrix;
    private int rows;
    private int columns;
    private Position start;
    private Position end;


    /**
     * constructor to Maze class - creates a maze object
     *
     * @param rows    of the maze
     * @param columns of the maze
     * @throws IllegalArgumentException if the number of rows or columns is negative or zero
     */
    public Maze(int rows, int columns) throws IllegalArgumentException {
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("rows and columns have to be positive numbers");
        }
        this.mazeMatrix = new int[rows][columns];
        this.rows = rows;
        this.columns = columns;
        this.start = calcPosition();
        this.end = calcPosition();
    }

    public Maze(byte[] array){
        ByteBuffer buffer = ByteBuffer.wrap(array);

        this.start = new Position(buffer.getInt(), buffer.getInt());
        this.end = new Position(buffer.getInt(), buffer.getInt());
        this.rows = buffer.getInt();
        this.columns = (array.length - 4 * 5) / this.rows;
        this.mazeMatrix = new int[rows][columns];
        for (int i=0;i< rows; i++){
            for(int j=0;j<columns;j++){
                mazeMatrix[i][j] = buffer.get();
            }
        }
    }

    /**
     * returns the number of rows
     *
     * @return int that represents the number of rows
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * returns the number of columns
     *
     * @return int that represents the number of columns
     */
    public int getColumns() {
        return this.columns;
    }

    /**
     * method to set a position in the maze as a specific number
     *
     * @param row    row number in the maze
     * @param column column number in the maze
     * @param num    new number to set in the position
     * @throws IndexOutOfBoundsException if arguments rows and columns are invalid
     * @throws IllegalArgumentException  if arguments rows and columns are invalid
     */
    public void setPosition(int row, int column, int num) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (row >= rows || column >= columns) {
            throw new IndexOutOfBoundsException("index out of range");
        } else if (row < 0 || column < 0) {
            throw new IllegalArgumentException("row and column have to be positive numbers");
        }
        else if(num !=1&&num !=0){
            throw new IllegalArgumentException("maze contains values of 1 or 0");
        }
        this.mazeMatrix[row][column]=num;
    }

    /**
     * method to check if the given input is a valid move
     *
     * @param row row number in the maze
     * @param column column number in the maze
     * @return true if it is a valid move, else return false
     * @throws IndexOutOfBoundsException if arguments rows and columns are invalid
     * @throws IllegalArgumentException if arguments rows and columns are invalid
     */
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

    /**
     * method to calculate a random position in the maze frame
     *
     * @return position object calculated
     */
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


    /**
     * method to get the start position in the maze
     *
     * @return start position object
     */
    public Position getStartPosition(){
        return this.start;
    }

    /**
     * method to get the goal position in the maze
     *
     * @return goal position object
     */
    public Position getGoalPosition() {
        return this.end;
    }

    /**
     * method to print the maze
     *
     * @throws IndexOutOfBoundsException if arguments rows and columns in the func "canPass" are invalid
     * @throws IllegalArgumentException if arguments rows and columns in the func "canPass" are invalid
     */
    public void print() throws IndexOutOfBoundsException, IllegalArgumentException{
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                if (i == this.getStartPosition().getRowIndex() && j == this.getStartPosition().getColumnIndex()) {
                    System.out.print("S ");
                } else if (i == this.getGoalPosition().getRowIndex() && j == this.getGoalPosition().getColumnIndex()) {
                    System.out.print("E ");
                } else if (this.canPass(i, j)) {
                    System.out.print("0 ");
                } else {
                    System.out.print("1 ");
                }
            }
            System.out.println();
        }
    }

    /**
     * method to set the maze with a given input
     * this method used only for tests in this project
     *
     * @param mazeMatrix an input int matrix to set as the maze matrix
     * @param start a position to set as start position
     * @param end a position to set as goal position
     */
    public void setMaze(int[][] mazeMatrix, Position start, Position end){
        if (mazeMatrix!=null)
            this.mazeMatrix = mazeMatrix;
        this.start = start;
        this.end = end;
    }

    /**
     * method to get the maze matrix
     *
     * @return int[][] array of numbers 0 or 1 represents the maze matrix
     */
    public int[][] getMazeMatrix (){
        return mazeMatrix;
    }

    public byte[] toByteArray(){
        // the format is [start_row,start_column, end_row,end_column, rows, ...matrix in one array(flatten)]
        // columns can be computed from array length
        ByteBuffer buffer = ByteBuffer.allocate(4*5 + rows * columns);
        buffer.putInt(start.getRowIndex());
        buffer.putInt(start.getColumnIndex());

        buffer.putInt(end.getRowIndex());
        buffer.putInt(end.getColumnIndex());

        buffer.putInt(rows);
        for (int i = 0;i<rows; i++){
            for (int j=0;j < columns; j ++){
                buffer.put((byte) (mazeMatrix[i][j])); // value is always 0 or 1
            }
        };
        return buffer.array();
    }

}
