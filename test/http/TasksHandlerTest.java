package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.adapter.DurationAdapter;
import http.adapter.LocalDateTimeAdapter;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TasksManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TasksHandlerTest {
    TasksManager tasksManager;
    HttpTaskServer httpTaskServer;
    Task task;
    Task task1;
    HttpClient client;

    @BeforeEach
    void beforeAll() {
        client = HttpClient.newHttpClient();
        tasksManager = Managers.getInMemoryTaskManager();
        httpTaskServer = new HttpTaskServer(tasksManager);
        httpTaskServer.start();

        task = tasksManager.createTask(new Task("Task with time 22", "time task 22"));
        task1 = tasksManager.createTask(new Task("Task 33", "task 33"));
    }

    @AfterEach
    void afterEach() {
        httpTaskServer.stop();
    }

    @Test
    void shouldBeCorrectTaskOnGET() {
        try {
            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String jsonTask = "[{\"id\":1,\"title\":\"Task with time 22\",\"description\":\"time task 22\"" +
                    ",\"status\":\"NEW\"},{\"id\":2,\"title\":\"Task 33\",\"description\":\"task 33\",\"status\":\"NEW\"}]";
            assertEquals(200, response.statusCode());
            assertEquals(jsonTask, response.body(), "bad JSON");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error");
        }
    }

    @Test
    void shouldReturnTaskOnGETWithID() {
        try {
            URI url = URI.create("http://localhost:8080/tasks/1");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String jTask = "{\"id\":1,\"title\":\"Task with time 22\",\"description\":\"time task 22\",\"status\":\"NEW\"}";
            assertEquals(200, response.statusCode());
            assertEquals(jTask, response.body(), "bad JSON");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error");
        }
    }

    @Test
    void shouldReturnCorrectTaskOnPOST() {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder
                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            String post = gson.toJson(task);

            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(post, StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String responseAns = "{\"id\":3,\"title\":\"Task with time 22\",\"description\":\"time task 22\",\"status\":\"NEW\"}";
            assertEquals(200, response.statusCode());
            assertEquals(responseAns, response.body(), "Bad Answer");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса возникла ошибка.");
        }
    }
}
