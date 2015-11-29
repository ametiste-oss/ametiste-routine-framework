package org.ametiste.routine.infrastructure.persistency.sdata;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ame_routine_task_notice")
public class TaskNoticeData {

    @Id
    @GeneratedValue
    public int id;

    @Column(name = "task_id")
    public UUID taskId;

    @Column(name = "cr_time")
    public Instant creationTime;

    public String text;

}