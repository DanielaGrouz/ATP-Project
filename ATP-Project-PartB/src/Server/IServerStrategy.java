package Server;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An interface for defining different server strategies.
 * Each strategy handles communication with a client using input and output streams.
 */
public interface IServerStrategy {
    /**
     * Defines the behavior for handling client communication.
     *
     * @param inFromClient  the input stream from the client
     * @param outToClient  the output stream to the client
     * @throws IOException if an I/O error occurs during communication
     */
    void applyStrategy(InputStream inFromClient, OutputStream outToClient) throws IOException;
}
