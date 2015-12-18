package org.ametiste.routine.mod.tasklog.mod;

import org.ametiste.laplatform.protocol.Protocol;
import org.ametiste.routine.domain.task.Task;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * <p>
 *  Protocol defines queries to task log, clients may use this protocol
 *  to retrieve access to tasks execution history and other log data related to
 *  tasks execution and lifecycle.
 * </p>
 *
 * @since 0.1.0
 */
public interface TaskLogProtocol extends Protocol {

    /**
     *  <p>
     *      Filter that must be applied to limit task log query. Note,
     *      each preducate is incremental, "AND" semantic must be used to build
     *      filter based on the given predicates.
     *  </p>
     *
     *  @since 0.1.0
     */
    interface LogFilter {

        void stateIn(List<Task.State> state);

        void creationTimeAfter(Instant crTime);

        void execStartTimeAfter(Instant exTime);

        void completionTimeAfter(Instant exTime);

    }

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
    List<UUID> findIdentifiers(Consumer<LogFilter> filter, int offset, int limit);

}
