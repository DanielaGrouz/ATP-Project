package algorithms.search;

public class MazeState extends AState{

    private int currentRow;
    private int currentCol;

    public MazeState(int row, int col) {
        super("("+ row + "," + col +")");
        currentRow = row;
        currentCol = col;

        //איפה יש יצוג של כל הרופציות האפשריות להמשיך מהאינדקסים האלה - המצבים האפשריים כמו במצגת
    }
}
