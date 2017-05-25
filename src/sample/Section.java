package sample;

import javafx.beans.Observable;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.util.Callback;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Gon on 2017-05-07.
 */
public class Section implements Serializable{
    private FloatProperty mark;
    private String name;
    private ArrayList<Requirement> requirements;
    private String className;
    private String methodName;
    private  TemplateControler controler;

    public Section(FloatProperty mark, String name, String className, String methodName, TemplateControler controler) {
        this.mark = mark;
        this.name = name;
        this.requirements = new ArrayList<>();
        this.className = className;
        this.methodName = methodName;
        this.controler = controler;
    }

    public Section(String name, String className, String methodName, TemplateControler controler) {
        this.name = name;
        this.className = className;
        this.methodName = methodName;
        this.requirements = new ArrayList<>();
        this.mark = new SerialSimpleFloatProperty(0);
    }

    public Section(String name) {
        this.name = name;
        this.requirements = new ArrayList<>();
        this.className = "";
        this.methodName = "";
        this.mark = new SerialSimpleFloatProperty(0);
    }

    public TemplateControler getControler() {
        return controler;
    }

    public void setControler(TemplateControler controler) {
        this.controler = controler;
    }

    public float getMark() {
        return mark.floatValue();
    }

    public void setMark(float mark) {
        this.mark.set(mark);
    }

    public ArrayList<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(ArrayList<Requirement> requirements) {
        this.requirements = requirements;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addRequirement(Requirement req){
        requirements.add(req);
        mark.set(getMark()+ req.getMark());
    }

    public void removeRequirement(Requirement req){
        requirements.remove(req);
        mark.set(mark.floatValue() - req.getMark());
    }

     @Override
    public boolean equals(Object obj){
         if (obj == null)
             return false;
         if (!(obj instanceof Section))
             return false;
         Section other = (Section) obj;
         return  name == other.name && mark == other.mark && className == other.className && methodName == other.methodName;//Compare Id if null falseF
     }

    @Override
    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }

    public static Callback<Section, Observable[]> extractor() {
        return new Callback<Section, Observable[]>() {
            @Override
            public Observable[] call(Section param) {
                return new Observable[]{param.mark};
            }
        };
    }
}
