package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import entities.TransferRequest;
import org.apache.commons.io.IOUtils;
import service.Bank;
import service.NotEnoughMoneyException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TransferHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        int httpCode = 200;
        String response = "";
        if (!requestMethod.equals("POST")) {
            httpCode = 405;
            response = "request method POST required";
        } else {
            Headers headers = exchange.getRequestHeaders();
            if (headers.get("user_id") == null || headers.get("user_id").size() != 1) {
                httpCode = 400;
                response = "not found user id";
            } else {
                Integer userIdentifierFrom = Integer.valueOf(headers.getFirst("user_id"));
                String request = IOUtils.toString(exchange.getRequestBody(), String.valueOf(StandardCharsets.UTF_8));
                try {
                    TransferRequest transferRequest = (new Gson()).fromJson(request, TransferRequest.class);
                    if (transferRequest == null) {
                        throw new JsonSyntaxException("incorrect json");
                    }
                    if (transferRequest.getUserIdentifier() == 0) {
                        httpCode = 400;
                        response = "not correct user to response amount";
                    } else {
                        if (userIdentifierFrom.equals(transferRequest.getUserIdentifier())) {
                            httpCode = 400;
                            response = "you cannot send money to yourself";
                        } else {
                            if (transferRequest.getAmount() <= 0) {
                                httpCode = 400;
                                response = "choose correct sum to send";
                            } else {
                                Bank.getBank().transfer(userIdentifierFrom, transferRequest.getUserIdentifier(), transferRequest.getAmount());
                                response = "transfer was success";
                            }
                        }
                    }
                } catch (JsonSyntaxException error) {
                    httpCode = 400;
                    response = "json syntax error : " + error.getMessage();
                } catch (NotEnoughMoneyException error) {
                    httpCode = 400;
                    response = "not enough money";
                }
            }
        }
        exchange.sendResponseHeaders(httpCode, response.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
        exchange.close();
    }

}
