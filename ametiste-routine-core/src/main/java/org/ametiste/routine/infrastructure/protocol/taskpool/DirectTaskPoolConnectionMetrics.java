package org.ametiste.routine.infrastructure.protocol.taskpool;

import org.ametiste.metrics.annotations.markers.MetricsInterface;
import org.ametiste.routine.sdk.protocol.taskpool.TaskPoolProtocol;

/**
 * <p>
 *     Set of metrics for {@link DirectTaskPoolConnection} implementation of {@link TaskPoolProtocol}.
 * </p>
 *
 * @since 0.1.0
 */
@MetricsInterface
public interface DirectTaskPoolConnectionMetrics {

    String __PREFIX = "core.infrastructure.protocol.task-pool";

    String __OVERALL_PREFIX = __PREFIX + ".overall";

    String CLIENTS_PREFIX = __PREFIX + ".clients";

    String OVERAL_ISSUE_TASK_TIMING = __OVERALL_PREFIX + ".issue-task.timing";

    String CLIENT_ISSUE_TASK_TIMING = "target.clientId + '.issue-task.timing'";

    String OVERAL_REMOVE_TASKS_TIMING = __OVERALL_PREFIX + ".remove-tasks.timing";

    String CLIENT_REMOVE_TASKS_TIMING = "target.clientId + '.remove-tasks.timing'";

    /**
     * <p>
     *     Returns current protocol client identifier.
     * </p>
     *
     * @return client identifier, can't be null.
     */
    String getClientId();

}
