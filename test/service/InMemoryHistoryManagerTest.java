package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager;


    @BeforeEach
    void createHistoryManager() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void shouldHistoryManagerContain1Task() {
        Task task = new Task("title", "description");
        task.setId(1);
        historyManager.add(task);
        List<Task> actual = historyManager.getHistory();
        List<Task> expected = new ArrayList<>();
        expected.add(task);
        assertEquals(expected, actual, "Менеджер историй должен содержать только одну задачу");
    }

    @Test
    void shouldHistoryManagerContain3Task() {
        Task task = new Task("title", "description");
        task.setId(1);
        Epic epic = new Epic("title", "description");
        epic.setId(2);
        Subtask subtask = new Subtask("title", "description", epic);
        subtask.setId(3);
        epic.addSubtask(subtask);
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        List<Task> actual = historyManager.getHistory();
        List<Task> expected = new ArrayList<>();
        expected.add(task);
        expected.add(epic);
        expected.add(subtask);
        assertEquals(expected, actual, "Менеджер историй должен содержать только 3 задачи");
    }

    @Test
    void shouldHistoryManagerContain10TaskAndChange1stTask() {
        Task task = new Task("title", "description");
        task.setId(1);
        Epic epic = new Epic("title", "description");
        epic.setId(2);
        Subtask subtask = new Subtask("title", "description", epic);
        subtask.setId(3);
        epic.addSubtask(subtask);
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.add(epic);
        historyManager.add(epic);
        historyManager.add(task);
        historyManager.add(subtask);
        historyManager.add(subtask);
        historyManager.add(subtask);
        historyManager.add(subtask);
        historyManager.add(subtask);
        List<Task> acrual = historyManager.getHistory();
        List<Task> expected = new ArrayList<>();
        expected.add(epic);
        expected.add(subtask);
        expected.add(epic);
        expected.add(epic);
        expected.add(task);
        expected.add(subtask);
        expected.add(subtask);
        expected.add(subtask);
        expected.add(subtask);
        expected.add(subtask);
        assertEquals(expected, acrual, "Менеджер историй должен содержать только 10 задач\n" +
                "При добавлении 11й должен удалить самую раннюю задачу");
    }
}