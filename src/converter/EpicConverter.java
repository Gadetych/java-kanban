package converter;

import model.Epic;
import model.TaskType;

public class EpicConverter implements Converter<Epic> {
    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString(Epic task) {
        return String.format("%s,%s,%s,%s,%s,%s\n", task.getId(), task.getType(), task.getTitle(),
                task.getStatus(), task.getDescription(), null);
    }
}
