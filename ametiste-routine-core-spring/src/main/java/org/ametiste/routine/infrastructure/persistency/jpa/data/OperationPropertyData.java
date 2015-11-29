package org.ametiste.routine.infrastructure.persistency.jpa.data;

import javax.persistence.*;

@Entity
@Table(name = "ame_routine_task_operation_property")
public class OperationPropertyData {

    @Id
    @GeneratedValue
    public int id;

    public String name;

    public String value;

}