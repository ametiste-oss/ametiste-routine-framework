package org.ametiste.routine.infrastructure.persistency.jpa.data;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "ame_routine_task_operation_property", indexes = {
        @Index(name = "op_prop_op_id_idx", columnList = "operation_id", unique = false)
})
public class OperationPropertyData {

    @Id
    @GeneratedValue
    public int id;

//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "operation_id", foreignKey = @ForeignKey(name = "fk_op_prop_op_id"))
//    public OperationData operation;

    public String name;

    public String value;

}