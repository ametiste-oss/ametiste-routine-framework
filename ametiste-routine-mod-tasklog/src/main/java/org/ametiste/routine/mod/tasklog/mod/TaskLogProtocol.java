package org.ametiste.routine.mod.tasklog.mod;

import org.ametiste.laplatform.protocol.Protocol;
import org.ametiste.routine.sdk.domain.TaskFilter;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * <p>
 *  Connect defines queries to task log, clients may use this protocol
 *  to retrieve access to tasks termination history and other log data related to
 *  tasks termination and lifecycle.
 * </p>
 *
 * @since 0.1.0
 */
public interface TaskLogProtocol extends Protocol {

    /**
     * <p>
     *  Queries task log for entries identifiers list using given filter and page parameters.
     * </p>
     *
     * @param filter log filter criteria builder
     * @param offset entries page offset
     * @param limit entries limit per page
     *
     * @return list of entries identifiers, may be empty.
     */
    List<UUID> findIdentifiers(Consumer<TaskFilter> filter, int offset, int limit);

}
