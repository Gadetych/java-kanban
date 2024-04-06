package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;
import java.util.Map;

public interface TasksManager {
    //Task
    Map<Integer, Task> getTasks();

    HistoryManager getHistoryManager();

    void clearTasks();

    Task getTask(Integer id);

    Task createTask(Task task);

    Task updateTask(Task task);

    Task removeTask(Integer id);

    //    Epic
    Map<Integer, Epic> getEpicTasks();

    List<Subtask> getEpicSubtasks(Integer id);

    void clearEpicTasks();

    Epic getEpicTask(Integer id);

    Epic createEpicTask(Epic epicTask);

    Epic updateEpicTask(Epic epicTask);

    Epic removeEpicTask(Integer id);

    //    Subtask
    Map<Integer, Subtask> getSubtasks();

    void clearSubtasks();

    Subtask getSubtask(Integer id);

    Subtask createSubtask(Subtask subtask);

    Subtask updateSubtask(Subtask subtask);

    Subtask removeSubtask(Integer id);

    List<Task> getPrioritizedTasks();
}
