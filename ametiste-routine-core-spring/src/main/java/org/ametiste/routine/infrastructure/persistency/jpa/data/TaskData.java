package org.ametiste.routine.infrastructure.persistency.jpa.data;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ame_routine_task", indexes = {
        @Index(name = "task_state_idx", columnList = "state", unique = false),
        @Index(name = "task_cr_time_idx", columnList = "cr_time", unique = false),
        @Index(name = "task_execs_time_idx", columnList = "execs_time", unique = false),
        @Index(name = "task_co_time_idx", columnList = "co_time", unique = false)
})
public class TaskData implements Persistable<UUID> {

    @Id
    public UUID id;

    public String state;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    public List<OperationData> operationData = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    public List<TaskPropertyData> properties = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    public List<TaskNoticeData> notices = new ArrayList<>();

    @Column(name = "cr_time")
    public Instant creationTime;

    @Column(name = "execs_time")
    public Instant executionStartTime;

    @Column(name = "co_time")
    public Instant completionTime;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return state.equals("NEW");
    }
}
