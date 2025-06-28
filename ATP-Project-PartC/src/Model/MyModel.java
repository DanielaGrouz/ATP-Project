package Model;

import Client.Client;
import IO.MyDecompressorInputStream;
import IO.SimpleDecompressorInputStream;
import Server.Server;
import Server.Configurations;
import Server.ServerStrategySolveSearchProblem;
import Server.ServerStrategyGenerateMaze;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import java.io.*;
import java.net.InetAddress;
import java.util.Observable;
import java.util.Observer;

/**
 * This class implements the Model part of the application.
 * It manages the maze data, player position, and server communication.
 */
public class MyModel extends Observable implements IModel{
    private Maze maze;
    private Solution solution;
    private int playerRow;
    private int playerCol;
    private int goalRow;
    private int goalCol;
    private boolean reachedGoal;
    Server solveSearchProblemServer; //the server that solves the maze
    Server mazeGeneratingServer; //the server that generate the maze

    /**
     * Constructor - starts the maze generation and solving servers.
     */
    public MyModel() {
        solveSearchProblemServer = new Server(4001, 1000, new ServerStrategySolveSearchProblem());
        solveSearchProblemServer.start();
        mazeGeneratingServer = new Server(4002, 1000, new ServerStrategyGenerateMaze());
        mazeGeneratingServer.start();

    }

