package org.ametiste.routine.infrastructure.persistency.sdata;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "ame_routine_task_operation_property")
public class OperationPropertyData {

    @Id
    @GeneratedValue
    public int id;

    @Column(name = "operation_id")
    public UUID operationId;

    public String name;

    public String value;

}