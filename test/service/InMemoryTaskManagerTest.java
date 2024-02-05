package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TasksManager tasksManager;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    void createTaskManager() {
        tasksManager = Managers.getDefault();
        task = tasksManager.createTask(new Task("title", "description"));
        epic = tasksManager.createEpicTask(new Epic("e title", "e description"));
        subtask = tasksManager.createSubtask(new Subtask("s title", "s description", epic));
    }


    // Tasks
    @Test
    void shouldTasksManagerSaved1Task() {
        Map<Integer, Task> actual = tasksManager.getTasks();
        Map<Integer, Task> expected = new HashMap<>();
        Task expectedTask = new Task("title", "description");
        int id = task.getId();
        expectedTask.setId(id);
        expected.put(id, expectedTask);

        assertEquals(expected, actual, "Списки задач не совпадают");
    }

    @Test
    void shouldTaskManagerTasksIsEmpty() {
        tasksManager.clearTasks();

        assertTrue(tasksManager.getTasks().isEmpty());
    }

    @Test
    void shouldNewTaskEqualReturnedTask() {
        int id = task.getId();
        Task actual = tasksManager.getTask(id);
        Task expected = new Task("title", "description");
        expected.setId(id);

        assertEquals(expected, actual, "Задачи не совпадают");
    }

    @Test
    void shouldTasksManagerUpdateTask() {
        int id = task.getId();
        Task expected = new Task("expected title", "expected description");
        expected.setId(id);
        Task actual = tasksManager.updateTask(expected);

        assertEquals(expected, actual, "Задача не обновилась");
    }

    @Test
    void shouldTaskManagerRemoveTaskById() {
        int id = task.getId();
        tasksManager.removeTask(id);
        Task itsNull = tasksManager.getTask(id);

        assertNull(itsNull, "Задача не удалена");
    }


    //Epic
    @Test
    void shouldTasksManagerSaved1Epic() {
        Map<Integer, Epic> actual = tasksManager.getEpicTasks();
        Map<Integer, Epic> expected = new HashMap<>();
        Epic expectedEpic = new Epic("e title", "e description");
        int id = epic.getId();
        expectedEpic.setId(id);
        List<Integer> subtasksId = new ArrayList<>();
        subtasksId.add(subtask.getId());
        expectedEpic.setSubtasksId(subtasksId);
        expected.put(id, expectedEpic);

        assertEquals(expected, actual, "Списки эпиков не совпадают");
    }

    @Test
    void shouldTaskManagerEpicTasksIsEmpty() {
        tasksManager.clearEpicTasks();

        assertTrue(tasksManager.getEpicTasks().isEmpty());
    }

    @Test
    void shouldNewEpicEqualReturnedEpic() {
        int id = epic.getId();
        Epic actual = tasksManager.getEpicTask(id);
        Epic expected = new Epic("e title", "e description");
        expected.setId(id);
        List<Integer> subtasksId = new ArrayList<>();
        subtasksId.add(subtask.getId());
        expected.setSubtasksId(subtasksId);

        assertEquals(expected, actual, "Эпики не совпадают");
    }

    @Test
    void shouldTasksManagerUpdateEpic() {
        int id = epic.getId();
        Epic expected = new Epic("expected title", "expected description");
        expected.setId(id);
        List<Integer> subtasksId = new ArrayList<>();
        subtasksId.add(subtask.getId());
        expected.setSubtasksId(subtasksId);
        expected.setStatus(TaskStatus.DONE);
        subtask.setStatus(TaskStatus.DONE);
        Task actual = tasksManager.updateTask(expected);

        assertEquals(expected, actual, "Эпик не обновилася");
    }

    @Test
    void shouldTaskManagerRemoveEpicById() {
        int id = epic.getId();
        tasksManager.removeEpicTask(id);
        Epic itsNull = tasksManager.getEpicTask(id);
        Map<Integer, Subtask> subtasks = tasksManager.getSubtasks();

        assertNull(itsNull, "Эпик не удален");
        assertEquals(0, subtasks.size(), "Список подзадач должен быть пуст");
    }

    //Subtasks
    Subtask createSubtask() {
        Epic expectedEpic = new Epic("e title", "e description");
        int idE = epic.getId();
        expectedEpic.setId(idE);
        Subtask expectedTask = new Subtask("s title", "s description", expectedEpic);
        int id = subtask.getId();
        expectedTask.setId(id);
        return expectedTask;
    }
    @Test
    void shouldTasksManagerSaved1SubTask() {
        Map<Integer, Subtask> actual = tasksManager.getSubtasks();
        Map<Integer, Subtask> expected = new HashMap<>();
        Subtask expectedTask = createSubtask();
        expected.put(expectedTask.getId(), expectedTask);

        assertEquals(expected, actual, "Списки задач не совпадают");
    }

    @Test
    void shouldTaskManagerSebTasksIsEmpty() {
        tasksManager.clearSubtasks();

        assertTrue(tasksManager.getSubtasks().isEmpty());
    }

    @Test
    void shouldNewSubTaskEqualReturnedSubTask() {
        int id = subtask.getId();
        Subtask actual = tasksManager.getSubtask(id);
        Subtask expected = createSubtask();

        assertEquals(expected, actual, "Задачи не совпадают");
    }

    @Test
    void shouldTasksManagerUpdateSubTask() {
        Subtask expected = createSubtask();
        expected.setStatus(TaskStatus.DONE);
        expected.setTitle("good");
        expected.setDescription("good");
        Subtask actual = tasksManager.updateSubtask(expected);

        assertEquals(expected, actual, "Задача не обновилась");
    }

    @Test
    void shouldTaskManagerRemoveSubTaskById() {
        int id = subtask.getId();
        tasksManager.removeSubtask(id);
        Subtask itsNull = tasksManager.getSubtask(id);

        assertNull(itsNull, "Подзадача не удалена");
    }



}