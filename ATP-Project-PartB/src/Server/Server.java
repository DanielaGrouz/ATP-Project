package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private int listeningIntervalMS;
    private IServerStrategy strategy;
    private volatile boolean stop;
    private ExecutorService threadPool; // Thread pool
    private Thread mainThread;

    public Server(int port, int listeningIntervalMS, IServerStrategy strategy) {
        this.port = port;
        Configurations configurations = Configurations.getInstance();
        this.listeningIntervalMS = listeningIntervalMS;
        this.strategy = strategy;
        // initialize a new fixed thread pool with 2 threads:
        this.threadPool = Executors.newFixedThreadPool(Integer.parseInt(configurations.get("threadPoolSize")));
        this.mainThread = null;
    }

    public void start(){
        Thread thread = new Thread(()->{
            startLoop();
        });
        thread.start();
        this.mainThread = thread;
    }

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

    private void handleClient(Socket clientSocket) {
        try {
            strategy.applyStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
            System.out.println("Done handling client: " + clientSocket.toString());
            clientSocket.close();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void stop(){
        stop = true;
        threadPool.shutdownNow();
        if(mainThread != null && mainThread.isAlive()){
            mainThread.interrupt();
        }
        System.out.println("Stopping server...");
    }
}
