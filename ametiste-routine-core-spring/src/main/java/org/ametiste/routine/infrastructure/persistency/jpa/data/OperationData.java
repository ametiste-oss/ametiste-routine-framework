package org.ametiste.routine.infrastructure.persistency.jpa.data;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ame_routine_task_operation")
public class OperationData implements Persistable<UUID> {

    @Id
    public UUID id;

    public String label;

    public String state;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<OperationPropertyData> properties = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
