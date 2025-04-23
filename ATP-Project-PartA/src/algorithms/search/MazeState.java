package algorithms.search;

public class MazeState extends AState{

    private int row;
    private int col;

    /**
     * Constructs a new MazeState.
     *
     * @param row the row coordinate of the state
     * @param col the column coordinate of the state
     */
    public MazeState(int row, int col) {
        super("("+ row + "," + col +")");
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the row coordinate of this maze state.
     *
     * @return the row coordinate
     */
    public int getRow(){
        return row;
    }

    /**
     * Returns the column coordinate of this maze state.
     *
     * @return the column coordinate
     */
    public int getCol(){
        return col;
    }


}
