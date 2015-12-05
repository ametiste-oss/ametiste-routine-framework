package org.ametiste.routine.infrastructure.persistency;

import org.ametiste.metrics.annotations.markers.MetricsInterface;

/**
 *
 * @since 0.1.0
 */
@MetricsInterface
public interface PersistencyMetrics {

    String TASK_REPO_PREFIX = "infrastructure.persistency.task-repository.";

    String FIND_TASK_BY_ID_TIMING = TASK_REPO_PREFIX +
            "find-by-id.timing";

    String FIND_TASK_BY_STATE = TASK_REPO_PREFIX +
            "find-by-state.timing";

    String FIND_TASK_BY_MULTIPLE_STATE = TASK_REPO_PREFIX +
            "find-by-multiple-state.timing";

    String FIND_TASK_BY_OP_ID = TASK_REPO_PREFIX +
            "find-by-op-id.timing";

    String SAVE_TASK_TIMING = TASK_REPO_PREFIX +
            "save.timing";

}
