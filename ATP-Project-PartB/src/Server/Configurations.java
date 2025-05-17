package Server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configurations {
    private Properties properties = new Properties();

    static Configurations instance = null;
    private Configurations(){
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream("resources/config.properties");
            if (input == null) {
                throw new RuntimeException("Unable to find configuration file");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Error reading configuration file", ex);
        }
    }

    static public Configurations getInstance(){
        if (instance == null){
            instance = new Configurations();
        }
        return instance;
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}
