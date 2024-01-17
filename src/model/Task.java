package model;

public class Task {
    private String title;
    private String description;
    private int id;
    private TaskStatus status;


    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        status = TaskStatus.NEW;
    }
//
////    !?
//    public Task(String title, String description, TaskStatus taskStatus) {
//        this.title = title;
//        this.description = description;
//        this.id = ++counter;
//        this.status = taskStatus;
//    }

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

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return id*17;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return this.getId() == task.getId();
    }
}
