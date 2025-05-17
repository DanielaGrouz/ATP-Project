package Server;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.*;

import java.io.*;

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
            SearchableMaze searchableMaze = new SearchableMaze(maze);
            String mazeSearchingAlgorithm = configurations.get("mazeSearchingAlgorithm");
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
            System.out.println("Received dimensions");
            Solution solution = searcher.solve(searchableMaze);
            toClient.writeObject(solution);
            toClient.flush();
            fromClient.close();
            toClient.close();
        }catch (ClassNotFoundException e){
            throw new IOException("object not found");
        }
    }
}
