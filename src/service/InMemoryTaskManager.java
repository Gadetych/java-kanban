package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class InMemoryTaskManager implements TasksManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subtasks;
    private HashMap<Integer, Epic> epicTasks;
    private int counter = 0;
    private HistoryManager historyManager;


    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epicTasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    private int generateId() {
        return ++counter;
    }

    //Task
    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public Task getTask(Integer id) {
        Task task = tasks.get(id);
        historyManager.addTaskInHistory(task);
        return task;
    }

    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        if (task != null) {
            tasks.put(task.getId(), task);
        }
        return task;
    }

    @Override
    public Task removeTask(Integer id) {
        return tasks.remove(id);
    }

    //    Epic
    @Override
    public HashMap<Integer, Epic> getEpicTasks() {
        return epicTasks;
    }

    @Override
    public List<Subtask> getEpicSubtasks(Integer id) {
        Epic epic = epicTasks.get(id);
        List<Subtask> subtaskList = null;
        if (epic != null) {
            subtaskList = new ArrayList<>();
            for (Integer i : epic.getSubtasks()) {
                subtaskList.add(subtasks.get(i));
            }
        }
        return subtaskList;
    }

    @Override
    public void clearEpicTasks() {
        clearSubtasks();
        epicTasks.clear();
    }

    @Override
    public Epic getEpicTask(Integer id) {
        Epic epic = epicTasks.get(id);
        historyManager.addTaskInHistory(epic);
        return epic;
    }

    @Override
    public Epic createEpicTask(Epic epicTask) {
        epicTask.setId(generateId());
        epicTasks.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    @Override
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

    @Override
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
    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public void clearSubtasks() {
        subtasks.clear();
    }

    @Override
    public Subtask getSubtask(Integer id) {
        Subtask subtask = subtasks.get(id);
        historyManager.addTaskInHistory(subtask);
        return subtask;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epicTasks.get(subtask.getIdEpic());
        epic.addSubtask(subtask.getId());
        updateEpicTask(epic);
        return subtask;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        if (subtask != null) {
            subtasks.put(subtask.getId(), subtask);
        }
        return subtask;
    }

    @Override
    public Subtask removeSubtask(Integer id) {
        return subtasks.remove(id);
    }
}
