package org.ametiste.routine.mod.backlog.domain;

import java.util.List;

/**
 * <p>
 *     Simple backlog repository protocol, defines operation to store and load
 *     backlogs, simple.
 * </p>
 *
 * @since 0.1.0
 */
public interface BacklogRepository {

    List<Backlog> loadAll();

    Backlog loadBacklogOf(String taskSchemeName);

    void save(Backlog backlog);

}
