package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exeption.EndpointException;
import exeption.NotFoundException;
import exeption.ValidationException;

import java.io.IOException;
import java.io.OutputStream;

public class ExceptionHandler {
    private final Gson gson;

    public ExceptionHandler(Gson gson) {
        this.gson = gson;
    }

    public void exceptionHandle(HttpExchange exchange, Exception e) throws IOException {
        if (e instanceof EndpointException exception) {
            handle(exchange, exception);
            return;
        }
        if (e instanceof NotFoundException exception) {
            handle(exchange, exception);
            return;
        }
        if (e instanceof ValidationException exception) {
            handle(exchange, exception);
            return;
        }
        handle(exchange, e);

    }

    private void handle(HttpExchange exchange, EndpointException e) throws IOException {
        int rCode = 400;
        String response = e.getMessage();
        writeResponse(exchange, rCode, response);
    }

    private void handle(HttpExchange exchange, NotFoundException e) throws IOException {
        int rCode = 404;
        String response = e.getMessage();
        writeResponse(exchange, rCode, response);
    }

    private void handle(HttpExchange exchange, ValidationException e) throws IOException {
        int rCode = 406;
        String response = e.getMessage();
        writeResponse(exchange, rCode, response);
    }

    private void handle(HttpExchange exchange, Exception e) throws IOException {
        int rCode = 500;
        String response = e.getMessage();
        writeResponse(exchange, rCode, response);
    }

    private void writeResponse(HttpExchange exchange, int rCode, String response) throws IOException {
        try (OutputStream outputStream = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(rCode, 0);
            byte[] resp = response.getBytes(TasksHandler.CHARSET);
            outputStream.write(resp);
        }
    }

    public Gson getGson() {
        return gson;
    }
}
