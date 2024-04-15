package http.handler;

import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.HistoryManager;
import service.TasksManager;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends TasksHandler {
    public HistoryHandler(TasksManager tasksManager, ExceptionHandler exceptionHandler) {
        super(tasksManager, exceptionHandler);
    }

    @Override
    void handleGet(HttpExchange exchange) throws IOException {
        HistoryManager historyManager = tasksManager.getHistoryManager();
        List<Task> history = historyManager.getHistory();
        int rCode = 200;
        String response = gson.toJson(history);
        writeResponse(exchange, rCode, response);
    }
}
