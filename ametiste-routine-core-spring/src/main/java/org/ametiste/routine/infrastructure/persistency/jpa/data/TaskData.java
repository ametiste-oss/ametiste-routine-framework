package org.ametiste.routine.infrastructure.persistency.jpa.data;

import org.hibernate.annotations.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ame_routine_task", indexes = {
        @Index(name = "task_state_idx", columnList = "state", unique = false),
        @Index(name = "task_creator_idx", columnList = "creatorId", unique = false),
        @Index(name = "task_scheme_idx", columnList = "schemeId", unique = false),
        @Index(name = "task_cr_time_idx", columnList = "cr_time", unique = false),
        @Index(name = "task_execs_time_idx", columnList = "execs_time", unique = false),
        @Index(name = "task_co_time_idx", columnList = "co_time", unique = false)
})
public class TaskData implements Persistable<UUID> {

    private UUID id;

    private String state;

    private List<OperationData> operations = new ArrayList<>();

    private List<TaskPropertyData> properties = new ArrayList<>();

    private List<TaskNoticeData> notices = new ArrayList<>();

    private String creatorId;

    private String schemeId;

    private Date creationTime;

    private Date executionStartTime;

    private Date completionTime;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "task")
    public List<OperationData> getOperations() {
        return operations;
    }

    @ElementCollection
    @CollectionTable(
            name = "ame_routine_task_property",
            joinColumns = @JoinColumn(name = "task_id"),
            indexes = @Index(name = "task_prop_task_id_idx", columnList = "task_id", unique = false)
    )
    @org.hibernate.annotations.ForeignKey(name = "fk_task_prop_task_id")
    // NOTE: see resources/update.sql for foreign key replacement details
    public List<TaskPropertyData> getProperties() {
        return properties;
    }

    @ElementCollection
    @CollectionTable(
            name = "ame_routine_task_notice",
            joinColumns = @JoinColumn(name = "task_id"),
            indexes = @Index(name = "task_notice_task_id_idx", columnList = "task_id", unique = false)
    )
    @org.hibernate.annotations.ForeignKey(name = "fk_task_notice_task_id")
    // NOTE: see resources/update.sql for foreign key replacement details
    public List<TaskNoticeData> getNotices() {
        return notices;
    }

    @Id
    @Override
    public UUID getId() {
        return id;
    }

    @Column(name = "co_time")
    public Date getCompletionTime() {
        return completionTime;
    }

    @Column(name = "execs_time")
    public Date getExecutionStartTime() {
        return executionStartTime;
    }

    @Column(name = "cr_time")
    public Date getCreationTime() {
        return creationTime;
    }

    @Override
    @Transient
    public boolean isNew() {
        return state.equals("NEW");
    }

    public void setNotices(final List<TaskNoticeData> notices) {
        this.notices = notices;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(final String creatorId) {
        this.creatorId = creatorId;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(final String schemeId) {
        this.schemeId = schemeId;
    }

    public void setCreationTime(final Date creationTime) {
        this.creationTime = creationTime;
    }

    public void setExecutionStartTime(final Date executionStartTime) {
        this.executionStartTime = executionStartTime;
    }

    public void setCompletionTime(final Date completionTime) {
        this.completionTime = completionTime;
    }

    public void addNoticeData(TaskNoticeData noticeData) {
        notices.add(noticeData);
    }

    public void addPropertyData(TaskPropertyData propertyData) {
        properties.add(propertyData);
    }

    public void addOperationData(OperationData operationData) {
        operationData.setTask(this);
        this.operations.add(operationData);
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

    public void setOperations(final List<OperationData> operations) {
        this.operations = operations;
    }

    public void setProperties(final List<TaskPropertyData> properties) {
        this.properties = properties;
    }


}
