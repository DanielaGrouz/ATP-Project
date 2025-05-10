package algorithms.mazeGenerators;

public class EmptyMazeGenerator extends AMazeGenerator {

    /**
     * method to generate an empty maze
     *
     * @param rows is the number of rows in the maze
     * @param columns is the number of columns in the maze
     * @return Maze object created
     * @throws IndexOutOfBoundsException if arguments rows and columns in func "setPosition" are invalid
     * @throws IllegalArgumentException if arguments rows and columns in func "setPosition" are invalid
     */
    @Override
    public Maze generate(int rows, int columns) throws IndexOutOfBoundsException, IllegalArgumentException{
        if (rows <= 1 || columns <= 1){
            return defaultMaze();
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

