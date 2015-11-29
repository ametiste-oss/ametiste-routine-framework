package org.ametiste.routine.infrastructure.persistency.sdata;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ame_routine_task")
public class TaskData implements Serializable {

    @Id
    public UUID id;

    public String state;

    @OneToMany(cascade = CascadeType.ALL)
    public List<OperationData> operationData = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    public List<TaskPropertyData> properties = new ArrayList<>();

    @Column(name = "cr_time")
    public Instant creationTime;

    @Column(name = "execs_time")
    public Instant executionStartTime;

    @Column(name = "co_time")
    public Instant completionTime;

    @OneToMany(cascade = CascadeType.ALL)
    public List<TaskNoticeData> notices = new ArrayList<>();

}
