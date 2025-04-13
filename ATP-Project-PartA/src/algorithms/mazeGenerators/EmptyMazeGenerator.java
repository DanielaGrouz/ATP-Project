package algorithms.mazeGenerators;

public class EmptyMazeGenerator extends AMazeGenerator {

    @Override
    public Maze generate(int rows, int columns){
    Maze MazeMatrix = new Maze(rows, columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                MazeMatrix.SetPosition(i,j,0);
            }
        }
        return MazeMatrix;
    }
}

