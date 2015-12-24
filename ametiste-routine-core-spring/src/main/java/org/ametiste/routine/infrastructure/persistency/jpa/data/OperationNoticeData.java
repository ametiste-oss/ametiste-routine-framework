package org.ametiste.routine.infrastructure.persistency.jpa.data;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.Instant;

@Embeddable
@Table(name = "ame_routine_task_operation_notice", indexes = {
        @Index(name = "op_notice_op_id_idx", columnList = "operation_id", unique = false)
})
public class OperationNoticeData {

    private Instant creationTime;

    private String text;

    @Column(name = "cr_time")
    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(final Instant creationTime) {
        this.creationTime = creationTime;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

}