import algorithms.mazeGenerators.*;
import algorithms.search.*;

import java.io.IOException;
import java.util.ArrayList;

    public class Main {

        public static String m_resultsFileName = "results.txt";

        public static enum TestStatus {
            Passed, Failed
        }

        public static void main(String[] args) {
            appendToResultsFile("Test started!");
            Tests_GenerateMaze();
            Tests_SearchOnMaze();
            appendToResultsFile("Test finished!");
        }

        private static String getTestStatusString(boolean testPassed) {
            return testPassed ? TestStatus.Passed.toString() : TestStatus.Failed.toString();
        }

        private static int[][] getRowsColumnsCombinations() {
            int[][] rowsColumnsCombinations = {
                    {5, 5}
            };
            return rowsColumnsCombinations;
        }

        //<editor-fold desc="Tests_GenerateMaze">
        private static void Tests_GenerateMaze() {
            int[][] rowsColumnsCombinations = getRowsColumnsCombinations();

            int rows;
            int columns;
            for (int i = 0; i < rowsColumnsCombinations.length; i++) {
                rows = rowsColumnsCombinations[i][0];
                columns = rowsColumnsCombinations[i][1];
                Test_MazeGenerator(new SimpleMazeGenerator(), rows, columns);
            }
        }

        private static void Test_MazeGenerator(IMazeGenerator mazeGenerator, int rows, int columns) {
            boolean testStatus = true;
            try {
                // prints the time it takes the algorithm to run
                mazeGenerator.measureAlgorithmTimeMillis(rows, columns);
                // generate another maze
                Maze maze = mazeGenerator.generate(rows, columns);
                // prints the maze
                maze.print();

                // get the maze entrance
                Position startPosition = maze.getStartPosition();
                startPosition.getColumnIndex();
                startPosition.getRowIndex();

                // get goal position
                Position startGoalPosition = maze.getGoalPosition();
                startPosition.getColumnIndex();
                startPosition.getRowIndex();
            } catch (Exception e) {
                testStatus = false;
            } finally {
                appendToResultsFile(String.format("TEST %s: generating maze (%s*%s) using %s", getTestStatusString(testStatus), rows, columns, mazeGenerator.getClass().getSimpleName().toString()));
            }
        }
        //</editor-fold>

        //<editor-fold desc="Tests_SearchOnMaze">
        private static void Tests_SearchOnMaze() {
            boolean testPassed;
            IMazeGenerator mg = new MyMazeGenerator();

            int[][] rowsColumnsCombinations = getRowsColumnsCombinations();

            int rows = 0;
            int columns = 0;
            for (int i = 0; i < rowsColumnsCombinations.length; i++) {
                try {
                    rows = rowsColumnsCombinations[i][0];
                    columns = rowsColumnsCombinations[i][1];

                    Maze maze = mg.generate(rows, columns);
                    SearchableMaze searchableMaze = new SearchableMaze(maze);

                    testPassed = solveProblem(searchableMaze, new BreadthFirstSearch(), rows, columns);

                } catch (Exception e) {
                    appendToResultsFile(String.format("Fatal Error when converting Maze to SearchableMaze (%s,%s): %s", rows, columns, e.getMessage()));
                }
            }
        }

        private static boolean solveProblem(ISearchable domain, ISearchingAlgorithm searcher, int rows, int columns) {
            boolean testStatus = false;
            try {
                //Solve a searching problem with a searcher
                Solution solution = searcher.solve(domain);
                ArrayList<AState> solutionPath = solution.getSolutionPath();
                testStatus = solutionPath.isEmpty() ? false : true;
            } catch (Exception e) {
                testStatus = false;
            } finally {
                appendToResultsFile(String.format("TEST %s: Applying %s on maze (%s,%s)", getTestStatusString(testStatus), searcher.getClass().getSimpleName(), rows, columns));
            }
            return testStatus;
        }
        //</editor-fold>

        public static void appendToResultsFile(String text) {
            try (java.io.FileWriter fw = new java.io.FileWriter(m_resultsFileName, true)) {
                fw.write(text + "\r\n");
            } catch (IOException ex) {
                System.out.println(String.format("Error appending text to file: %s", m_resultsFileName));
            }
        }
    }

