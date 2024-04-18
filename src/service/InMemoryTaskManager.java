package service;

import exeption.ValidationException;
import model.Epic;
import model.Subtask;
import model.Task;
import model.type.TaskStatus;
import model.type.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

class InMemoryTaskManager implements TasksManager {
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Subtask> subtasks;
    private final Map<Integer, Epic> epicTasks;
    private final HistoryManager historyManager;
    private final Set<Task> prioritizedTasks;
    private int counter = 0;


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
        tasks.values().forEach(this::removePrioritizedTask);
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
        if (isValid(task)) {
            task.setId(generateId());
            addPrioritizedTasks(task);
            tasks.put(task.getId(), task);
        } else {
            throw new ValidationException("Задача пересекается по времени с уже существующей");
        }
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        if (isValid(task)) {
            addPrioritizedTasks(task);
            tasks.put(task.getId(), task);
        } else {
            throw new ValidationException("Задача пересекается по времени с уже существующей");
        }
        return task;
    }

    @Override
    public Task removeTask(Integer id) {
        historyManager.remove(id);
        removePrioritizedTask(tasks.get(id));
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
        epicTasks.values().forEach(this::removePrioritizedTask);
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
        if (isValid(epicTask)) {
            epicTask.setId(generateId());
            addPrioritizedTasks(epicTask);
            epicTasks.put(epicTask.getId(), epicTask);
            return epicTask;
        } else {
            throw new ValidationException("Задача пересекается по времени с уже существующей");
        }
    }

    @Override
    public Epic removeEpicTask(Integer id) {
        Epic epic = getEpicTask(id);
        List<Integer> sub = epic.getSubtasksId();
        for (Integer i : sub) {
            removePrioritizedTask(subtasks.get(i));
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

    @Override
    public Epic updateEpicTask(Epic epicTask) {
        if (epicTask != null) {
            epicTasks.put(epicTask.getId(), epicTask);
        } else {
            throw new ValidationException("Задача равна null");
        }
        return epicTask;
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
        subtasks.values().forEach(this::removePrioritizedTask);
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
        if (isValid(subtask)) {
            Epic epic = epicTasks.get(subtask.getIdEpic());
            if (epic == null) {
                throw new ValidationException("Подзадача не привязана к эпику");
            }
            subtask.setId(generateId());
            subtasks.put(subtask.getId(), subtask);
            addPrioritizedTasks(subtask);
            epic.addSubtask(subtask);
            updateEpicStatus(epic);
            updateEpicTime(epic);
            return subtask;
        } else {
            throw new ValidationException("Задача пересекается по времени с уже существующей");
        }
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        if (isValid(subtask)) {
            addPrioritizedTasks(subtask);
            subtasks.put(subtask.getId(), subtask);
            return subtask;
        } else {
            throw new ValidationException("Задача пересекается по времени с уже существующей");
        }

    }

    @Override
    public Subtask removeSubtask(Integer id) {
        Subtask sub = subtasks.get(id);
        Epic epic = epicTasks.get(sub.getIdEpic());
        epic.getSubtasksId().remove(id);
        historyManager.remove(id);
        removePrioritizedTask(sub);
        return subtasks.remove(id);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private void addPrioritizedTasks(Task task) {
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
        if (original != null) {
            prioritizedTasks.remove(original);
        }
        prioritizedTasks.add(task);
    }

    private void removePrioritizedTask(Task task) {
        prioritizedTasks.remove(task);
    }

    private boolean isValid(Task task) {
        if (task == null) {
            throw new ValidationException("Задача равна null");
        }
        boolean result = true;
        for (Task saved : prioritizedTasks) {
            if (isIntersecting(task, saved)) {
                result = false;
                break;
            }
        }
        return result;
    }

    private boolean isIntersecting(Task task, Task saved) {
        boolean result = false;
        LocalDateTime startTime1 = task.getStartTime();
        LocalDateTime endTime1 = task.getEndTime();
        LocalDateTime startTime2 = saved.getStartTime();
        LocalDateTime endTime2 = saved.getEndTime();
        if (startTime1 != null && startTime2 != null) {
            boolean first = endTime1.isBefore(startTime2);
            boolean second = startTime1.isAfter(endTime2);
            result = !(first || second);
        }
        return result;
    }
}
