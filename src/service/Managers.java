package service;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Managers {

    public static TasksManager getDefault() {
        Path path = Paths.get("tasks.csv");
        return new FileBackedTaskManager(getDefaultHistory(), path);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
