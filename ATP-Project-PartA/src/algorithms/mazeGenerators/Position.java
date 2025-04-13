package algorithms.mazeGenerators;

public class Position {
    private int row;
    private int column;

    public Position(int row, int column){
        this.column = column;
        this.row = row;
    }

    public int getRowIndex(){
        return this.row;
    }

    public int getColumnIndex(){
        return this.column;
    }

    @Override
    public String toString() {
        return "{" + column + "," + row + "}";
    }

}
