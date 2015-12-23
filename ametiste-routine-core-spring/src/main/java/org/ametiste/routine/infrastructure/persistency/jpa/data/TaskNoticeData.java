package org.ametiste.routine.infrastructure.persistency.jpa.data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "ame_routine_task_notice", indexes = {
        @Index(name = "task_notice_task_id_idx", columnList = "task_id", unique = false)
})
public class TaskNoticeData {

    @Id
    @GeneratedValue
    public int id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    public TaskData task;

    @Column(name = "cr_time")
    public Instant creationTime;

    public String text;

}