package org.ametiste.routine.infrastructure.persistency.jpa.data;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "ame_routine_task_property", indexes = {
        @Index(name = "task_prop_task_id_idx", columnList = "task_id", unique = false)
})
public class TaskPropertyData {

    @Id
    @GeneratedValue
    public int id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", foreignKey = @ForeignKey(name = "fk_task_prop_task_id"))
    public TaskData task;

    public String name;

    public String value;

}