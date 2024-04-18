package http;

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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryHandlerTest {
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
        tasksManager.getTask(1);
        tasksManager.getTask(2);
    }

    @AfterEach
    void afterEach() {
        httpTaskServer.stop();
    }

    @Test
    void shouldBeCorrectHistoryOnGET() {
        try {
            URI url = URI.create("http://localhost:8080/history");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String history = "[{\"id\":1,\"title\":\"Task with time 22\",\"description\":\"time task 22\"" +
                    ",\"status\":\"NEW\"},{\"id\":2,\"title\":\"Task 33\",\"description\":\"task 33\",\"status\":\"NEW\"}]";
            assertEquals(200, response.statusCode());
            assertEquals(history, response.body(), "bad JSON");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error");
        }
    }
}
