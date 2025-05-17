package Server;
import algorithms.mazeGenerators.Maze;
import algorithms.search.*;

import java.io.*;

public class ServerStrategySolveSearchProblem implements IServerStrategy{
    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) throws IOException {
        System.out.println("got new request");
        ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
        ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
        try{
            System.out.println("Waiting for object...");
            Maze maze = (Maze) fromClient.readObject();
            Solution solution = findSolution(maze);
            if (solution==null){ //the maze hasn't been solved before
                ISearchingAlgorithm searcher = new BestFirstSearch();
                SearchableMaze searchableMaze = new SearchableMaze(maze);
                System.out.println("Received dimensions");
                solution = searcher.solve(searchableMaze);
                writeSolutionToFile(maze, solution);
            }
            toClient.writeObject(solution);
            toClient.flush();
            fromClient.close();
            toClient.close();
        }catch (ClassNotFoundException e){
            throw new IOException("object not found");
        }
    }

    private Solution findSolution(Maze maze){
        Solution solution = null;

        //

        return solution;
    }

    private void writeSolutionToFile(Maze maze, Solution solution) {
//            int mazeID = maze.getMazeID(); //.toString().hashCode();
//            String fileName = System.getProperty("java.io.tmpdir") + '\\' + "solution_" + mazeID + ".sol";
//             File solutionFile = new File(fileName);
//            if (solutionFile.exists()) {
//
//            }
        //FileOutputStream outStream = new FileOutputStream(fileName);
        //ObjectOutputStream fileObjectOut = new ObjectOutputStream(outStream);

    }
}
