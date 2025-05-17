package Server;

import IO.SimpleCompressorOutputStream;
import algorithms.mazeGenerators.*;

import java.io.*;

public class ServerStrategyGenerateMaze implements IServerStrategy{
    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) throws IOException {
        System.out.println("got new request");
        ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
        ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
        Configurations configurations = Configurations.getInstance();
        try{
            int[] mazeDimensions = (int[]) fromClient.readObject();
            System.out.println("Received dimensions");
            String mazeGeneratingAlgorithm = configurations.get("mazeGeneratingAlgorithm");
            IMazeGenerator generator;
            if (mazeGeneratingAlgorithm.equals("MyMazeGenerator")){
                generator = new MyMazeGenerator();
            }else if (mazeGeneratingAlgorithm.equals("SimpleMazeGenerator")){
                generator = new SimpleMazeGenerator();
            } else if (mazeGeneratingAlgorithm.equals("EmptyMazeGenerator")) {
                generator = new EmptyMazeGenerator();
            }else {
                System.out.println("not found mazeGeneratingAlgorithm for value " + mazeGeneratingAlgorithm);
                generator = new SimpleMazeGenerator(); // simple maze is the default choice
            }
            if (mazeDimensions.length < 2){
                throw new IllegalArgumentException("maze dimensions should be 2 integers");
            }
            Maze maze = generator.generate(mazeDimensions[0], mazeDimensions[1]);
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            SimpleCompressorOutputStream compressor = new SimpleCompressorOutputStream(byteOut); //change to myCompressor
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
