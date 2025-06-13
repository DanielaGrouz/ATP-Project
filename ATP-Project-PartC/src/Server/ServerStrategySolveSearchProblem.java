package Server;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.*;

import java.io.*;
import java.util.Arrays;

/**
 * A server strategy that solves a maze sent by the client.
 * If the solution already exists, it reads it from a file. Otherwise, it solves the maze,
 * saves the solution, and sends it back to the client.
 */
public class ServerStrategySolveSearchProblem implements IServerStrategy{

    /**
     * Reads a maze from the client, solves it (or retrieves a saved solution),
     * and sends the solution back.
     *
     * @param inFromClient  input stream from the client
     * @param outToClient   output stream to the client
     * @throws IOException if a communication or file error occurs
     */
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
                //choose algorithm based on config
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
                System.out.println("Solved maze and saved solution to file");
                solution = searcher.solve(searchableMaze);
                writeSolutionToFile(maze, solution, mazeSearchingAlgorithm);
            }
            //send the solution back to the client and close streams
            toClient.writeObject(solution);
            toClient.flush();
            fromClient.close();
            toClient.close();
        }catch (ClassNotFoundException e){
            throw new IOException("object not found");
        }
    }

    /**
     * Tries to load a saved solution from a file,The file name is based on the hash of the maze and algorithm name.
     *
     * @param maze the maze to solve
     * @param mazeSearchingAlgorithm algorithm name
     * @return Solution if found in file, otherwise null
     * @throws IOException if there is a file reading error
     * @throws ClassNotFoundException if the solution object cannot be read
     */
    private Solution findSolution(Maze maze, String mazeSearchingAlgorithm) throws IOException, ClassNotFoundException {
        Solution solution = null;
        int mazeID = Arrays.hashCode(maze.toByteArray());
        //use system temporary directory for storing solutions
        String tempDirectoryPath = System.getProperty("java.io.tmpdir");
        String fileName = tempDirectoryPath + "\\solution_" + mazeID + "_" + mazeSearchingAlgorithm + ".sol";

        File solutionFile = new File(fileName);
        if (solutionFile.exists()){
            System.out.println("Reading solution from file");
            FileInputStream fileInStream = new FileInputStream(solutionFile);
            ObjectInputStream objectInStream = new ObjectInputStream(fileInStream);
            solution = (Solution) objectInStream.readObject();
        }
        return solution;
    }

    /**
     * Saves a solution to a file for future use.
     *
     * @param maze the maze that was solved
     * @param solution the solution to save
     * @param mazeSearchingAlgorithm algorithm used to solve the maze
     * @throws IOException if there is an error during file writing
     */
    private void writeSolutionToFile(Maze maze, Solution solution, String mazeSearchingAlgorithm) throws IOException {
        int mazeID = Arrays.hashCode(maze.toByteArray());
        //use system temporary directory for storing solutions
        String tempDirectoryPath = System.getProperty("java.io.tmpdir");
        String fileName = tempDirectoryPath + "\\solution_" + mazeID + "_" + mazeSearchingAlgorithm + ".sol";

        //write solution to file
        FileOutputStream fileOutStream = new FileOutputStream(fileName);
        ObjectOutputStream objectOutStream = new ObjectOutputStream(fileOutStream);
        objectOutStream.writeObject(solution);
    }
}
