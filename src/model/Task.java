package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private int id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime startTime;
    private Duration duration; //продолжительность в минутах
    public static final DateTimeFormatter START_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");


    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        status = TaskStatus.NEW;
    }

    public Task(String title, String description, String startTime, long duration) {
        this.title = title;
        this.description = description;
        status = TaskStatus.NEW;
        this.startTime = LocalDateTime.parse(startTime, START_TIME_FORMATTER);
        this.duration = Duration.ofMinutes(duration);
    }


    public String getTitle() {
        return title;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public Integer getIdEpic() {
        return null;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && title.equals(task.title) && description.equals(task.description) && status == task.status
                && startTime.equals(task.startTime) && duration.equals(task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status, startTime, duration);
    }

    @Override
    public String toString() {
        return this.getClass() + "{title=" + title + ", description=" + description + ", status=" + status
                + ", id=" + id + ", startTime=" + startTime + ", duration=" + duration + '}';
    }
}
