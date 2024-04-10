package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private int id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime startTime;
    private Duration duration; //продолжительность в минутах


    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        status = TaskStatus.NEW;
    }

    public Task(String title, String description, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        status = TaskStatus.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public Integer getIdEpic() {
        return null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plus(duration);
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        if (startTime != null && duration != null) {
            return id == task.id && title.equals(task.title) && description.equals(
                    task.description) && status == task.status
                    && startTime.equals(task.startTime) && duration.equals(task.duration);
        } else {
            return id == task.id && title.equals(task.title) && description.equals(
                    task.description) && status == task.status;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status, startTime, duration);
    }

    @Override
    public String toString() {
        return this.getClass() + "{title=" + title + ", description=" + description + ", status=" + status
                + ", id=" + id + ", startTime=" + startTime + ", duration=" + duration + ", endTime=" + getEndTime() + '}';
    }
}
