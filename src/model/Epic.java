package model;

import java.util.HashMap;

public class Epic extends AbstractTask {
    private HashMap<Integer, SubTask> subTaskHashMap;

    public Epic(String title, String description, int id, TaskStatus taskStatus, HashMap<Integer, SubTask> subTaskHashMap) {
        super(title, description, id, taskStatus);
        this.subTaskHashMap = subTaskHashMap;
    }
}
