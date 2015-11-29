package org.ametiste.routine.infrastructure.persistency.jpa.data;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ame_routine_task")
public class TaskData implements Persistable<UUID> {

    @Id
    public UUID id;

    public String state;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    public List<OperationData> operationData = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    public List<TaskPropertyData> properties = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
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
