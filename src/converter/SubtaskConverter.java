package converter;

import model.Subtask;
import model.TaskType;

public class SubtaskConverter implements Converter<Subtask> {
    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString(Subtask task) {
        return String.format("%s,%s,%s,%s,%s,%s\n", task.getId(), task.getType(), task.getTitle(),
                task.getStatus(), task.getDescription(), task.getIdEpic());
    }
}
