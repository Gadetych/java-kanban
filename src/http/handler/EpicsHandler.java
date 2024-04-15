package http.handler;

import com.sun.net.httpserver.HttpExchange;
import exeption.NotFoundException;
import model.Epic;
import service.TasksManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EpicsHandler extends TasksHandler {
    public EpicsHandler(TasksManager tasksManager, ExceptionHandler exceptionHandler) {
        super(tasksManager, exceptionHandler);
    }

    @Override
    void handleGet(HttpExchange exchange) throws IOException {
        List<Epic> epics = new ArrayList<>(tasksManager.getEpicTasks().values());
        int rCode = 200;
        String response = gson.toJson(epics);
        writeResponse(exchange, rCode, response);
    }

    @Override
    void handleGetForID(HttpExchange exchange, String requestPath) throws IOException {
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

    @Override
    void handleGetEpicSub(HttpExchange exchange, String requestPath) throws IOException {
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

    @Override
    void handlePostCreate(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            String requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(requestBody, Epic.class);
            epic = tasksManager.createEpicTask(epic);
            int rCode = 200;
            String response = gson.toJson(epic);
            writeResponse(exchange, rCode, response);
        }
    }

    @Override
    void handleDelete(HttpExchange exchange, String requestPath) throws IOException {
        String str = "/epics/";
        int begin = str.length();
        String strId = requestPath.substring(begin);
        int id = Integer.parseInt(strId);
        tasksManager.removeEpicTask(id);
        int rCode = 200;
        String response = "Эпик удален";
        writeResponse(exchange, rCode, response);
    }

    @Override
    void handleDeleteAll(HttpExchange exchange) throws IOException {
        tasksManager.clearEpicTasks();
        int rCode = 200;
        String response = "Эпики удалены";
        writeResponse(exchange, rCode, response);
    }
}
