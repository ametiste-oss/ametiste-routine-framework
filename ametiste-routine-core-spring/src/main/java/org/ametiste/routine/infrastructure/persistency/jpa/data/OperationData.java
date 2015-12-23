package org.ametiste.routine.infrastructure.persistency.jpa.data;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ame_routine_task_operation", indexes = {
        @Index(name = "op_state_idx", columnList = "state", unique = false),
        @Index(name = "op_task_idx", columnList = "task_id", unique = false)
})
public class OperationData implements Persistable<UUID> {

    @Id
    public UUID id;

    public String label;

    public String state;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", foreignKey = @ForeignKey(name = "fk_task_op_task_id"))
    public TaskData task;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id")
    public List<OperationPropertyData> properties = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id")
    public List<OperationNoticeData> notices = new ArrayList<>();

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return state.equals("NEW");
    }
}
