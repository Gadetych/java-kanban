package service;

import model.AbstractTask;
import model.Epic;

import java.util.HashMap;

public class TasksManager {
    private HashMap<Integer, AbstractTask> tasks;


    public TasksManager(HashMap<Integer, AbstractTask> tasks) {
        this.tasks = tasks;
    }

    public HashMap<Integer, AbstractTask> getTasks() {
        return tasks;
    }

    public void clear(){

    }

    public AbstractTask remove(int id) {
        return null;
    }

    public AbstractTask getTask(int id) {
        return null;
    }

}
