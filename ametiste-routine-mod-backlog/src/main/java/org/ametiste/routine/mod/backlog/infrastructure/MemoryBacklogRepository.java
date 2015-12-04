package org.ametiste.routine.mod.backlog.infrastructure;

import org.ametiste.routine.mod.backlog.domain.Backlog;
import org.ametiste.routine.mod.backlog.domain.BacklogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @since
 */
public class MemoryBacklogRepository implements BacklogRepository {

    private Map<String, Backlog> backlogs = new ConcurrentHashMap<>();

    public MemoryBacklogRepository(List<Backlog> backlogs) {
        backlogs.forEach(this::save);
    }

    @Override
    public List<Backlog> loadAll() {
        return new ArrayList<>(backlogs.values());
    }

    @Override
    public Backlog loadBacklogOf(String taskSchemeName) {
        return backlogs.get(taskSchemeName);
    }

    @Override
    public void save(Backlog backlog) {
        backlogs.put(backlog.boundTaskScheme(), backlog);
    }

}
