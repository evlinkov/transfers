import com.sun.net.httpserver.HttpServer;
import config.Configuration;

import java.net.InetSocketAddress;

public class ApplicationServer {

    public static void main(String[] args) {
        try {
            Configuration configuration = Configuration.getConfiguration();
            HttpServer httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(configuration.getPort()), 0);
            httpServer.start();
        } catch (java.io.IOException error) {
            System.out.printf("cannot create http server, error : %s\n", error.getMessage());
        }
    }
}
