package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exeption.EndpointException;
import exeption.NotFoundException;
import model.Epic;
import model.type.EndpointType;
import service.TasksManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EpicsHandler implements HttpHandler {
    private final TasksManager tasksManager;
    private final Gson gson;
    public static final Charset CHARSET = StandardCharsets.UTF_8;
    private final ExceptionHandler exceptionHandler;

    public EpicsHandler(TasksManager tasksManager, Gson gson, ExceptionHandler exceptionHandler) {
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
                switch (endpointType) {
                    case GET -> handleGetEpics(exchange);
                    case GET_ID -> handleGetEpic(exchange, requestPath);
                    case GET_ID_EPIC_SUB -> handleGetEpicSub(exchange, requestPath);
                    case POST_CREATE -> handlePostCreate(exchange);
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

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = new ArrayList<>(tasksManager.getEpicTasks().values());
        int rCode = 200;
        String response = gson.toJson(epics);
        writeResponse(exchange, rCode, response);
    }

    private void handleGetEpic(HttpExchange exchange, String requestPath) throws IOException {
        String str = "/epics/";
        int begin = str.length();
        String strId = requestPath.substring(begin);
        int id = Integer.parseInt(strId);
        Epic epic = tasksManager.getEpicTask(id);
        if (epic == null) {
            throw new NotFoundException("Not Found");
        }
        int rCode = 200;
        String response = gson.toJson(epic);
        writeResponse(exchange, rCode, response);
    }

    private void handleGetEpicSub(HttpExchange exchange, String requestPath) throws IOException {
        String str = "/epics/";
        int begin = str.length();
        int end = requestPath.indexOf("/subtasks");
        String strId = requestPath.substring(begin, end);
        int id = Integer.parseInt(strId);
        Epic epic = tasksManager.getEpicTask(id);
        if (epic == null) {
            throw new NotFoundException("Not Found");
        }
        List<Integer> idSubtasks = epic.getSubtasksId();
        int rCode = 200;
        String response = gson.toJson(idSubtasks);
        writeResponse(exchange, rCode, response);
    }

    private void handlePostCreate(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            String requestBody = new String(inputStream.readAllBytes(), CHARSET);
            Epic epic = gson.fromJson(requestBody, Epic.class);
            epic = tasksManager.createEpicTask(epic);
            int rCode = 200;
            String response = gson.toJson(epic);
            writeResponse(exchange, rCode, response);
        }
    }

    private void handleDelete(HttpExchange exchange, String requestPath) throws IOException {
        String str = "/epics/";
        int begin = str.length();
        String strId = requestPath.substring(begin);
        int id = Integer.parseInt(strId);
        tasksManager.removeEpicTask(id);
        int rCode = 200;
        String response = "Эпик удален";
        writeResponse(exchange, rCode, response);
    }

    private void handleDeleteAll(HttpExchange exchange) throws IOException {
        tasksManager.clearEpicTasks();
        int rCode = 200;
        String response = "Эпики удалены";
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
        if (pathParts.length == 3 && method.equals("GET")) {
            return EndpointType.GET_ID;
        }
        if (pathParts.length == 4 && method.equals("GET")) {
            return EndpointType.GET_ID_EPIC_SUB;
        }
        if (pathParts.length == 2 && method.equals("POST")) {
            return EndpointType.POST_CREATE;
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
