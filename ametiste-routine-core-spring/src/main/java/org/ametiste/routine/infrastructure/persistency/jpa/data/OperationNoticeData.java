package org.ametiste.routine.infrastructure.persistency.jpa.data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "ame_routine_task_operation_notice", indexes = {
        @Index(name = "op_notice_op_id_idx", columnList = "operation_id", unique = false)
})
public class OperationNoticeData {

    @Id
    @GeneratedValue
    public int id;

    @Column(name = "cr_time")
    public Instant creationTime;

    public String text;

}