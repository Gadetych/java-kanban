import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.Managers;
import service.TasksManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Поехали!");
        System.out.println("Проверяем корректность созданных задач");
        Task task1 = new Task("Задача1", "описание1", "04.04.2024 10:00", 15);
        Task task2 = new Task("Задача2", "описание2", "04.04.2024 11:00", 25);
        Epic epic1 = new Epic("Эпик1", "описание1");
        TasksManager tasksManager = Managers.getDefault();
        task1 = tasksManager.createTask(task1);
        task2 = tasksManager.createTask(task2);
        epic1 = tasksManager.createEpicTask(epic1);
        Subtask subtask1 = new Subtask("Подзадача1", "описание1", epic1.getId()
                , "04.04.2024 09:00", 60);
        Subtask subtask2 = new Subtask("Подзадача2", "описание2", epic1.getId()
                , "04.05.2024 12:00", 45);
        subtask1 = tasksManager.createSubtask(subtask1);
        subtask2 = tasksManager.createSubtask(subtask2);
        Epic epic2 = new Epic("Эпик2", "описание2");
        epic2 = tasksManager.createEpicTask(epic2);
        Subtask subtask21 = new Subtask("Подзадача21", "описание21", epic2.getId()
                , "04.04.2024 10:00", 15);
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

        System.out.println(tasksManager.getTasks());
        System.out.println(tasksManager.getEpicTasks());
        System.out.println(tasksManager.getSubtasks());
        System.out.println(tasksManager.getEpicSubtasks(epic1.getId()));
        System.out.println(tasksManager.getEpicSubtasks(epic2.getId()));
        System.out.println();

        System.out.println("Изменяем статусы задач и подзадач. Проверяем корректность изменений. Проверяем изменения статуса эпиков.");
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

        System.out.println(tasksManager.getTasks());
        System.out.println(tasksManager.getEpicTasks());
        System.out.println(tasksManager.getSubtasks());
        System.out.println(tasksManager.getEpicSubtasks(epic1.getId()));
        System.out.println(tasksManager.getEpicSubtasks(epic2.getId()));
        System.out.println();

        System.out.println("Удаляем задачу1, подзадачу2 и эпик2. Проверяем поля менеджера на удаленные объекты.");
        tasksManager.removeTask(task1.getId());
        tasksManager.removeSubtask(subtask2.getId());
        tasksManager.removeEpicTask(epic2.getId());

        System.out.println(tasksManager.getTasks());
        System.out.println(tasksManager.getEpicTasks());
        System.out.println(tasksManager.getSubtasks());
        System.out.println();
        System.out.println(tasksManager.getTask(task2.getId()));
    }
}