package algorithms.mazeGenerators;

public interface IMazeGenerator {

    Maze generate(int rows, int columns);

    long measureAlgorithmTimeMillis(int rows, int columns);
}
