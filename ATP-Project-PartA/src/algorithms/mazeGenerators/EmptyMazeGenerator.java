package algorithms.mazeGenerators;

public class EmptyMazeGenerator extends AMazeGenerator {

    //method to generate an empty maze
    @Override
    public Maze generate(int rows, int columns) throws IndexOutOfBoundsException, IllegalArgumentException{
        if (rows <= 0 || columns <= 0){
            throw new IllegalArgumentException("row and column have to be positive numbers bigger than 0");
        }
        Maze MazeMatrix = new Maze(rows, columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                MazeMatrix.setPosition(i,j,0);
            }
        }
        return MazeMatrix;
    }
}

