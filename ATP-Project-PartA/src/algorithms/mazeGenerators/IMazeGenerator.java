package algorithms.mazeGenerators;

public interface IMazeGenerator {

    /**
     * Generates a new maze with the given dimensions.
     *
     * @param rows number of rows
     * @param columns number of columns
     * @return a fully initialized maze object
     */
    public Maze generate(int rows, int columns);

    /**
     * Measures the time, in milliseconds, that takes to create a maze in the requested size.
     *
     * @param rows number of rows
     * @param columns number of columns
     * @return elapsed time in milliseconds
     */
    public long measureAlgorithmTimeMillis(int rows, int columns);
}
