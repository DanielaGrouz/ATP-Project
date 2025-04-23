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
    void test3X3Maze() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();

        Maze maze = new Maze(3,3);
        int[][] matrix = {{0,0,0}, {0,0,0}, {0,0,0}};
        maze.getStartPosition().setRowIndex(0);
        maze.getStartPosition().setColumnIndex(0);
        maze.getGoalPosition().setRowIndex(2);
        maze.getGoalPosition().setColumnIndex(2);
        maze.setMazeMatrix(matrix);

        SearchableMaze searchableMaze = new SearchableMaze(maze);
        Solution solution = bestFirstSearch.solve(searchableMaze);
        assertEquals(3, solution.getSolutionPath().size());
    }

    @Test
    void test2X2Maze() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();

        Maze maze = new Maze(2,2);
        int[][] matrix = {{0,0}, {0,0}};
        maze.getStartPosition().setRowIndex(0);
        maze.getStartPosition().setColumnIndex(0);
        maze.getGoalPosition().setRowIndex(1);
        maze.getGoalPosition().setColumnIndex(1);
        maze.setMazeMatrix(matrix);

        SearchableMaze searchableMaze = new SearchableMaze(maze);
        Solution solution = bestFirstSearch.solve(searchableMaze);
        assertEquals(2, solution.getSolutionPath().size());
    }

    @Test
    void test1X1Maze() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();

//        Maze maze = new Maze(1,1);
//        int[][] matrix = {{0}};
//        maze.getStartPosition().setRowIndex(0);
//        maze.getStartPosition().setColumnIndex(0);
//        maze.getGoalPosition().setRowIndex(0);
//        maze.getGoalPosition().setColumnIndex(0);
//        maze.setMazeMatrix(matrix);
//
//        //אולי לזרוק שגיאה בcalc p
//        SearchableMaze searchableMaze = new SearchableMaze(maze);
//        Solution solution = bestFirstSearch.solve(searchableMaze);
//        assertEquals(1, solution.getSolutionPath().size());
    }


    @Test
    void testByCost() { //רלוונטי?
        BestFirstSearch bestFirstSearch = new BestFirstSearch();

        Maze maze = new Maze(3,3);
        int[][] matrix = {{0,0,0}, {0,0,0}, {0,0,0}};
        maze.getStartPosition().setRowIndex(0);
        maze.getStartPosition().setColumnIndex(0);
        maze.getGoalPosition().setRowIndex(2);
        maze.getGoalPosition().setColumnIndex(2);
        maze.setMazeMatrix(matrix);

        SearchableMaze searchableMaze = new SearchableMaze(maze);
        Solution solution = bestFirstSearch.solve(searchableMaze);
        assertEquals(3, solution.getSolutionPath().size());
    }

    @Test
    void testGetName(){
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        assertEquals("Best First Search", bestFirstSearch.getName());
    }

}