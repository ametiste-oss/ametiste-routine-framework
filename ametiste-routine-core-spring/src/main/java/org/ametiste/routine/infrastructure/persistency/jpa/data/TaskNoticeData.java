package org.ametiste.routine.infrastructure.persistency.jpa.data;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.Instant;

@Embeddable
@Table(name = "ame_routine_task_notice", indexes = {
        @Index(name = "task_notice_task_id_idx", columnList = "task_id", unique = false)
})
public class TaskNoticeData {

    private Instant creationTime;

    private String text;

    @Column(name = "cr_time")
    public Instant getCreationTime() {
        return creationTime;
    }

    public String getText() {
        return text;
    }

    public void setCreationTime(final Instant creationTime) {
        this.creationTime = creationTime;
    }

    public void setText(final String text) {
        this.text = text;
    }
}