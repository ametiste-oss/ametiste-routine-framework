package org.ametiste.routine.infrastructure.persistency.jpa.data;

import javax.persistence.*;

@Entity
@Table(name = "ame_routine_task_operation_property", indexes = {
        @Index(name = "op_prop_op_id_idx", columnList = "operation_id", unique = false),
        @Index(name = "op_prop_name_idx", columnList = "name", unique = false),
        @Index(name = "op_prop_value_idx", columnList = "value", unique = false)
})
public class OperationPropertyData {

    @Id
    @GeneratedValue
    public int id;

    public String name;

    public String value;

}