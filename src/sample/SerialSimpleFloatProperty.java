package sample;

import javafx.beans.property.SimpleFloatProperty;

import java.io.Serializable;

/**
 * Created by Gon on 2017-05-08.
 */
public class SerialSimpleFloatProperty extends SimpleFloatProperty implements Serializable{
    public SerialSimpleFloatProperty() {
    }

    public SerialSimpleFloatProperty(float initialValue) {
        super(initialValue);
    }

    public SerialSimpleFloatProperty(Object bean, String name) {
        super(bean, name);
    }

    public SerialSimpleFloatProperty(Object bean, String name, float initialValue) {
        super(bean, name, initialValue);
    }
}
