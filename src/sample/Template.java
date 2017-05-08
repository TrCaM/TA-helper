package sample;

import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Gon on 2017-05-07.
 */
public class Template implements Serializable{
    private float mark;
    private ArrayList<Section> sections;

    public Template() {
        sections = new ArrayList<>();
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }

    public void addSection(Section section){
        sections.add(section);
    }

    public void removeSection(Section section){
        sections.remove(section);
    }

    public void countMark() {
        for (Section s: sections){
            mark += s.getMark();
        }
        System.out.println(mark);
    }

    @Override
    public String toString() {
        return "The total mark is: " + this.mark;
    }
}
