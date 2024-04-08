package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subtasksId;
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description, null, Duration.ofMinutes(0));
        this.endTime = LocalDateTime.now();
        this.subtasksId = new ArrayList<>();
    }


    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public Integer getIdEpic() {
        return null;
    }

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setSubtasksId(List<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksId);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return subtasksId.equals(epic.subtasksId);
    }

    @Override
    public String toString() {
        return super.toString() +
                ", subtasksId=" + subtasksId.toString();
    }

    public void addSubtask(Subtask subtask) {
        subtasksId.add(subtask.getId());
    }

}
