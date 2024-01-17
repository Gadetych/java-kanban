package model;

import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks;

    public Epic(String title, String description) {
        super(title, description);

        this.subtasks = new HashMap<>();
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(HashMap<Integer, Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public Subtask addSubtask(Integer id, Subtask subtask) {
        subtasks.put(id, subtask);
        return subtask;
    }

    public void updateStatus() {
        if (subtasks == null) {
            setStatus(TaskStatus.NEW);
            return;
        }
        int counterNewStatus = 0;
        int counterDoneStatus = 0;
        for (Integer i : subtasks.keySet()) {
            Subtask subtask = subtasks.get(i);
            if (subtask.getStatus() == TaskStatus.NEW) {
                ++counterNewStatus;
            } else if (subtask.getStatus() == TaskStatus.DONE) {
                ++counterDoneStatus;
            }
        }
        if (counterNewStatus == subtasks.size()) {
            setStatus(TaskStatus.NEW);
        } else if (counterDoneStatus == subtasks.size()) {
            setStatus(TaskStatus.DONE);
        } else {
            setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
