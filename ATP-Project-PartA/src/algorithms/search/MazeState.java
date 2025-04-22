package algorithms.search;

public class MazeState extends AState{

    private int row;
    private int col;

    public MazeState(int row, int col) {
        super("("+ row + "," + col +")");
        this.row = row;
        this.col = col;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

//    @Override
//    public boolean equals(Object obj) {
//
//        return true; /// לשנות?
//    }

}
