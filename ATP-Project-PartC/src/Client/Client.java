package Client;

import Client.IClientStrategy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Connects to a server and runs a given strategy to talk to it.
 */
public class Client {
    private InetAddress serverIP;
    private int serverPort;
    private IClientStrategy strategy;

    /**
     * Creates a new client with server IP, port, and communication strategy.
     *
     * @param serverIP IP address of the server
     * @param serverPort Port number of the server
     * @param strategy How the client should talk to the server
     */
    public Client(InetAddress serverIP, int serverPort, IClientStrategy strategy) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.strategy = strategy;
    }

    /**
     * Connects to the server and runs the communication strategy.
     */
    public void communicateWithServer(){
        try(Socket serverSocket = new Socket(serverIP, serverPort)){
            System.out.println("connected to server - IP = " + serverIP + ", Port = " + serverPort);
            strategy.clientStrategy(serverSocket.getInputStream(), serverSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
