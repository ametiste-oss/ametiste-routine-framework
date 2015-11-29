package org.ametiste.routine.infrastructure.persistency.sdata;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ame_routine_task_operation_notice")
public class OperationNoticeData {

    @Id
    @GeneratedValue
    public int id;

    @Column(name = "operation_id")
    public UUID operationId;

    @Column(name = "cr_time")
    public Instant creationTime;

    public String text;

}