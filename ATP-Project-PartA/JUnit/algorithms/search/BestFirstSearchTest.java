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
}