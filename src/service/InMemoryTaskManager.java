package service;

import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

class InMemoryTaskManager implements TasksManager {
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Subtask> subtasks;
    private final Map<Integer, Epic> epicTasks;
    private int counter = 0;
    private final HistoryManager historyManager;
    private final Set<Task> prioritizedTasks;


    public InMemoryTaskManager(HistoryManager historyManager) {
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epicTasks = new HashMap<>();
        this.historyManager = historyManager;
        this.prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    private int generateId() {
        return ++counter;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
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
        tasks.values().forEach(this::remove);
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
        add(task);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        if (task != null) {
            add(task);
            tasks.put(task.getId(), task);
        }
        return task;
    }

    @Override
    public Task removeTask(Integer id) {
        historyManager.remove(id);
        prioritizedTasks.remove(tasks.get(id));
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
        epicTasks.values().forEach(this::remove);
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
        add(epicTask);
        epicTasks.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    @Override
    public Epic updateEpicTask(Epic epicTask) {
        Epic originalEpicTask = null;
        if (epicTask != null) {
            add(epicTask);
            originalEpicTask = epicTasks.get(epicTask.getId());
            originalEpicTask.setTitle(epicTask.getTitle());
            originalEpicTask.setDescription(epicTask.getDescription());
            originalEpicTask.setSubtasksId(epicTask.getSubtasksId());
            updateEpicStatus(originalEpicTask);
            updateEpicTime(originalEpicTask);
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
        remove(epicTasks.get(id));
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

    private void updateEpicTime(Epic epic) {
        updateEpicStartTime(epic);
        updateEpicDuration(epic);
        updateEpicEndTime(epic);
    }

    private void updateEpicStartTime(Epic epic) {
        List<Integer> subtasksId = epic.getSubtasksId();
        if (!subtasksId.isEmpty()) {
            LocalDateTime startTime = subtasksId.stream()
                    .map(this::getSubtask)
                    .map(Subtask::getStartTime)
                    .min(LocalDateTime::compareTo)
                    .get();
            epic.setStartTime(startTime);
        }
    }

    private void updateEpicDuration(Epic epic) {
        Duration duration = epic.getSubtasksId().stream()
                .map(this::getSubtask)
                .map(Subtask::getDuration)
                .reduce(Duration.ofMinutes(0), Duration::plus);
        epic.setDuration(duration);

    }

    private void updateEpicEndTime(Epic epic) {
        List<Integer> subtasksId = epic.getSubtasksId();
        if (!subtasksId.isEmpty()) {
            LocalDateTime endTime = subtasksId.stream()
                    .map(this::getSubtask)
                    .map(Subtask::getEndTime)
                    .max(LocalDateTime::compareTo)
                    .get();
            epic.setEndTime(endTime);
        }
    }

    //    Subtask
    @Override
    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public void clearSubtasks() {
        subtasks.values().forEach(this::remove);
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
        add(subtask);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epicTasks.get(subtask.getIdEpic());
        epic.addSubtask(subtask);
        updateEpicTask(epic);
        return subtask;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        if (subtask != null) {
            add(subtask);
            subtasks.put(subtask.getId(), subtask);
        }
        return subtask;
    }

    @Override
    public Subtask removeSubtask(Integer id) {
        historyManager.remove(id);
        remove(subtasks.get(id));
        return subtasks.remove(id);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private void add(Task task) {
        if (task.getStartTime() == null) {
            return;
        }
        Task original;
        TaskType type = task.getType();
        switch (type) {
            case TASK -> original = tasks.get(task.getId());
            case EPIC -> original = epicTasks.get(task.getId());
            case SUBTASK -> original = subtasks.get(task.getId());
            default -> original = null;
        }
        if (original != null && !task.getStartTime().equals(original.getStartTime())) {
            prioritizedTasks.remove(original);
        }
        prioritizedTasks.add(task);
    }

    private void remove(Task task) {
        prioritizedTasks.remove(task);
    }
}
