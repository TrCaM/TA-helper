package sample;

import org.json.simple.JSONObject;

import java.io.Serializable;

/**
 * Created by Gon on 2017-05-07.
 */
public class Requirement implements Serializable{
    private float mark;
    private String content;
    private String className;
    private String methodName;

    public Requirement(float mark, String content, String className, String methodName) {
        this.mark = mark;
        this.content = content;
        this.className = className;
        this.methodName = methodName;
    }


    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Override
    public boolean equals(Object obj){
        if (obj == null)
            return false;
        if (!(obj instanceof Requirement))
            return false;
        Requirement other = (Requirement) obj;
        return  content == other.content && mark == other.mark && className == other.className && methodName == other.methodName;//Compare Id if null falseF
    }

    @Override
    public int hashCode() {
        return content == null ? 0 : content.hashCode();
    }
}
