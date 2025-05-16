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
        ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
        try{
            int[] mazeDimensions = (int[]) fromClient.readObject();
            System.out.println("Received dimensions");
            MyMazeGenerator generator = new MyMazeGenerator();
            if (mazeDimensions.length < 2){
                throw new IllegalArgumentException("maze dimension should be 2 integers");
            }
            Maze maze = generator.generate(mazeDimensions[0], mazeDimensions[1]);
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            SimpleCompressorOutputStream compressor = new SimpleCompressorOutputStream(byteOut);
            compressor.write(maze.toByteArray());
            compressor.close();

            byte[] compressedData = byteOut.toByteArray();
            toClient.writeObject(compressedData);
            toClient.flush();
            fromClient.close();
            toClient.close();
        }catch (ClassNotFoundException e){
            throw new IOException("object not found");
        }
    }
}
