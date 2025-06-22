package Server;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * A singleton class that loads and provides access to configuration properties
 * from a file named "config.properties".
 */
public class Configurations {
    private Properties properties = new Properties();
    static private Configurations instance = null;

    /**
     * Private constructor that loads the configuration file.
     * Throws a runtime exception if the file is missing or unreadable.
     */
    private Configurations(){
        try {
            //InputStream input = Files.newInputStream(Paths.get("C:\\Users\\97254\\Documents\\config.properties"));
            InputStream input = Files.newInputStream(Paths.get("./ATP-Project-PartC/resources/config.properties"));
            //InputStream input = getClass().getClassLoader().getResourceAsStream("/config.properties");
            if (input == null) {
                throw new RuntimeException("Unable to find configuration file");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Error reading configuration file", ex);
        }
    }

    /**
     * Returns the single instance of Configurations (singleton pattern).
     *
     * @return the Configurations instance
     */
    static public Configurations getInstance(){
        if (instance == null){
            instance = new Configurations();
        }
        return instance;
    }

    /**
     * Gets the value for the given key from the configuration.
     *
     * @param key the name of the property
     * @return the value of the property, or null if not found
     */
    public String get(String key) {
        return properties.getProperty(key);
    }
}
