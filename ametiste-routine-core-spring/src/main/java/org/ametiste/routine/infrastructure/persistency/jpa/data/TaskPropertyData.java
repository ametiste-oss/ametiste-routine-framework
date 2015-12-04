package org.ametiste.routine.infrastructure.persistency.jpa.data;

import javax.persistence.*;

@Entity
@Table(name = "ame_routine_task_property", indexes = {
        @Index(name = "task_prop_name_idx", columnList = "name", unique = false),
        @Index(name = "task_prop_value_idx", columnList = "value", unique = false)
})
public class TaskPropertyData {

    @Id
    @GeneratedValue
    public int id;

    public String name;

    public String value;

}