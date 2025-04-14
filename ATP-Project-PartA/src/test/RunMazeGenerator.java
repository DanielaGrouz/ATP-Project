package test;
import algorithms.mazeGenerators.*;
import org.testng.annotations.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RunMazeGenerator {
    public static void main(String[] args) {
        testMazeGenerator(new EmptyMazeGenerator());
        testMazeGenerator(new SimpleMazeGenerator());
        testMazeGenerator(new MyMazeGenerator());
    }

    private static void testMazeGenerator(IMazeGenerator mazeGenerator) {
// prints the time it takes the algorithm to run
        System.out.println(String.format("Maze generation time(ms): %s", mazeGenerator.measureAlgorithmTimeMillis(100/*rows*/, 100/*columns*/)));
// generate another maze
        Maze maze = mazeGenerator.generate(100/*rows*/, 100/*columns*/);
// prints the maze
        maze.print();
// get the maze entrance
        Position startPosition = maze.getStartPosition();
// print the start position
        System.out.println(String.format("Start Position: %s", startPosition)); // format "{row,column}"
// prints the maze exit position
        System.out.println(String.format("Goal Position: %s", maze.getGoalPosition()));
    }

    @Test
    public void testEmptyMazeNotNull() {
        IMazeGenerator gen = new EmptyMazeGenerator();
        Maze maze = gen.generate(10, 10);
        assertNotNull(maze);
    }

    @Test
    public void testSimpleMazeNotNull() {
        IMazeGenerator gen = new SimpleMazeGenerator();
        Maze maze = gen.generate(10, 10);
        assertNotNull(maze);
    }

    @Test
    public void testMyMazeNotNull() {
        IMazeGenerator gen = new MyMazeGenerator();
        Maze maze = gen.generate(10, 10);
        assertNotNull(maze);
    }

    @Test
    public void testStartAndGoalNotNull() {
        Maze maze = new EmptyMazeGenerator().generate(10, 10);
        assertNotNull(maze.getStartPosition());
        assertNotNull(maze.getGoalPosition());
    }

    @Test
    public void testStartAndGoalDifferent() {
        Maze maze = new EmptyMazeGenerator().generate(10, 10);
        Position start = maze.getStartPosition();
        Position goal = maze.getGoalPosition();
        assertFalse(start.getRowIndex() == goal.getRowIndex() &&
                start.getColumnIndex() == goal.getColumnIndex());
    }

    @Test
    public void testMazeMatrixSize() {
        int rows = 10, cols = 12;
        Maze maze = new EmptyMazeGenerator().generate(rows, cols);
        assertEquals(rows, maze.getRows());
        assertEquals(cols, maze.getColumns());
    }

    @Test
    public void testMazeCanPassStartAndGoal() {
        Maze maze = new MyMazeGenerator().generate(10, 10);
        assertTrue(maze.canPass(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex()));
        assertTrue(maze.canPass(maze.getGoalPosition().getRowIndex(), maze.getGoalPosition().getColumnIndex()));
    }

    @Test
    public void testZeroSizeMaze() {
        IMazeGenerator gen = new MyMazeGenerator();
        assertNull(gen.generate(0, 0));
    }

    @Test
    public void testNegativeSizeMaze() {
        IMazeGenerator gen = new SimpleMazeGenerator();
        assertThrows(NegativeArraySizeException.class, () -> gen.generate(-5, -5));
    }

    @Test
    public void testMazeHasPath() {
        AMazeGenerator gen = new MyMazeGenerator();
        Maze maze = gen.generate(20, 20);
        assertTrue(gen.hasPath(maze));
    }

    @Test
    public void testPositionToStringFormat() {
        Position pos = new Position(2, 3);
        assertEquals("{3,2}", pos.toString());
    }
}
