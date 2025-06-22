package Model;

import Client.IClientStrategy;
import Client.Client;
import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import IO.SimpleCompressorOutputStream;
import IO.SimpleDecompressorInputStream;
import Server.Server;
import Server.Configurations;
import Server.ServerStrategySolveSearchProblem;
import Server.ServerStrategyGenerateMaze;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.io.*;
import java.net.InetAddress;
import java.util.Observable;
import java.util.Observer;
import algorithms.search.Solution;


public class MyModel extends Observable implements IModel{
    private Maze maze;
    private int playerRow;
    private int playerCol;
    private Solution solution;
    private MyMazeGenerator generator;
    private int goalRow;
    private int goalCol;
    private boolean reachedGoal;
    Server solveSearchProblemServer; //the server that solves the maze
    Server mazeGeneratingServer; //the server that generate the maze

    public MyModel() {
        generator = new MyMazeGenerator();
        solveSearchProblemServer = new Server(4001, 1000, new ServerStrategySolveSearchProblem());
        solveSearchProblemServer.start();
        mazeGeneratingServer = new Server(4002, 1000, new ServerStrategyGenerateMaze());
        mazeGeneratingServer.start();

    }

//    @Override
//    public void generateMaze(int rows, int cols) {
//        maze = generator.generate(rows, cols);
//        goalRow=maze.getGoalPosition().getRowIndex();
//        goalCol=maze.getGoalPosition().getColumnIndex();
//        solution=null;
//        setChanged();
//        notifyObservers("maze generated");
//        //start position:
//        restartMaze();
//    }

    @Override
    public void generateMaze(int rows, int cols) {
//        maze = generator.generate(rows, cols);
//        goalRow=maze.getGoalPosition().getRowIndex();
//        goalCol=maze.getGoalPosition().getColumnIndex();

        reachedGoal = false;
        solution=null;
        setChanged();
        try {
            Client client = new Client(InetAddress.getLocalHost(), 4002, (inFromServer, outToServer) -> {
                try {
                    ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                    toServer.flush();
                    int[] mazeDimensions = new int[]{rows, cols};
                    toServer.writeObject(mazeDimensions);
                    toServer.flush();

                    ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                    byte[] compressedMaze = (byte[])fromServer.readObject();
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
                    byte[] decompressedMaze = new byte[rows*cols + 20];
                    is.read(decompressedMaze);
                    maze = new Maze(decompressedMaze);
                    goalRow=maze.getGoalPosition().getRowIndex();
                    goalCol=maze.getGoalPosition().getColumnIndex();
                    notifyObservers("maze generated");
                    //start position:
                    movePlayer(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());

                } catch (Exception e) {
                    notifyObservers("Error:" + e.getMessage());
                }
            });
            client.communicateWithServer();

        } catch (Exception e) {
            setChanged();
            notifyObservers("Error" + e.getMessage());
        }

    }


    @Override
    public void restartMaze(){
        movePlayer(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());
        reachedGoal=false;
        setChanged();
        notifyObservers("player moved");
    }

    @Override
    public Maze getMaze() {
        return maze;
    }

    @Override
    public boolean updatePlayerLocation(MovementDirection direction) {
        if (reachedGoal) {
            return false;
        }

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

        return false;
    }

    private void movePlayer(int row, int col){
        System.out.println("movePlayer called with: " + row + "," + col);
        this.playerRow = row;
        this.playerCol = col;
        setChanged();
        if (reachedGoal()) {
            System.out.println("Player reached the goal at: " + row + "," + col);
            reachedGoal = true;
            notifyObservers("goal reached");
        } else {
            notifyObservers("player moved");
        }
    }


    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    @Override
    public int getPlayerCol() {
        return playerCol;
    }

    @Override
    public int getGoalRow() {
        return goalRow;
    }

    @Override
    public int getGoalCol() {
        return goalCol;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void solveMaze() {
//        if (maze==null){
//            setChanged();
//            notifyObservers("Error: You must generate a maze before solving it.");
//            return;
//        }
        try {
            Client client = new Client(InetAddress.getLocalHost(), 4001, (inFromServer, outToServer) -> {
                try {
                    ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                    toServer.flush();
                    toServer.writeObject(maze);
                    toServer.flush();

                    ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                    solution = (Solution) fromServer.readObject();

                    setChanged();
                    notifyObservers("maze solved");
                } catch (Exception e) {
                    setChanged();
                    notifyObservers("Error:" + e.getMessage());
                }
            });

            client.communicateWithServer();

        } catch (Exception e) {
            setChanged();
            notifyObservers("Error" + e.getMessage());
        }
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    @Override
    public boolean reachedGoal(){
        if(playerRow == goalRow && playerCol == goalCol){
            return true;
        }
        return false;
    }

    @Override
    public void movePlayerTo(int row, int col) {
        if(row >= 0 && row < maze.getRows() && col >= 0 && col < maze.getColumns()) {
            if(maze.getMazeMatrix()[row][col] == 0) {
                movePlayer(row, col);
            }
        }
    }

    @Override
    public void exit(){
        solveSearchProblemServer.stop();
    }

    @Override
    public void saveMaze(File file) {
        if (maze == null) {
            setChanged();
            notifyObservers("Error: No maze to save. Please generate a maze first.");
            return;
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(maze);
            out.writeInt(playerRow);
            out.writeInt(playerCol);
        }
        catch (Exception e) {
            setChanged();
            notifyObservers("Error" + e.getMessage());
        }
    }

    @Override
    public void openMaze(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            maze = (Maze) in.readObject();
            playerRow = in.readInt();
            playerCol = in.readInt();
            movePlayer(playerRow, playerCol);
            goalRow=maze.getGoalPosition().getRowIndex();
            goalCol=maze.getGoalPosition().getColumnIndex();
            this.solution = null;
            this.reachedGoal = (goalRow==playerRow && goalCol==playerCol);
            setChanged();
            notifyObservers("maze loaded");
        } catch (Exception e) {
            setChanged();
            notifyObservers("Error: " + e.getMessage());
        }
    }


}
