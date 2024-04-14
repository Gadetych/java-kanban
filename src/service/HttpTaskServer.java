package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handler.ExceptionHandler;
import handler.TasksHandler;
import service.adapter.DurationAdapter;
import service.adapter.LocalDateTimeAdapter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

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

            server.createContext("/tasks", new TasksHandler(tasksManager, gson, exceptionHandler));
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
}
