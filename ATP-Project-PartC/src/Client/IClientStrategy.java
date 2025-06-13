package Client;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Defines how a client communicates with the server.
 */
public interface IClientStrategy {
    /**
     * Handles communication with the server using input and output streams.
     *
     * @param inFromServer input stream from the server
     * @param outToServer output stream to the server
     */
    void clientStrategy(InputStream inFromServer, OutputStream outToServer);
}
