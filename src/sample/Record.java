package sample;

/**
 * Created by Gon on 2017-05-06.
 */
public class Record {
    private String name;
    private float mark;
    private String id;
    private String comment;

    public Record(String name, float mark) {
        this.name = name;
        this.mark = mark;
        this.id = "";
        this.comment = "";
    }

    public Record(String name) {
        this.name = name;
        this.mark = 0;
        this.id = "";
        this.comment = "";
    }

    public Record(String name, String id) {
        this.name = name;
        this.id = id;
        this.mark = 0;
        this.comment = "";
    }

    public Record(String name, float mark, String id) {
        this.name = name;
        this.mark = mark;
        this.id = id;
        this.comment = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Participant " + id+ ",,,,"+ mark+",,,,,"+ comment;
    }
}
