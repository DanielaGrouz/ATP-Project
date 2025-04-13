package algorithms.mazeGenerators;
import java.util.Random;
public class SimpleMazeGenerator extends AMazeGenerator{

    public void randomPath(Maze MazeMatrix, int rows, int columns) {
        Random rand = new Random();
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < columns - 1; j++) {
                if (rand.nextInt(4) == 0) {
                    MazeMatrix.SetPosition(i, j, 1);
                }
            }
        }
    }

    @Override
    public Maze generate(int rows, int columns) {
        Maze MazeMatrix = new Maze(rows, columns);
        Random rand = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                MazeMatrix.SetPosition(i, j, rand.nextInt(2)); // 0 או 1

            }
        }
        this.randomPath(MazeMatrix, rows, columns);
        return MazeMatrix;
    }

}
