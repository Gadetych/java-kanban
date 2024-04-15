package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exeption.EndpointException;
import model.Task;
import model.type.EndpointType;
import service.HistoryManager;
import service.TasksManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HistoryHandler implements HttpHandler {
    private final TasksManager tasksManager;
    private final Gson gson;
    public static final Charset CHARSET = StandardCharsets.UTF_8;
    private final ExceptionHandler exceptionHandler;

    public HistoryHandler(TasksManager tasksManager, Gson gson, ExceptionHandler exceptionHandler) {
        this.tasksManager = tasksManager;
        this.gson = gson;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (exchange) {
            try {
                URI url = exchange.getRequestURI();
                String requestPath = url.getPath();
                String method = exchange.getRequestMethod();

                EndpointType endpointType = getEndpoint(requestPath, method);
                if (endpointType == EndpointType.GET) {
                    handleGet(exchange);
                } else {
                    throw new EndpointException("Неизвестный эндпоинт");
                }
            } catch (Exception e) {
                exceptionHandler.exceptionHandle(exchange, e);
            }
        }

    }

    private void handleGet(HttpExchange exchange) throws IOException {
        HistoryManager historyManager = tasksManager.getHistoryManager();
        List<Task> history = historyManager.getHistory();
        int rCode = 200;
        String response = gson.toJson(history);
        writeResponse(exchange, rCode, response);
    }

    private void writeResponse(HttpExchange exchange, int rCode, String response) throws IOException {
        try (OutputStream outputStream = exchange.getResponseBody()) {
            byte[] resp = response.getBytes(CHARSET);
            exchange.sendResponseHeaders(rCode, 0);
            outputStream.write(resp);
        }
    }

    private EndpointType getEndpoint(String requestPath, String method) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2 && method.equals("GET")) {
            return EndpointType.GET;
        }
        return EndpointType.UNKNOWN;
    }
}
