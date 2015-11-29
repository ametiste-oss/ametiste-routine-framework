package org.ametiste.routine.infrastructure.persistency.jpa.data;

import javax.persistence.*;

@Entity
@Table(name = "ame_routine_task_property")
public class TaskPropertyData {

    @Id
    @GeneratedValue
    public int id;

    public String name;

    public String value;

}