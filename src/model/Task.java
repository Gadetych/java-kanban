package model;

import java.util.Objects;

public class Task {
    private int id;
    private String title;
    private String description;
    private TaskStatus status;


    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        status = TaskStatus.NEW;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && title.equals(task.title) && description.equals(task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status);
    }

    @Override
    public String toString() {
        return this.getClass() + "{title=" + title + ", description=" + description + ", status=" + status + ", id=" + id + '}';
    }
}
