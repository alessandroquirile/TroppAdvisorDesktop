package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class ConfigFileReader {
    public static String getProperty(String key) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = ConfigFileReader.class.getResourceAsStream("/config.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }
}