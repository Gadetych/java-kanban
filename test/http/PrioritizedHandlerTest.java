package http;

import model.Epic;
import model.Subtask;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrioritizedHandlerTest {
    TasksManager tasksManager;
    HttpTaskServer httpTaskServer;
    Epic epic;
    Subtask subTask1;
    Subtask subTask2;
    HttpClient client;

    @BeforeEach
    void beforeAll() {
        client = HttpClient.newHttpClient();
        tasksManager = Managers.getInMemoryTaskManager();
        httpTaskServer = new HttpTaskServer(tasksManager);
        httpTaskServer.start();

        epic = tasksManager.createEpicTask(new Epic("Test", "Test"));
        subTask1 = tasksManager.createSubtask(
                new Subtask("Subtask", "Subtask", epic.getId(), LocalDateTime.of(2024, Month.AUGUST, 14, 13, 15),
                            Duration.ofMinutes(10)));
        subTask2 = tasksManager.createSubtask(new Subtask("Subtask", "Subtask", epic.getId(),
                                                          LocalDateTime.of(2024, Month.AUGUST, 15, 13, 15),
                                                          Duration.ofMinutes(10)));
    }

    @AfterEach
    void afterEach() {
        httpTaskServer.stop();
    }

    @Test
    void shouldBeCorrectPrioritizedTasksOnGET() {
        try {
            URI url = URI.create("http://localhost:8080/prioritized");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonTask = "[{\"idEpic\":1,\"id\":2,\"title\":\"Subtask\",\"description\":\"Subtask\",\"status\":\"NEW\"" +
                    ",\"startTime\":\"2024-08-14T13:15\",\"duration\":\"PT10M\"},{\"idEpic\":1,\"id\":3,\"title\":\"Subtask\"" +
                    ",\"description\":\"Subtask\",\"status\":\"NEW\",\"startTime\":\"2024-08-15T13:15\",\"duration\":\"PT10M\"}]";
            assertEquals(200, response.statusCode());
            assertEquals(jsonTask, response.body(), "bad JSON");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error");
        }
    }
}
