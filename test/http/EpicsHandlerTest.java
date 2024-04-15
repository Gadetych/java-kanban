package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.adapter.DurationAdapter;
import http.adapter.LocalDateTimeAdapter;
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
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicsHandlerTest {
    TasksManager tasksManager;
    HttpTaskServer httpTaskServer;
    Epic epic;
    Subtask subTask1;
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
    }

    @AfterEach
    void afterEach() {
        httpTaskServer.stop();
    }

    @Test
    void shouldBeCorrectTaskOnGET() {
        try {
            URI url = URI.create("http://localhost:8080/epics");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String jsonTask = "[{\"subtasksId\":[2],\"endTime\":\"2024-08-14T13:25\",\"id\":1,\"title\":\"Test\"" +
                    ",\"description\":\"Test\",\"status\":\"NEW\",\"startTime\":\"2024-08-14T13:15\",\"duration\":\"PT10M\"}]";
            assertEquals(200, response.statusCode());
            assertEquals(jsonTask, response.body(), "bad JSON");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error");
        }
    }

    @Test
    void shouldReturnTaskOnGETWithID() {
        try {
            URI url = URI.create("http://localhost:8080/epics/1");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String jTask = "{\"subtasksId\":[2],\"endTime\":\"2024-08-14T13:25\",\"id\":1,\"title\":\"Test\"" +
                    ",\"description\":\"Test\",\"status\":\"NEW\",\"startTime\":\"2024-08-14T13:15\",\"duration\":\"PT10M\"}";
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
            String post = gson.toJson(new Epic("test2", "descr2"));

            URI url = URI.create("http://localhost:8080/epics");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(post, StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String responseAns = "{\"subtasksId\":[],\"id\":3,\"title\":\"test2\",\"description\":\"descr2\",\"status\":\"NEW\",\"duration\":\"PT0S\"}";
            assertEquals(200, response.statusCode());
            assertEquals(responseAns, response.body(), "Bad Answer");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса возникла ошибка.");
        }
    }
}

