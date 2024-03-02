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

    List<Task> getArrayList() {
        List<Task> list = new ArrayList<>();
        Task task = new Task("title", "description");
        task.setId(1);
        Epic epic = new Epic("title", "description");
        epic.setId(2);
        Subtask subtask = new Subtask("title", "description", epic);
        subtask.setId(3);
        epic.addSubtask(subtask);
        list.add(task);
        list.add(epic);
        list.add(subtask);
        return list;
    }

    void addTasksInHitoryManager(List<Task> expected) {
        historyManager.add(expected.get(0));
        historyManager.add(expected.get(1));
        historyManager.add(expected.get(2));
    }

    @Test
    void shouldDeleteFirstAndAddTheTaskToTheEndOfTheHistory() {
        List<Task> expected = getArrayList();
        addTasksInHitoryManager(expected);
        Task task = expected.get(0);
        historyManager.add(task);
        expected.add(task);
        expected.remove(0);
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual, "Не удалилась первая задача из истории или не добавилась в конец истории");
    }

    @Test
    void shouldDeleteLastAndAddTheTaskToTheEndOfTheHistory() {
        List<Task> expected = getArrayList();
        addTasksInHitoryManager(expected);
        Task subtask = expected.get(2);
        historyManager.add(subtask);
        expected.add(subtask);
        expected.remove(2);
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual, "Не удалилась последняя задача из истории или не добавилась в конец истории");
    }

    @Test
    void shouldDeleteEpicFromTheMiddleAndAddTheEpicToTheEndOfTheHistory() {
        List<Task> expected = getArrayList();
        addTasksInHitoryManager(expected);
        Task epic = expected.get(1);
        historyManager.add(epic);
        expected.add(epic);
        expected.remove(1);
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual, "Не удалился Эпик из середины истории или не добавился в конец истории");
    }
}