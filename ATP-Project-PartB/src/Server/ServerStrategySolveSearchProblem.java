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
            SearchableMaze searchableMaze = new SearchableMaze(maze);
            ISearchingAlgorithm searcher = new BestFirstSearch();
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
