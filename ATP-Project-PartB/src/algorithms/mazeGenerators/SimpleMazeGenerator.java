package algorithms.mazeGenerators;
import java.util.Random;
public class SimpleMazeGenerator extends AMazeGenerator{

    /**
     * helper func to method generate
     * method to makes a random path
     *
     * @param MazeMatrix a maze to create the path in
     * @param rows is the number of rows in the maze
     * @param columns is the number of columns in the maze
     * @throws IndexOutOfBoundsException if arguments rows and columns in func "setPosition" are invalid
     * @throws IllegalArgumentException if arguments rows and columns in func "setPosition" are invalid
     */
    private void randomPath(Maze MazeMatrix, int rows, int columns) throws IndexOutOfBoundsException, IllegalArgumentException {
        Random rand = new Random();
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < columns - 1; j++) {
                if (rand.nextInt(4) == 0) {
                    MazeMatrix.setPosition(i, j, 1);
                }
            }
        }
    }

    /**
     * method to generate a simple random maze
     *
     * @param rows is the number of rows in the maze
     * @param columns is the number of columns in the maze
     * @return Maze object created
     * @throws IndexOutOfBoundsException if arguments rows and columns in func "setPosition" are invalid
     * @throws IllegalArgumentException if arguments rows and columns in func "setPosition" are invalid
     */
    @Override
    public Maze generate(int rows, int columns) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (rows <= 1 || columns <= 1){
            return defaultMaze();
        }
        while (true) {
            Maze MazeMatrix = new Maze(rows, columns);
            Random rand = new Random();
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    MazeMatrix.setPosition(i, j, rand.nextInt(2));

                }
            }
            this.randomPath(MazeMatrix, rows, columns);
            //check if the maze has path
            if (hasPath(MazeMatrix)) {
                return MazeMatrix;
            } else {
                //makes a random path in the maze
                makeRandomPath(MazeMatrix, MazeMatrix.getStartPosition(), MazeMatrix.getGoalPosition());
                //check if the maze has path
                if (hasPath(MazeMatrix)) {
                    return MazeMatrix;
                }
            }
        }
    }
}
