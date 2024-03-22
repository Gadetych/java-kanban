package service;

import java.nio.file.Paths;

public class Managers {

    public static TasksManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static TasksManager getDefaultFile() {
        return new FileBackedTaskManager(getDefaultHistory(), Paths.get("TASK_CSV.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
