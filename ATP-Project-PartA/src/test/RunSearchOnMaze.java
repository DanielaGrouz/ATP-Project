package test;

import algorithms.mazeGenerators.*;
import algorithms.search.*;

import java.util.ArrayList;

//import static jdk.internal.org.jline.utils.Colors.s;

public class RunSearchOnMaze {
    public static void main(String[] args) {
        IMazeGenerator mg = new SimpleMazeGenerator(); //MyMazeGenerator
        Maze maze = mg.generate(30, 30);

        maze.print();
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        solveProblem(searchableMaze, new BreadthFirstSearch());
        solveProblem(searchableMaze, new DepthFirstSearch());
        solveProblem(searchableMaze, new BestFirstSearch());

    }

    private static void solveProblem(ISearchable domain, ISearchingAlgorithm searcher) {
        //Solve a searching problem with a searcher
        Solution solution = searcher.solve(domain);
        System.out.println(String.format("'%s' algorithm - nodes evaluated: %s", searcher.getName(), searcher.getNumberOfNodesEvaluated()));
        //Printing Solution Path
        System.out.println("Solution path:");
        ArrayList<AState> solutionPath = solution.getSolutionPath();
        for (int i = 0; i < solutionPath.size(); i++) {
            System.out.println(String.format("%s. %s",i,solutionPath.get(i)));
        }
    }
}
