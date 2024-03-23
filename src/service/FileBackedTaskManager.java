package service;

import exeption.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path path;

    public FileBackedTaskManager(HistoryManager historyManager, Path path) {
        super(historyManager);
        this.path = path;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(), StandardCharsets.UTF_8))) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : this.getTasks().values()) {
                writer.write(toString(task));
            }
            for (Epic task : this.getEpicTasks().values()) {
                writer.write(toString(task));
            }
            for (Subtask task : this.getSubtasks().values()) {
                writer.write(toString(task));
            }
            writer.newLine();
            writer.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    static FileBackedTaskManager loadFromFile(Path path) {
        FileBackedTaskManager manager = new FileBackedTaskManager(Managers.getDefaultHistory(), path);
        manager.loadFromFile();
        return manager;
    }

    private void loadFromFile() {
        Map<Integer, Task> tasks = getTasks();
        Map<Integer, Subtask> subtasks = getSubtasks();
        Map<Integer, Epic> epics = getEpicTasks();
        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile(), StandardCharsets.UTF_8))) {
            reader.readLine();
            String line = reader.readLine();
            while (!line.isBlank()) {
                Task task = fromString(line);
                int id = task.getId();
                if (maxId < id) {
                    maxId = id;
                }
                switch (task.getType()) {
                    case TASK:
                        tasks.put(id, task);
                        break;
                    case SUBTASK:
                        subtasks.put(id, (Subtask) task);
                        break;
                    case EPIC:
                        epics.put(id, (Epic) task);
                        break;
                }
            }
            setCounter(maxId);
            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                Integer idEpic = entry.getKey();
                Epic epic = entry.getValue();
                List<Integer> subtasksId = epic.getSubtasksId();
                for (Subtask subtask : subtasks.values()) {
                    if (idEpic.equals(subtask.getIdEpic())) {
                        subtasksId.add(subtask.getId());
                    }
                }
            }
            line = reader.readLine();
            List<Integer> history = historyFromString(line);
            HistoryManager historyManager = getHistoryManager();
            Map<Integer, Task> all = new HashMap<>(tasks);
            all.putAll(subtasks);
            all.putAll(epics);
            for (Integer id : history) {
                for (Task task : all.values()) {
                    if (id.equals(task.getId())) {
                        historyManager.add(task);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    private String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder result = new StringBuilder();
        for (Task task : history) {
            result.append(task.getId()).append(",");
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

    private List<Integer> historyFromString(String value) {
        String[] ids = value.split(",");
        List<Integer> list = new ArrayList<>();
        for (String s : ids) {
            list.add(Integer.getInteger(s));
        }
        return list;
    }

    private String toString(Task task) {
        return String.format("%s,%s,%s,%s,%s,%s\n", task.getId(), task.getType(), task.getTitle(),
                task.getStatus(), task.getDescription(), task.getIdEpic());
    }

    private Task fromString(String value) {
        String[] array = value.split(",");
//        id,type,name,status,description,epic_id
        String id = array[0];
        String type = array[1];
        String titleName = array[2];
        String status = array[3];
        String description = array[4];
        String epicId = array[5];
        switch (TaskType.valueOf(type)) {
            case TASK:
                Task task = new Task(titleName, description);
                task.setId(Integer.getInteger(id));
                task.setStatus(TaskStatus.valueOf(status));
                return task;
            case EPIC:
                Task epic = new Epic(titleName, description);
                epic.setId(Integer.getInteger(id));
                epic.setStatus(TaskStatus.valueOf(status));
                return epic;
            case SUBTASK:
                Task subtask = new Subtask(titleName, description, Integer.getInteger(epicId));
                subtask.setId(Integer.getInteger(id));
                subtask.setStatus(TaskStatus.valueOf(status));
                return subtask;
        }
        return null;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return super.getHistoryManager();
    }

    @Override
    public Map<Integer, Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public Task getTask(Integer id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Task createTask(Task task) {
        Task newTask = super.createTask(task);
        save();
        return newTask;
    }

    @Override
    public Task updateTask(Task task) {
        Task newTask = super.updateTask(task);
        save();
        return newTask;
    }

    @Override
    public Task removeTask(Integer id) {
        Task newTask = super.removeTask(id);
        save();
        return newTask;
    }

    @Override
    public Map<Integer, Epic> getEpicTasks() {
        return super.getEpicTasks();
    }

    @Override
    public List<Subtask> getEpicSubtasks(Integer id) {
        return super.getEpicSubtasks(id);
    }

    @Override
    public void clearEpicTasks() {
        super.clearEpicTasks();
        save();
    }

    @Override
    public Epic getEpicTask(Integer id) {
        Epic epic = super.getEpicTask(id);
        save();
        return epic;
    }

    @Override
    public Epic createEpicTask(Epic epicTask) {
        Epic newEpic = super.createEpicTask(epicTask);
        save();
        return newEpic;
    }

    @Override
    public Epic updateEpicTask(Epic epicTask) {
        Epic newEpic = super.updateEpicTask(epicTask);
        save();
        return newEpic;
    }

    @Override
    public Epic removeEpicTask(Integer id) {
        Epic newEpic = super.removeEpicTask(id);
        save();
        return newEpic;
    }

    @Override
    public Map<Integer, Subtask> getSubtasks() {
        return super.getSubtasks();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public Subtask getSubtask(Integer id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask newSub = super.createSubtask(subtask);
        save();
        return newSub;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Subtask newSub = super.updateSubtask(subtask);
        save();
        return newSub;
    }

    @Override
    public Subtask removeSubtask(Integer id) {
        Subtask newSub = super.removeSubtask(id);
        save();
        return newSub;
    }
}
