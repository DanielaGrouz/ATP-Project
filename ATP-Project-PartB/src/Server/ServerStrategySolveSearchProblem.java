package Server;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.*;

import java.io.*;
import java.util.Arrays;

public class ServerStrategySolveSearchProblem implements IServerStrategy{
    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) throws IOException {
        System.out.println("got new request");
        ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
        ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
        Configurations configurations = Configurations.getInstance();
        try{
            System.out.println("Waiting for object...");
            Maze maze = (Maze) fromClient.readObject();
            String mazeSearchingAlgorithm = configurations.get("mazeSearchingAlgorithm");
            Solution solution = findSolution(maze, mazeSearchingAlgorithm);
            if (solution==null) { //the maze hasn't been solved before
                ISearchingAlgorithm searcher;
                if (mazeSearchingAlgorithm.equals("BestFirstSearch")){
                    searcher = new BestFirstSearch();
                } else if (mazeSearchingAlgorithm.equals("BreadthFirstSearch")) {
                    searcher = new BreadthFirstSearch();
                } else if (mazeSearchingAlgorithm.equals("DepthFirstSearch")) {
                    searcher = new DepthFirstSearch();
                } else {
                    System.out.println("not found mazeSearchingAlgorithm for value " + mazeSearchingAlgorithm);
                    searcher = new BestFirstSearch(); // using some default here
                }
                SearchableMaze searchableMaze = new SearchableMaze(maze);
                System.out.println("Received dimensions");
                solution = searcher.solve(searchableMaze);
                writeSolutionToFile(maze, solution, mazeSearchingAlgorithm);
            }
            toClient.writeObject(solution);
            toClient.flush();
            fromClient.close();
            toClient.close();
        }catch (ClassNotFoundException e){
            throw new IOException("object not found");
        }
    }

    private Solution findSolution(Maze maze, String mazeSearchingAlgorithm) throws IOException, ClassNotFoundException {
        Solution solution = null;
        int mazeID = Arrays.hashCode(maze.toByteArray());
        String tempDirectoryPath = System.getProperty("java.io.tmpdir");
        String fileName = tempDirectoryPath + "\\solution_" + mazeID + "_" + mazeSearchingAlgorithm + ".sol";

        File solutionFile = new File(fileName);
        if (solutionFile.exists()){
            FileInputStream fileInStream = new FileInputStream(solutionFile);
            ObjectInputStream objectInStream = new ObjectInputStream(fileInStream);
            solution = (Solution) objectInStream.readObject();
        }
        return solution;
    }

    private void writeSolutionToFile(Maze maze, Solution solution, String mazeSearchingAlgorithm) throws IOException {
        int mazeID = Arrays.hashCode(maze.toByteArray());
        String tempDirectoryPath = System.getProperty("java.io.tmpdir");
        String fileName = tempDirectoryPath + "\\solution_" + mazeID + "_" + mazeSearchingAlgorithm + ".sol";

        FileOutputStream fileOutStream = new FileOutputStream(fileName);
        ObjectOutputStream objectOutStream = new ObjectOutputStream(fileOutStream);
        objectOutStream.writeObject(solution);
    }
}