    /**
     * Generates a new maze with the received measurements using the server.
     * @param rows Number of rows in the new maze.
     * @param cols Number of columns in the new maze.
     */
    @Override
    public void generateMaze(int rows, int cols) {
        //reset solution and reachedGoal status
        solution = null;
        reachedGoal = false;
        setChanged();
        try {
            //create a client to communicate with the maze generation server
            Client client = new Client(InetAddress.getLocalHost(), 4002, (inFromServer, outToServer) -> {
                try {
                    //send maze dimensions to the server
                    ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                    toServer.flush();
                    int[] mazeDimensions = new int[]{rows, cols};
                    toServer.writeObject(mazeDimensions);
                    toServer.flush();

                    //receive compressed maze from the server
                    ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                    byte[] compressedMaze = (byte[])fromServer.readObject();

                    //get the configured decompressor type
                    Configurations configurations = Configurations.getInstance();
                    String mazeCompressor = configurations.get("mazeCompressor");
                    InputStream is;
                    if (mazeCompressor.equals("SimpleCompressorOutputStream")) {
                        is = new SimpleDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                    } else if (mazeCompressor.equals("MyCompressorOutputStream")) {
                        is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));

                    } else {
                        System.out.println("not found mazeCompressor for value " + mazeCompressor);
                        is = new SimpleDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                    }
                    //decompress the maze data and create the maze object
                    byte[] decompressedMaze = new byte[rows*cols + 20];
                    is.read(decompressedMaze);
                    maze = new Maze(decompressedMaze);
                    goalRow=maze.getGoalPosition().getRowIndex();
                    goalCol=maze.getGoalPosition().getColumnIndex();

                    //notify observers that the maze was generated
                    notifyObservers("maze generated");
                    //move player to the start position
                    movePlayer(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());

                } catch (Exception e) {
                    //notify observers about any error
                    notifyObservers("Error:" + e.getMessage());
                }
            });
            //start communication with the server
            client.communicateWithServer();

        } catch (Exception e) {
            //notify observers about any error
            setChanged();
            notifyObservers("Error:" + e.getMessage());
        }

    }

    /**
     * Resets the player position to start position.
     */
    @Override
    public void restartMaze(){
        movePlayer(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());
        reachedGoal=false;
        setChanged();
        notifyObservers("player moved");
    }

    /**
     * @return the current maze.
     */
    @Override
    public Maze getMaze() {
        return maze;
    }

    /**
     * @return Current row of the player.
     */
    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    /**
     * @return Current column of the player.
     */
    @Override
    public int getPlayerCol() {
        return playerCol;
    }

    /**
     * @return Row of the goal position.
     */
    @Override
    public int getGoalRow() {
        return goalRow;
    }

    /**
     * @return Column of the goal position.
     */
    @Override
    public int getGoalCol() {
        return goalCol;
    }

    /**
     * Updates the player's position in the maze according to the given direction,
     * if the move is valid (within maze bounds and not blocked by a wall).
     * If the player has already reached the goal, no movement is performed.
     *
     * @param direction The direction in which the player should move.
     * @return true if the player's position was updated (valid move), false otherwise.
     */
    @Override
    public boolean updatePlayerLocation(MovementDirection direction) {
        //do not move if the goal has already been reached
        if (reachedGoal) {
            return false;
        }

        //try to move in given direction if possible
        switch (direction) {
            case UP -> {
                if (playerRow > 0 && maze.getMazeMatrix()[playerRow - 1][playerCol] != 1) {
                    movePlayer(playerRow - 1, playerCol);
                    return true;
                }
            }
            case DOWN -> {
                if (playerRow < maze.getRows() - 1 && maze.getMazeMatrix()[playerRow + 1][playerCol] != 1) {
                    movePlayer(playerRow + 1, playerCol);
                    return true;
                }
            }
            case LEFT -> {
                if (playerCol > 0 && maze.getMazeMatrix()[playerRow][playerCol - 1] != 1) {
                    movePlayer(playerRow, playerCol - 1);
                    return true;
                }
            }
            case RIGHT -> {
                if (playerCol < maze.getColumns() - 1 && maze.getMazeMatrix()[playerRow][playerCol + 1] != 1) {
                    movePlayer(playerRow, playerCol + 1);
                    return true;
                }
            }
            case UP_LEFT -> {
                if (playerRow > 0 && playerCol > 0 && maze.getMazeMatrix()[playerRow - 1][playerCol - 1] != 1) {
                    movePlayer(playerRow - 1, playerCol - 1);
                    return true;
                }
            }
            case UP_RIGHT -> {
                if (playerRow > 0 && playerCol < maze.getColumns() - 1 && maze.getMazeMatrix()[playerRow - 1][playerCol + 1] != 1) {
                    movePlayer(playerRow - 1, playerCol + 1);
                    return true;
                }
            }
            case DOWN_LEFT -> {
                if (playerRow < maze.getRows() - 1 && playerCol > 0 && maze.getMazeMatrix()[playerRow + 1][playerCol - 1] != 1) {
                    movePlayer(playerRow + 1, playerCol - 1);
                    return true;
                }
            }
            case DOWN_RIGHT -> {
                if (playerRow < maze.getRows() - 1 && playerCol < maze.getColumns() - 1 && maze.getMazeMatrix()[playerRow + 1][playerCol + 1] != 1) {
                    movePlayer(playerRow + 1, playerCol + 1);
                    return true;
                }
            }
        }
        //if none of the moves were valid, return false
        return false;
    }

    /**
     * Updates the player's position to the specified row and column.
     * Notifies observers whether the player has reached the goal or just moved.
     *
     * @param row The new row position for the player.
     * @param col The new column position for the player.
     */
    private void movePlayer(int row, int col){
        System.out.println("movePlayer called with: " + row + "," + col);
        //update player position
        this.playerRow = row;
        this.playerCol = col;

        //mark the observable as changed
        setChanged();
        if (reachedGoal()) {
            System.out.println("Player reached the goal at: " + row + "," + col);
            reachedGoal = true;
            notifyObservers("goal reached");
        } else {
            notifyObservers("player moved");
        }
    }

    /**
     * Sends the current maze to the server for solving.
     * Receives the solution from the server and notifies observers when done.
     */
    @Override
    public void solveMaze() {
        try {
            //create a client to communicate with the maze-solving server
            Client client = new Client(InetAddress.getLocalHost(), 4001, (inFromServer, outToServer) -> {
                try {
                    //send the maze object to the server
                    ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                    toServer.flush();
                    toServer.writeObject(maze);
                    toServer.flush();

                    //receive the solution from the server
                    ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                    solution = (Solution) fromServer.readObject();

                    //notify observers that the maze has been solved
                    setChanged();
                    notifyObservers("maze solved");
                } catch (Exception e) {
                    //notify observers about any error
                    setChanged();
                    notifyObservers("Error:" + e.getMessage());
                }
            });
            //start communication with the server
            client.communicateWithServer();

        } catch (Exception e) {
            //notify observers about any error
            setChanged();
            notifyObservers("Error:" + e.getMessage());
        }
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    /**
     * @return true if player reached the goal position
     */
    @Override
    public boolean reachedGoal(){
        if(playerRow == goalRow && playerCol == goalCol){
            return true;
        }
        return false;
    }

    /**
     * Saves the current maze and player position to the specified file.
     *
     * @param file The file to which the maze and player position will be saved.
     */
    @Override
    public void saveMaze(File file) {
        //check if there is a maze to save
        if (maze == null) {
            setChanged();
            notifyObservers("Error: No maze to save. Please generate a maze first.");
            return;
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            //write the maze object and current player position to the file
            out.writeObject(maze);
            out.writeInt(playerRow);
            out.writeInt(playerCol);
        }
        catch (Exception e) {
            //notify observers about any error
            setChanged();
            notifyObservers("Error:" + e.getMessage());
        }
    }

    /**
     * Loads a maze and player position from the specified file.
     * Updates the goal position, resets the solution, and notifies observers.
     *
     * @param file The file from which to load the maze and player position.
     */
    @Override
    public void openMaze(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            //read the maze object and current player position from the file
            maze = (Maze) in.readObject();
            playerRow = in.readInt();
            playerCol = in.readInt();

            //move the player to the loaded position
            movePlayer(playerRow, playerCol);
            //update goal position
            goalRow=maze.getGoalPosition().getRowIndex();
            goalCol=maze.getGoalPosition().getColumnIndex();
            //reset solution and reachedGoal status
            this.solution = null;
            this.reachedGoal = (goalRow==playerRow && goalCol==playerCol);
            setChanged();
            notifyObservers("maze loaded");
        } catch (Exception e) {
            //notify observers about any error
            setChanged();
            notifyObservers("Error: " + e.getMessage());
        }
    }

    /**
     * Assigns an observer to observe model updates.
     */
    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    /**
     * Stops the servers and exits the program.
     */
    @Override
    public void exit(){
        solveSearchProblemServer.stop();
        mazeGeneratingServer.stop();
    }

}
