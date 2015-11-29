package org.ametiste.routine.infrastructure.persistency.sdata;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ame_routine_task_operation")
public class OperationData {

    @Id
    public UUID id;

    public String label;

    public String state;

    @Embedded
    public List<OperationPropertyData> properties = new ArrayList<>();

    @Embedded
    public List<OperationNoticeData> notices = new ArrayList<>();

}
