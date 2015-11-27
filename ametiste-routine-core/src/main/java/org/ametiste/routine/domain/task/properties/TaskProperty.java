package org.ametiste.routine.domain.task.properties;

/**
 *
 * @since
 */
public class TaskProperty {

    protected final String name;

    protected final String value;

    public TaskProperty(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public boolean equalsTo(TaskProperty otherProperty) {
        return this.name.equals(otherProperty.name) && this.value.equals(otherProperty.value);
    }

    public String name() {
        return name;
    }

    public String value() {
        return value;
    }

    // NOTE: this method must be implemented by subclasses to support concrete matching logic
    public boolean match(TaskProperty value) {
        return false;
    }

}
