package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.Bank;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class GetBalance implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        int httpCode = 200;
        String response = "";
        if (!requestMethod.equals("GET")) {
            httpCode = 405;
            response = "request method GET required";
        } else {
            Headers headers = exchange.getRequestHeaders();
            if (headers.get("user_id") == null || headers.get("user_id").size() != 1) {
                httpCode = 400;
                response = "not found user id";
            } else {
                Integer userIdentifier = Integer.valueOf(headers.getFirst("user_id"));
                response = Bank.getBank().getAmount(userIdentifier).toString();
            }
        }
        exchange.sendResponseHeaders(httpCode, response.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
        exchange.close();
    }
}
