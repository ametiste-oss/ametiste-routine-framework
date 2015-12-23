package org.ametiste.routine.infrastructure.persistency.jpa.data;

import javax.persistence.*;

@Entity
@Table(name = "ame_routine_task_property", indexes = {
        @Index(name = "task_prop_task_id_idx", columnList = "task_id", unique = false)
})
public class TaskPropertyData {

    @Id
    @GeneratedValue
    public int id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    public TaskData task;

    public String name;

    public String value;

}