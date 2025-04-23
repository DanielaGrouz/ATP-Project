package algorithms.mazeGenerators;

public class Position {
    private int row;
    private int column;

    public Position(int row, int column) throws IllegalArgumentException {
        if(row < 0 || column < 0){
            throw new IllegalArgumentException("row and column have to be positive numbers");
        }
        this.column = column;
        this.row = row;
    }

    /**
     * returns the index of the row position
     *
     * @return  int that represents the index of the row position
     */
    public int getRowIndex(){
        return this.row;
    }

    /**
     * returns the index of the column position
     *
     * @return int that represents the index of the column position
     */
    public int getColumnIndex(){
        return this.column;
    }

    /**
     * returns the String representation of the position
     *
     * @return String that represents the position
     */
    @Override
    public String toString() {
        return "{" + column + "," + row + "}";
    }

    public void setRowIndex(int row){
        this.row = row;
    }

    public void setColumnIndex(int column){
        this.column = column;
    }


}
