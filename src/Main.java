import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.TasksManager;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Task task1 = new Task("Задача1", "описание1");
        Task task2 = new Task("Задача2", "описание2");
        Epic epic1 = new Epic("Эпик1", "описание1");
        Subtask subtask1 = new Subtask("Подзадача1","описание1", epic1);
        Subtask subtask2 = new Subtask("Подзадача2","описание2", epic1);
        TasksManager tasksManager = new TasksManager();
        task1 = tasksManager.createTask(task1);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        System.out.println();
    }
}
