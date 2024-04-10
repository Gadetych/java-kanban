import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.Managers;
import service.TasksManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class Main {
    static Task task1;
    static Task task2;
    static Epic epic1;
    static TasksManager tasksManager;
    static Subtask subtask1;
    static Subtask subtask2;
    static Epic epic2;
    static Subtask subtask21;

    public static void main(String[] args) {
        task1 = new Task("Задача1", "описание1", LocalDateTime.of(2024, Month.AUGUST, 5, 14, 0),
                         Duration.ofMinutes(15));
        task2 = new Task("Задача2", "описание2", LocalDateTime.now(), Duration.ofMinutes(25));
        epic1 = new Epic("Эпик1", "описание1");
        tasksManager = Managers.getDefault();
        task1 = tasksManager.createTask(task1);
        task2 = tasksManager.createTask(task2);
        epic1 = tasksManager.createEpicTask(epic1);
        subtask1 = new Subtask("Подзадача1", "описание1", epic1.getId(), LocalDateTime.of(2024, Month.AUGUST, 7, 14, 0),
                               Duration.ofMinutes(60));
        subtask2 = new Subtask("Подзадача2", "описание2", epic1.getId(), LocalDateTime.of(2024, Month.AUGUST, 3, 14, 0),
                               Duration.ofMinutes(45));
        subtask1 = tasksManager.createSubtask(subtask1);
        subtask2 = tasksManager.createSubtask(subtask2);
        epic2 = new Epic("Эпик2", "описание2");
        epic2 = tasksManager.createEpicTask(epic2);
        subtask21 = new Subtask("Подзадача21", "описание21", epic2.getId(),
                                LocalDateTime.of(2024, Month.AUGUST, 10, 14, 0),
                                Duration.ofMinutes(25));
        System.out.println("Поехали!");
        System.out.println("Проверяем корректность созданных задач");
        subtask21 = tasksManager.createSubtask(subtask21);
        epic1.addSubtask(subtask1);
        epic1.addSubtask(subtask2);
        epic2.addSubtask(subtask21);
        Epic expected = tasksManager.getEpicTask(epic1.getId());
        System.out.println("Эпики до update равны: " + expected.equals(epic1));
        epic1 = tasksManager.updateEpicTask(epic1);
        tasksManager.updateEpicTask(epic2);
        System.out.println("Эпики после update равны: " + expected.equals(epic1));
        System.out.println();
        System.out.println(expected);
        System.out.println(epic1);
        System.out.println();

        print();

        System.out.println(
                "Изменяем статусы задач и подзадач. Проверяем корректность изменений. Проверяем изменения статуса эпиков.");
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task2.setStatus(TaskStatus.DONE);
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        subtask21.setStatus(TaskStatus.DONE);
        task1 = tasksManager.updateTask(task1);
        tasksManager.updateTask(task2);
        tasksManager.updateSubtask(subtask1);
        subtask2 = tasksManager.updateSubtask(subtask2);
        tasksManager.updateSubtask(subtask21);
        tasksManager.updateEpicTask(epic1);
        tasksManager.updateEpicTask(epic2);

        print();

        System.out.println("Удаляем задачу1, подзадачу2 и эпик2. Проверяем поля менеджера на удаленные объекты.");
        tasksManager.removeTask(task1.getId());
        tasksManager.removeSubtask(subtask2.getId());
        tasksManager.removeEpicTask(epic2.getId());

        print();
        System.out.println();
        System.out.println(tasksManager.getTask(task2.getId()));
    }

    public static void print() {
        System.out.println(tasksManager.getTasks());
        System.out.println(tasksManager.getEpicTasks());
        System.out.println(tasksManager.getSubtasks());
        System.out.println(tasksManager.getEpicSubtasks(epic1.getId()));
        System.out.println(tasksManager.getEpicSubtasks(epic2.getId()));
        System.out.println();
    }
}