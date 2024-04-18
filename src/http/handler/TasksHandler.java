package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exeption.EndpointException;
import exeption.NotFoundException;
import http.HttpTaskServer;
import model.Task;
import model.type.EndpointType;
import service.TasksManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TasksHandler implements HttpHandler {
    final TasksManager tasksManager;
    final Gson gson = HttpTaskServer.getGson();
    final ExceptionHandler exceptionHandler;

    public TasksHandler(TasksManager tasksManager, ExceptionHandler exceptionHandler) {
        this.tasksManager = tasksManager;
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
                switch (endpointType) {
                    case GET -> handleGet(exchange);
                    case GET_ID -> handleGetForID(exchange, requestPath);
                    case GET_ID_EPIC_SUB -> handleGetEpicSub(exchange, requestPath);
                    case POST_CREATE -> handlePostCreate(exchange);
                    case POST_UPDATE -> handlePostUpdate(exchange);
                    case DELETE_ID -> handleDelete(exchange, requestPath);
                    case DELETE_ALL -> handleDeleteAll(exchange);
                    default -> throw new EndpointException("Неизвестный эндпоинт");
                }
            } catch (Exception e) {
                exceptionHandler.exceptionHandle(exchange, e);
                e.printStackTrace();
            }
        }
    }

    void handleGet(HttpExchange exchange) throws IOException {
        List<Task> tasks = new ArrayList<>(tasksManager.getTasks().values());
        int rCode = 200;
        String response = gson.toJson(tasks);
        writeResponse(exchange, rCode, response);
    }

    void handleGetForID(HttpExchange exchange, String requestPath) throws IOException {
        String path = "/tasks/";
        int begin = path.length();
        String strId = requestPath.substring(begin);
        int id = Integer.parseInt(strId);
        Task task = tasksManager.getTask(id);
        if (task == null) {
            throw new NotFoundException("Not Found");
        }
        int rCode = 200;
        String response = gson.toJson(task);
        writeResponse(exchange, rCode, response);
    }

    void handleGetEpicSub(HttpExchange exchange, String requestPath) throws IOException {

    }

    void handlePostCreate(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            String requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(requestBody, Task.class);
            task = tasksManager.createTask(task);
            int rCode = 200;
            String response = gson.toJson(task);
            writeResponse(exchange, rCode, response);
        }
    }

    void handlePostUpdate(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            String requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(requestBody, Task.class);
            task = tasksManager.updateTask(task);
            int rCode = 200;
            String response = gson.toJson(task);
            writeResponse(exchange, rCode, response);
        }
    }

    void handleDelete(HttpExchange exchange, String requestPath) throws IOException {
        String strId = requestPath.substring(7);
        int id = Integer.parseInt(strId);
        tasksManager.removeTask(id);
        int rCode = 200;
        String response = "Задача удалена";
        writeResponse(exchange, rCode, response);
    }

    void handleDeleteAll(HttpExchange exchange) throws IOException {
        tasksManager.clearTasks();
        int rCode = 200;
        String response = "Задача удалена";
        writeResponse(exchange, rCode, response);
    }

    void writeResponse(HttpExchange exchange, int rCode, String response) throws IOException {
        try (OutputStream outputStream = exchange.getResponseBody()) {
            byte[] resp = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(rCode, 0);
            outputStream.write(resp);
        }
    }

    EndpointType getEndpoint(String requestPath, String method) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2 && method.equals("GET")) {
            return EndpointType.GET;
        }
        if (pathParts.length == 3 && method.equals("GET")) {
            return EndpointType.GET_ID;
        }
        if (pathParts.length == 4 && method.equals("GET")) {
            return EndpointType.GET_ID_EPIC_SUB;
        }
        if (pathParts.length == 2 && method.equals("POST")) {
            return EndpointType.POST_CREATE;
        }
        if (pathParts.length == 3 && method.equals("POST")) {
            return EndpointType.POST_UPDATE;
        }
        if (pathParts.length == 3 && method.equals("DELETE")) {
            return EndpointType.DELETE_ID;
        }
        if (pathParts.length == 2 && method.equals("DELETE")) {
            return EndpointType.DELETE_ALL;
        }
        return EndpointType.UNKNOWN;
    }
}
