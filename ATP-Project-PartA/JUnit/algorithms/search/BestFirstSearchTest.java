package algorithms.search;

import org.junit.jupiter.api.Test;
import algorithms.mazeGenerators.*;
import algorithms.search.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BestFirstSearchTest {

    @Test
    void testNullProblem() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        assertThrows(IllegalArgumentException.class, () -> bestFirstSearch.solve(null));
    }

    @Test
    void testNoSolution() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        Maze maze = new EmptyMazeGenerator().generate(5, 5);
        Position start = new Position(0,0);
        Position end = new Position(5,5);
        maze.setMaze(null, start, end);

        SearchableMaze searchableMaze = new SearchableMaze(maze);
        Solution solution = bestFirstSearch.solve(searchableMaze);
        assertEquals(0, solution.getSolutionPath().size());
    }


    @Test
    void test2X2Maze() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();

        Maze maze = new EmptyMazeGenerator().generate(2, 2);
        Position start = new Position(0,0);
        Position end = new Position(1,1);
        maze.setMaze(null, start, end);

        SearchableMaze searchableMaze = new SearchableMaze(maze);
        Solution solution = bestFirstSearch.solve(searchableMaze);
        assertEquals(2, solution.getSolutionPath().size());
        assertEquals(15, solution.getSolutionPath().get(1).getCost());

    }

    @Test
    void test3X3Maze() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();

        Maze maze = new EmptyMazeGenerator().generate(3, 3);
        int[][] matrix = {{0,0,1}, {1,0,0}, {0,1,0}};
        Position start = new Position(0,0);
        Position end = new Position(2,2);
        maze.setMaze(matrix, start, end);

        SearchableMaze searchableMaze = new SearchableMaze(maze);
        Solution solution = bestFirstSearch.solve(searchableMaze);
        assertEquals(3, solution.getSolutionPath().size());
        assertEquals(30, solution.getSolutionPath().get(2).getCost());
    }

    @Test
    void testBigMaze() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        Maze maze = new SimpleMazeGenerator().generate(100, 100);

        SearchableMaze searchableMaze = new SearchableMaze(maze);
        Solution solution = bestFirstSearch.solve(searchableMaze);
        assertFalse(solution.getSolutionPath().isEmpty());

    }

    @Test
    void testGetName(){
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        assertEquals("Best First Search", bestFirstSearch.getName());
    }

}