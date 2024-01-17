package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.HashMap;

public class TasksManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subtasks;
    private HashMap<Integer, Epic> epicTasks;
    private int counter = 0;


    public TasksManager() {
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epicTasks = new HashMap<>();
    }

    public int generateId() {
        return ++counter;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public void clearTasks() {
        tasks.clear();
    }

    public Task getTask(Integer id) {
        return tasks.get(id);
    }

    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Task updateTask(Task task) {
        Task originalTask = tasks.get(task.getId());
        originalTask.setTitle(task.getTitle());
        originalTask.setDescription(task.getDescription());
        originalTask.setStatus(task.getStatus());
        return originalTask;
    }

    public Task removeTask(Integer id) {
        return tasks.remove(id);
    }

    //    Epic

    public HashMap<Integer, Epic> getEpicTasks() {
        return epicTasks;
    }

    public HashMap<Integer, Subtask> getEpicSubtasks(Integer id) {
        return epicTasks.get(id).getSubtasks();
    }

    public void clearEpicTasks() {
        clearSubtasks();
        epicTasks.clear();
    }

    public Epic getEpicTask(Integer id) {
        return epicTasks.get(id);
    }

    public Epic createEpicTask(Epic epicTask) {
        epicTask.setId(generateId());
        epicTasks.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    public Epic updateEpicTask(Epic epicTask) {
        Epic originalEpicTask = epicTasks.get(epicTask.getId());
        originalEpicTask.setTitle(epicTask.getTitle());
        originalEpicTask.setDescription(epicTask.getDescription());
        originalEpicTask.setSubtasks(epicTask.getSubtasks());
        originalEpicTask.updateStatus();
        return originalEpicTask;
    }
    
    public Epic removeEpicTask(Integer id) {
        return epicTasks.remove(id);
    }


    //    Subtask
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void clearSubtasks() {
        subtasks.clear();
    }

    public Subtask getSubtask(Integer id) {
        return subtasks.get(id);
    }

    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = subtask.getEpic();
        epic.addSubtask(subtask.getId(), subtask);
        updateEpicTask(epic);
        return subtask;
    }

    public Subtask updateSubtask(Subtask subtask) {
        Subtask originalSubtask = subtasks.get(subtask.getId());
        originalSubtask.setTitle(subtask.getTitle());
        originalSubtask.setDescription(subtask.getDescription());
        originalSubtask.setStatus(subtask.getStatus());
        return originalSubtask;
    }

    public Subtask removeSubtask(Integer id) {
        return subtasks.remove(id);
    }
}
