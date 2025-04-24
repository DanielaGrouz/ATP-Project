package algorithms.search;

import org.junit.jupiter.api.Test;
import algorithms.mazeGenerators.*;


import static org.junit.jupiter.api.Assertions.*;

class BestFirstSearchTest {

    @Test
    void testNullProblem() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        //assert that an IllegalArgumentException is thrown when solve(null) is called
        assertThrows(IllegalArgumentException.class, () -> bestFirstSearch.solve(null));
    }

    @Test
    void testNoSolution() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        //initialize the maze
        Maze maze = new EmptyMazeGenerator().generate(5, 5);
        Position start = new Position(0,0);
        Position end = new Position(5,5); //invalid end position
        maze.setMaze(null, start, end); //null means that the matrix will not change
        //solve the maze
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        Solution solution = bestFirstSearch.solve(searchableMaze);
        //assert that the solution path is empty
        assertEquals(0, solution.getSolutionPath().size());
    }

    @Test
    void test2X2Maze() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        //initialize the maze
        Maze maze = new EmptyMazeGenerator().generate(2, 2);
        Position start = new Position(0,0);
        Position end = new Position(1,1);
        maze.setMaze(null, start, end); //null means that the matrix will not change
        //solve the maze
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        Solution solution = bestFirstSearch.solve(searchableMaze);
        //assert that the solution path size is 2 and the cost is 15
        assertEquals(2, solution.getSolutionPath().size());
        assertEquals(15, solution.getSolutionPath().get(1).getCost());

    }

    @Test
    void test3X3Maze() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        //initialize the maze
        Maze maze = new EmptyMazeGenerator().generate(3, 3);
        int[][] matrix = {{0,0,1}, {1,0,0}, {0,1,0}};
        Position start = new Position(0,0);
        Position end = new Position(2,2);
        maze.setMaze(matrix, start, end);
        //solve the maze
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        Solution solution = bestFirstSearch.solve(searchableMaze);
        //assert that the solution path size is 3 and the cost is 30
        assertEquals(3, solution.getSolutionPath().size());
        assertEquals(30, solution.getSolutionPath().get(2).getCost());
    }

    @Test
    void testLargeMaze() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        //initialize large maze
        Maze maze = new SimpleMazeGenerator().generate(1000, 1000);
        //solve the maze
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        Solution solution = bestFirstSearch.solve(searchableMaze);
        //assert that the solution path is not empty
        assertFalse(solution.getSolutionPath().isEmpty());
    }

    @Test
    void testGetName(){
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        //assert that the getName method returns "Best First Search"
        assertEquals("Best First Search", bestFirstSearch.getName());
    }

}