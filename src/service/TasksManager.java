package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private int generateId() {
        return ++counter;
    }

    //Task
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
        if (task != null) {
            tasks.put(task.getId(), task);
        }
        return task;
    }

    public Task removeTask(Integer id) {
        return tasks.remove(id);
    }

    //    Epic
    public HashMap<Integer, Epic> getEpicTasks() {
        return epicTasks;
    }

    public List<Subtask> getEpicSubtasks(Integer id) {
        Epic epic = epicTasks.get(id);
        List<Subtask> subtaskList = null;
        if ( epic != null) {
            subtaskList = new ArrayList<>();
            for (Integer i : epic.getSubtasks()) {
                subtaskList.add(subtasks.get(i));
            }
        }
        return subtaskList;
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
        Epic originalEpicTask = null;
        if (epicTask != null) {
            originalEpicTask = epicTasks.get(epicTask.getId());
            originalEpicTask.setTitle(epicTask.getTitle());
            originalEpicTask.setDescription(epicTask.getDescription());
            originalEpicTask.setSubtasks(epicTask.getSubtasks());
            updateEpicStatus(originalEpicTask);
        }
        return originalEpicTask;
    }
    
    public Epic removeEpicTask(Integer id) {
        return epicTasks.remove(id);
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getSubtasks() == null) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        int counterNewStatus = 0;
        int counterDoneStatus = 0;
        for (Integer i : epic.getSubtasks()) {
            Subtask subtask = subtasks.get(i);
            if (subtask.getStatus() == TaskStatus.NEW) {
                ++counterNewStatus;
            } else if (subtask.getStatus() == TaskStatus.DONE) {
                ++counterDoneStatus;
            }
        }
        if (counterNewStatus == epic.getSubtasks().size()) {
            epic.setStatus(TaskStatus.NEW);
        } else if (counterDoneStatus == epic.getSubtasks().size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
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
        Epic epic = epicTasks.get(subtask.getIdEpic());
        epic.addSubtask(subtask.getId());
        updateEpicTask(epic);
        return subtask;
    }

    public Subtask updateSubtask(Subtask subtask) {
        if (subtask != null) {
            subtasks.put(subtask.getId(), subtask);
        }
        return subtask;
    }

    public Subtask removeSubtask(Integer id) {
        return subtasks.remove(id);
    }
}
