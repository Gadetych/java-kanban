package converter;

import model.Task;
import model.TaskType;

public interface Converter<T extends Task> {

    TaskType getType();

    String toString(T task);
}
