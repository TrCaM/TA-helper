package sample;

/**
 * Created by Gon on 2017-05-06.
 */
public class Record {
    private String name;
    private float mark;

    public Record(String name, float mark) {
        this.name = name;
        this.mark = mark;
    }

    public Record(String name) {
        this.name = name;
        this.mark = 0;
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
}
