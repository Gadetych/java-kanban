package http.handler;

import com.sun.net.httpserver.HttpExchange;
import exeption.NotFoundException;
import model.Subtask;
import service.TasksManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SubtasksHandler extends TasksHandler {
    public SubtasksHandler(TasksManager tasksManager, ExceptionHandler exceptionHandler) {
        super(tasksManager, exceptionHandler);
    }

    @Override
    void handleGet(HttpExchange exchange) throws IOException {
        List<Subtask> subtasks = new ArrayList<>(tasksManager.getSubtasks().values());
        int rCode = 200;
        String response = gson.toJson(subtasks);
        writeResponse(exchange, rCode, response);
    }

    @Override
    void handleGetForID(HttpExchange exchange, String requestPath) throws IOException {
        String str = "/subtasks/";
        int begin = str.length();
        String strId = requestPath.substring(begin);
        int id = Integer.parseInt(strId);
        Subtask subtask = tasksManager.getSubtask(id);
        if (subtask == null) {
            throw new NotFoundException("Not Found");
        }
        int rCode = 200;
        String response = gson.toJson(subtask);
        writeResponse(exchange, rCode, response);
    }

    @Override
    void handlePostCreate(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            String requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtask = gson.fromJson(requestBody, Subtask.class);
            subtask = tasksManager.createSubtask(subtask);
            int rCode = 200;
            String response = gson.toJson(subtask);
            writeResponse(exchange, rCode, response);
        }
    }

    @Override
    void handlePostUpdate(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            String requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtask = gson.fromJson(requestBody, Subtask.class);
            subtask = tasksManager.updateSubtask(subtask);
            int rCode = 200;
            String response = gson.toJson(subtask);
            writeResponse(exchange, rCode, response);
        }
    }

    @Override
    void handleDelete(HttpExchange exchange, String requestPath) throws IOException {
        String str = "/subtasks/";
        int begin = str.length();
        String strId = requestPath.substring(begin);
        int id = Integer.parseInt(strId);
        tasksManager.removeSubtask(id);
        int rCode = 200;
        String response = "Подзадача удалена";
        writeResponse(exchange, rCode, response);
    }

    @Override
    void handleDeleteAll(HttpExchange exchange) throws IOException {
        tasksManager.clearSubtasks();
        int rCode = 200;
        String response = "Подзадачи удалены";
        writeResponse(exchange, rCode, response);
    }
}
