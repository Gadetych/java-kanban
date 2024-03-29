package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTaskManagerTest {
    FileBackedTaskManager tasksManager;
    Path path;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    void createFileTaskManager() {
        try {
            path = Files.createTempFile("test", ".csv");
            tasksManager = new FileBackedTaskManager(new InMemoryHistoryManager(), path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        task = tasksManager.createTask(new Task("title", "description"));
        epic = tasksManager.createEpicTask(new Epic("e title", "e description"));
        subtask = tasksManager.createSubtask(new Subtask("s title", "s description", epic.getId()));
    }


    @Test
    void shouldHistoryFromString() {
        String history = "1,2,3,4,5,6";
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> actual = FileBackedTaskManager.historyFromString(history);
        assertEquals(expected, actual, "Списки не совпадают");
    }

    @Test
    void shouldHistoryToString() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("one", "one");
        Task task2 = new Task("two", "two");
        task1.setId(1);
        task2.setId(2);
        historyManager.add(task1);
        historyManager.add(task2);
        String actual = FileBackedTaskManager.historyToString(historyManager);
        String expected = "1,2";

        assertEquals(expected, actual, "Конвертация истории задач в строку не работает");
    }

    @Test
    void shouldTaskToString() {
        String actual = tasksManager.toString(task);
        String expected = "1,TASK,title,NEW,description,null\n";

        assertEquals(expected, actual, "Конвертация задачи в строку не работает");
    }

    @Test
    void shouldTaskFromString() {
        Task actual = tasksManager.fromString("1,TASK,title,NEW,description,null\n");
        Task expected = task;

        assertEquals(expected, actual, "Конвертация задачи из строки не работает");
    }

    @Test
    void shouldLoadFromFile() {
        tasksManager.getTask(task.getId());
        tasksManager.getEpicTask(epic.getId());
        tasksManager.getSubtask(subtask.getId());
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(path);

        assertEquals(fileBackedTaskManager.getTasks(), tasksManager.getTasks(), "Списки задач не восстановились");
        assertEquals(fileBackedTaskManager.getEpicTasks(), tasksManager.getEpicTasks(), "Списки эпиков не восстановились");
        assertEquals(fileBackedTaskManager.getSubtasks(), tasksManager.getSubtasks(), "Списки подзадач не восстановились");
        assertEquals(fileBackedTaskManager.getHistoryManager().getHistory(), tasksManager.getHistoryManager().getHistory(), "История не восстановились");
        assertTrue(fileBackedTaskManager.getCounter() >= tasksManager.getCounter(), "Счетчик id должен продолжаться");
    }
}
