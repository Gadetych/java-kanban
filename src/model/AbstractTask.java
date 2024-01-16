package model;

public abstract class AbstractTask {
    private String title;
    private String description;
    private int id;
    private TaskStatus taskStatus;

    public AbstractTask(String title, String description, int id, TaskStatus taskStatus) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.taskStatus = taskStatus;
    }

    public String getTitle() {
        return title;
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

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public int hashCode() {
        return id*17;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        AbstractTask task = (AbstractTask) obj;
        return this.getId() == task.getId();
    }
}
