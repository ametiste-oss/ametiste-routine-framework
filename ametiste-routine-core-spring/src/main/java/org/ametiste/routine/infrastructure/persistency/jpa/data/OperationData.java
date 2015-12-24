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

    private UUID id;

    private String label;

    private String state;

    private TaskData task;

    private List<OperationPropertyData> properties = new ArrayList<>();

    private List<OperationNoticeData> notices = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", foreignKey = @ForeignKey(name = "fk_task_op_task_id"))
    // NOTE: see resources/update.sql for foreign key replacement details
    public TaskData getTask() {
        return task;
    }

    @ElementCollection
    @CollectionTable(
            name = "ame_routine_task_operation_property",
            joinColumns = @JoinColumn(name = "operation_id"),
            indexes = @Index(name = "op_prop_op_id_idx", columnList = "operation_id", unique = false)
    )
    @org.hibernate.annotations.ForeignKey(name = "fk_op_prop_op_id")
    // NOTE: see resources/update.sql for foreign key replacement details
    public List<OperationPropertyData> getProperties() {
        return properties;
    }

    @ElementCollection
    @CollectionTable(
        name = "ame_routine_task_operation_notice",
        joinColumns = @JoinColumn(name = "operation_id"),
        foreignKey = @ForeignKey(name = "fk_op_notice_op_id"),
        indexes = @Index(name = "op_notice_op_id_idx", columnList = "operation_id", unique = false)
    )
    @org.hibernate.annotations.ForeignKey(name = "fk_op_notice_op_id")
    // NOTE: see resources/update.sql for foreign key replacement details
    public List<OperationNoticeData> getNotices() {
        return notices;
    }

    @Id
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    @Transient
    public boolean isNew() {
        return state.equals("NEW");
    }

    public void setNotices(final List<OperationNoticeData> notices) {
        this.notices = notices;
    }

    public void addNoticeData(OperationNoticeData operationNoticeData) {
        notices.add(operationNoticeData);
    }

    public void addPropertyData(OperationPropertyData operationPropertyData) {
        properties.add(operationPropertyData);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public void setTask(final TaskData task) {
        this.task = task;
    }

    public void setProperties(final List<OperationPropertyData> properties) {
        this.properties = properties;
    }

}
