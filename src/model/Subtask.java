package model;

public class Subtask extends Task {
    int idEpic;
    public Subtask(String title, String description, Epic epic) {
        super(title, description);
        this.idEpic = epic.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return idEpic == subtask.idEpic;
    }


    public int getIdEpic() {
        return idEpic;
    }
}
