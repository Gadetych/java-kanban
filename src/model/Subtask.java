package model;

public class Subtask extends Task {
    Epic epic;
    public Subtask(String title, String description, Epic epic) {
        super(title, description);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }
}
