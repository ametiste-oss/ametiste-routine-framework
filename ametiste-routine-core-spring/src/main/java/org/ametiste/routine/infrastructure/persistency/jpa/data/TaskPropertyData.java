package org.ametiste.routine.infrastructure.persistency.jpa.data;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Embeddable
@Table(name = "ame_routine_task_property", indexes = {
        @Index(name = "task_prop_task_id_idx", columnList = "task_id", unique = false)
})
public class TaskPropertyData {

    private String name;

    private String value;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}