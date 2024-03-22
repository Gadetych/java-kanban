package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InMemoryTaskManager implements TasksManager {
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Subtask> subtasks;
    private final Map<Integer, Epic> epicTasks;
    private int counter = 0;
    private final HistoryManager historyManager;


    public InMemoryTaskManager(HistoryManager historyManager) {
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epicTasks = new HashMap<>();
        this.historyManager = historyManager;
    }

    private int generateId() {
        return ++counter;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    //Task

    @Override
    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public Task getTask(Integer id) {
        Task task = tasks.get(id);
        historyManager.add(task);
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
        historyManager.remove(id);
        return tasks.remove(id);
    }

    //    Epic
    @Override
    public Map<Integer, Epic> getEpicTasks() {
        return epicTasks;
    }

    @Override
    public List<Subtask> getEpicSubtasks(Integer id) {
        Epic epic = epicTasks.get(id);
        List<Subtask> subtaskList = null;
        if (epic != null) {
            subtaskList = new ArrayList<>();
            for (Integer i : epic.getSubtasksId()) {
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
        historyManager.add(epic);
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
            originalEpicTask.setSubtasksId(epicTask.getSubtasksId());
            updateEpicStatus(originalEpicTask);
        }
        return originalEpicTask;
    }

    @Override
    public Epic removeEpicTask(Integer id) {
        Epic epic = getEpicTask(id);
        List<Integer> sub = epic.getSubtasksId();
        for (Integer i : sub) {
            subtasks.remove(i);
            historyManager.remove(i);
        }
        historyManager.remove(id);
        return epicTasks.remove(id);
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getSubtasksId() == null) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        int counterNewStatus = 0;
        int counterDoneStatus = 0;
        for (Integer i : epic.getSubtasksId()) {
            Subtask subtask = subtasks.get(i);
            if (subtask.getStatus() == TaskStatus.NEW) {
                ++counterNewStatus;
            } else if (subtask.getStatus() == TaskStatus.DONE) {
                ++counterDoneStatus;
            }
        }
        if (counterNewStatus == epic.getSubtasksId().size()) {
            epic.setStatus(TaskStatus.NEW);
        } else if (counterDoneStatus == epic.getSubtasksId().size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }


    //    Subtask
    @Override
    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public void clearSubtasks() {
        subtasks.clear();
    }

    @Override
    public Subtask getSubtask(Integer id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epicTasks.get(subtask.getIdEpic());
        epic.addSubtask(subtask);
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
        historyManager.remove(id);
        return subtasks.remove(id);
    }
}
