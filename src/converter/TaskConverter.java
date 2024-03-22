package converter;

import model.Task;
import model.TaskType;

public class TaskConverter implements Converter<Task> {

    @Override
    public TaskType getType() {
        return TaskType.TASK;
    }

    @Override
    public String toString(Task task) {
        return String.format("%s,%s,%s,%s,%s,%s\n", task.getId(), task.getType(), task.getTitle(),
                task.getStatus(), task.getDescription(), null);
    }
}
