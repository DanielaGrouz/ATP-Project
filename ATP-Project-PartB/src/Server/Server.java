package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple multi-threaded server that listens for client connections,
 * handles them using a specified server strategy, and uses a thread pool to manage requests.
 */
public class Server {
    private int port;
    private int listeningIntervalMS;
    private IServerStrategy strategy;
    private volatile boolean stop;
    private ExecutorService threadPool; // Thread pool
    private Thread mainThread;

    /**
     * Constructs a new Server with the given port, timeout, and strategy.
     *
     * @param port the port to listen on
     * @param listeningIntervalMS how often to check for a stop condition (in milliseconds)
     * @param strategy the strategy to handle each client
     */
    public Server(int port, int listeningIntervalMS, IServerStrategy strategy) {
        this.port = port;
        Configurations configurations = Configurations.getInstance();
        this.listeningIntervalMS = listeningIntervalMS;
        this.strategy = strategy;
        // initialize a new fixed thread pool with 2 threads:
        this.threadPool = Executors.newFixedThreadPool(Integer.parseInt(configurations.get("threadPoolSize")));
        this.mainThread = null;
    }

    /**
     * Starts the server in a new thread.
     */
    public void start(){
        Thread thread = new Thread(()->{
            startLoop();
        });
        thread.start();
        this.mainThread = thread;
    }

    /**
     * The main loop that accepts client connections and passes them to the thread pool.
     */
    private void startLoop(){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningIntervalMS);
            System.out.println("Starting server at port = " + port);

            while (!stop) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client accepted: " + clientSocket.toString());

                    // Insert the new task into the thread pool:
                    threadPool.submit(() -> {
                        handleClient(clientSocket);
                    });
                } catch (SocketTimeoutException e){

                }
            }
            serverSocket.close();
//            threadPool.shutdownNow(); // do not allow any new tasks into the thread pool, and also interrupts all running threads (do not terminate the threads, so if they do not handle interrupts properly, they could never stop...)
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Handles a single client using the configured strategy.
     *
     * @param clientSocket the client's socket
     */
    private void handleClient(Socket clientSocket) {
        try {
            strategy.applyStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
           // System.out.println("Done handling client: " + clientSocket.toString());
            clientSocket.close();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Stops the server and shuts down the thread pool.
     */
    public void stop(){
        stop = true;
        threadPool.shutdownNow();
        if(mainThread != null && mainThread.isAlive()){
            mainThread.interrupt();
        }
        System.out.println("Stopping server...");
    }
}
