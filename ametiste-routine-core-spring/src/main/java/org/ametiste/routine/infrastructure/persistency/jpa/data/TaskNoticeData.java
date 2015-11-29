package org.ametiste.routine.infrastructure.persistency.jpa.data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "ame_routine_task_notice")
public class TaskNoticeData {

    @Id
    @GeneratedValue
    public int id;

    @Column(name = "cr_time")
    public Instant creationTime;

    public String text;

}