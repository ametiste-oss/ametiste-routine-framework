package org.ametiste.routine.infrastructure.persistency.sdata;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "ame_routine_task_property")
public class TaskPropertyData {

    @Id
    @GeneratedValue
    public int id;

    @Column(name = "task_id")
    public UUID taskId;

    public String name;

    public String value;

}