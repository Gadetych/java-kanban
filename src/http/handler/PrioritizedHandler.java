package http.handler;

import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TasksManager;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends TasksHandler {
    public PrioritizedHandler(TasksManager tasksManager, ExceptionHandler exceptionHandler) {
        super(tasksManager, exceptionHandler);
    }

    @Override
    void handleGet(HttpExchange exchange) throws IOException {
        List<Task> prioritizedTasks = tasksManager.getPrioritizedTasks();
        int rCode = 200;
        String response = gson.toJson(prioritizedTasks);
        writeResponse(exchange, rCode, response);
    }
}
