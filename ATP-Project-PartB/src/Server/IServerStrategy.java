package Server;
import java.io.InputStream;
import java.io.OutputStream;

public interface IServerStrategy {
    void applyStrategy(InputStream inFromClient, OutputStream outToClient);
}
