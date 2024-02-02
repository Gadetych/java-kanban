package model;

public class Subtask extends Task {
    int idEpic;
    public Subtask(String title, String description, Epic epic) {
        super(title, description);
        this.idEpic = epic.getId();
    }

    public int getIdEpic() {
        return idEpic;
    }
}
