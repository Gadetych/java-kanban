package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TasksManager tasksManager;

    @BeforeEach
    void createTaskManager() {
        tasksManager = Managers.getDefault();
    }


    // Tasks
    @Test
    void shouldTasksManagerSaved3Tasks() {
        Task task1 = tasksManager.createTask(new Task("title1", "description1"));
        Task task2 = tasksManager.createTask(new Task("title2", "description2"));
        Task task3 = tasksManager.createTask(new Task("title3", "description3"));
        Map<Integer, Task> actural = tasksManager.getTasks();
        Map<Integer, Task> expected = new HashMap<>();
        Task expectedTask1 = new Task("title1", "description1");
        Task expectedTask2 = new Task("title2", "description2");
        Task expectedTask3 = new Task("title3", "description3");
        expectedTask1.setId(task1.getId());
        expectedTask2.setId(task2.getId());
        expectedTask3.setId(task3.getId());
        expected.put(task1.getId(), expectedTask1);
        expected.put(task2.getId(), expectedTask2);
        expected.put(task3.getId(), expectedTask3);

        assertEquals(expected, actural, "Задачи не совпадают");
    }

    @Test
    void shouldTaskManagerIsEmpty() {
        tasksManager.createTask(new Task("title1", "description1"));
        tasksManager.createTask(new Task("title2", "description2"));
        tasksManager.createTask(new Task("title3", "description3"));
        tasksManager.clearTasks();

        assertTrue(tasksManager.getTasks().isEmpty());
    }

    @Test
    void shouldNewTaskEqualReturnedTask() {
        Task task = tasksManager.createTask(new Task("title", "description"));
        Task actual = tasksManager.getTask(task.getId());
        Task expected = new Task("title", "description");
        expected.setId(task.getId());

        assertEquals(expected, actual, "Задачи не совпадают");
    }

    @Test
    void shouldTasksManagerUpdateTask() {
        Task task = tasksManager.createTask(new Task("title", "description"));
        Task expected = new Task("expected title", "expected description");
        expected.setId(task.getId());
        Task actual = tasksManager.updateTask(expected);

        assertEquals(expected, actual, "Задача не обновилась");
    }

    @Test
    void shouldTaskManagerRemoveTaskById() {
        Task task = tasksManager.createTask(new Task("title", "description"));
        int id = task.getId();
        tasksManager.removeTask(id);
        Task itsNull = tasksManager.getTask(id);

        assertNull(itsNull);
    }

    //Epic


    @Test
    void testingTheInMemoryManagerBehaviorWhenAddingAndChangingEpics() {
        Epic epic = new Epic("title", "description");
        final int id = tasksManager.createEpicTask(epic).getId();
        epic.setId(id);
        Epic savedEpic = tasksManager.getEpicTask(id);

        //в итоге тут сравниваются просто ссылки. Вопрос: я тесты составляю неверные или реализация createTask() неверная?
        assertEquals(epic, savedEpic, "Задачи не совпадают");

        Map<Integer, Epic> epics = tasksManager.getEpicTasks();

        assertEquals(1, epics.keySet().size(), "Сохранилось неверное колличество задач");
        assertEquals(epic, epics.get(id), "Задачи не совпадают");

        epic.setTitle("expected title");
        epic.setDescription("expected description");
        epic = tasksManager.updateEpicTask(epic);

        assertEquals("expected title", epic.getTitle(), "Заголовки задач не совпадают");
        assertEquals("expected description", epic.getDescription(), "Описания задач не совпадают");

        Subtask subtask1 = new Subtask("S1 title", "S1 description", epic);
        Subtask subtask2 = new Subtask("S2 title", "S2 description", epic);
        Subtask subtask3 = new Subtask("S3 title", "S3 description", epic);
        subtask1 = tasksManager.createSubtask(subtask1);
        subtask2 = tasksManager.createSubtask(subtask2);
        subtask3 = tasksManager.createSubtask(subtask3);
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);
        epic.addSubtask(subtask3);
        epic = tasksManager.updateEpicTask(epic);

        assertEquals(TaskStatus.NEW, epic.getStatus(), "Статус эпика должен быть NEW");

        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        subtask3.setStatus(TaskStatus.DONE);
        epic = tasksManager.updateEpicTask(epic);

        assertEquals(TaskStatus.DONE, epic.getStatus(), "Статус эпика должен быть DONE");

        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask2.setStatus(TaskStatus.NEW);
        subtask3.setStatus(TaskStatus.DONE);
        epic = tasksManager.updateEpicTask(epic);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Статус эпика должен быть IN_PROGRESS");

        tasksManager.removeEpicTask(id);

        assertNull(tasksManager.getEpicTask(id), "Эпик не удалился");
        assertEquals(0, tasksManager.getEpicTasks().size(), "Подзадачи эпика не удалились");

    }

//    @Test
//    void testingTheInMemoryManagerBehaviorWhenAddingAndChangingTasks() {
//        Task task = new Task("title", "description");
//
//        final int id = tasksManager.createTask(task).getId();
//        task.setId(id);
//        Task savedTask = tasksManager.getTask(id);
//
//        assertEquals(task, savedTask, "Задачи не совпадают");
//
//        Map<Integer, Task> tasks = tasksManager.getTasks();
//
//        assertEquals(1, tasks.keySet().size(), "Сохранилось неверное колличество задач");
//        assertEquals(task, tasks.get(id), "Задачи не совпадают");
//
//        task.setStatus(TaskStatus.DONE);
//        task.setTitle("expected title");
//        task.setDescription("expected description");
//        tasksManager.updateTask(task);
//        savedTask = tasksManager.getTask(id);
//
//        assertEquals(task.getStatus(), savedTask.getStatus(), "Статусы не совпадают");
//        assertEquals(task.getTitle(), savedTask.getTitle(), "Заголовки задач не совпадают");
//        assertEquals(task.getDescription(), savedTask.getDescription(), "Описания задач не совпадают");
//
//        tasksManager.removeTask(id);
//
//        assertNull(tasksManager.getTask(id), "Задача не удалилась");

//    }


}