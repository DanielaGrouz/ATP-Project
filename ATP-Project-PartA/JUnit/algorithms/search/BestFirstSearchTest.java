package algorithms.search;

import org.junit.jupiter.api.Test;
import algorithms.mazeGenerators.*;
import algorithms.search.*;

import static org.junit.jupiter.api.Assertions.*;

class BestFirstSearchTest {

    public void test(){
        return;
    }
    //write your test here

    @Test
    void testNullProblem() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        assertThrows(IllegalArgumentException.class,() -> bestFirstSearch.solve(null));

    }

    @Test
    void testProblemWithoutStartOrEnd() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();

//        Maze maze = new Maze(3,3);
//        int[][] matrix = {{0,0,0}, {1,1,0}, {1,1,0}};
//        Position start = new Position();
//        Position end = new Position();
//        maze.setMazeMatrix(matrix);

        //assertThrows(IllegalArgumentException.class,() -> bestFirstSearch.solve(null));

    }


}