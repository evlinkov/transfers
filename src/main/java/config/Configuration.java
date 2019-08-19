package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

    private static Configuration configuration;

    public synchronized static Configuration getConfiguration() throws IOException {
        if (configuration == null) {
            configuration = initConfiguration();
        }
        return configuration;
    }

    private int port;

    public int getPort() {
        return port;
    }

    private Configuration() {

    }

    private static Configuration initConfiguration() throws IOException {
        Configuration configuration = new Configuration();
        Properties property = new Properties();

        FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties");
        property.load(fileInputStream);

        configuration.port = Integer.valueOf(property.getProperty("server.port"));

        return configuration;
    }

}
