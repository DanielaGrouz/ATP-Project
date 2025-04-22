package algorithms.mazeGenerators;
import java.util.Random;
public class SimpleMazeGenerator extends AMazeGenerator{

    //helper func to generate func - makes a random path
    public void randomPath(Maze MazeMatrix, int rows, int columns) throws IndexOutOfBoundsException, IllegalArgumentException {
        Random rand = new Random();
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < columns - 1; j++) {
                if (rand.nextInt(4) == 0) {
                    MazeMatrix.setPosition(i, j, 1);
                }
            }
        }
    }

    //method to generate a simple random maze
    @Override
    public Maze generate(int rows, int columns) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (rows <= 0 || columns <= 0){
            return null;
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
