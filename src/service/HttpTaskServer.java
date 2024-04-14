package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handler.*;
import model.Epic;
import model.Subtask;
import service.adapter.DurationAdapter;
import service.adapter.LocalDateTimeAdapter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class HttpTaskServer {
    private final HttpServer server;
    public static final int PORT = 8080;
    private final TasksManager tasksManager;
    private final Gson gson;
    private final ExceptionHandler exceptionHandler;

    public HttpTaskServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            tasksManager = Managers.getDefault();
            gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                    .create();
            exceptionHandler = new ExceptionHandler(gson);

            Epic epic = new Epic("title", "description");
            tasksManager.createEpicTask(epic);
            Subtask subtask = new Subtask("title", "description", epic.getId(),
                                          LocalDateTime.of(2024, Month.APRIL, 4, 11, 0),
                                          Duration.ofMinutes(20));
            tasksManager.createSubtask(subtask);

            server.createContext("/tasks", new TasksHandler(tasksManager, gson, exceptionHandler));
            server.createContext("/epics", new EpicsHandler(tasksManager, gson, exceptionHandler));
            server.createContext("/subtasks", new SubtasksHandler(tasksManager, gson, exceptionHandler));
            server.createContext("/history", new HistoryHandler(tasksManager, gson, exceptionHandler));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        System.out.println("Сервер запущен");
        server.start();
    }

    public void stop() {
        System.out.println("Сервер остановлен");
        server.stop(2);
    }

    public HttpServer getServer() {
        return server;
    }

    public TasksManager getTasksManager() {
        return tasksManager;
    }

    public Gson getGson() {
        return gson;
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }
}
