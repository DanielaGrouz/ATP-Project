package Server;

import IO.SimpleCompressorOutputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.io.*;

public class ServerStrategyGenerateMaze implements IServerStrategy{
    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) throws IOException {
        System.out.println("got new request");
        ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
        SimpleCompressorOutputStream toClient = new SimpleCompressorOutputStream(outToClient);
        try{
            System.out.println("Waiting for object...");
            int[] mazeDimensions = (int[]) fromClient.readObject();
            System.out.println("Received dimensions");
            MyMazeGenerator generator = new MyMazeGenerator();
            if (mazeDimensions.length < 2){
                throw new IllegalArgumentException("maze dimension should be 2 integers");
            }
            Maze maze = generator.generate(mazeDimensions[0], mazeDimensions[1]);
            toClient.write(maze.toByteArray());
            toClient.flush();
            fromClient.close();
            toClient.close();
        }catch (ClassNotFoundException e){
            throw new IOException("object not found");
        }
    }
}
