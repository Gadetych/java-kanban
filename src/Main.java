import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.HistoryManager;
import service.Managers;
import service.TasksManager;

public class Main {

    public static void main(String[] args) {
//        не могу сообразить какие методы тут вяделять
        System.out.println("Поехали!");
        System.out.println("Проверяем корректность созданных задач");
        Task task1 = new Task("Задача1", "описание1");
        Task task2 = new Task("Задача2", "описание2");
        Epic epic1 = new Epic("Эпик1", "описание1");
        Subtask subtask1 = new Subtask("Подзадача1", "описание1", epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача2", "описание2", epic1.getId());
        Epic epic2 = new Epic("Эпик2", "описание2");
        Subtask subtask2_1 = new Subtask("Подзадача21", "описание21", epic2.getId());
        TasksManager tasksManager = Managers.getDefault();
        task1 = tasksManager.createTask(task1);
        task2 = tasksManager.createTask(task2);
        epic1 = tasksManager.createEpicTask(epic1);
        subtask1 = tasksManager.createSubtask(subtask1);
        subtask2 = tasksManager.createSubtask(subtask2);
        epic2 = tasksManager.createEpicTask(epic2);
        subtask2_1 = tasksManager.createSubtask(subtask2_1);
        epic1.addSubtask(subtask1.getId());
        epic1.addSubtask(subtask2.getId());
        epic2.addSubtask(subtask2_1.getId());
        tasksManager.updateEpicTask(epic1);
        tasksManager.updateEpicTask(epic2);

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
        subtask2_1.setStatus(TaskStatus.DONE);
        task1 = tasksManager.updateTask(task1);
        tasksManager.updateTask(task2);
        tasksManager.updateSubtask(subtask1);
        subtask2 = tasksManager.updateSubtask(subtask2);
        tasksManager.updateSubtask(subtask2_1);
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
    }
}
