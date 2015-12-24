package org.ametiste.routine.infrastructure.persistency;

import org.ametiste.metrics.annotations.markers.MetricsInterface;

/**
 *
 * @since 0.1.0
 */
@MetricsInterface
public interface PersistencyMetrics {

    String TASK_REPO_PREFIX = "core.infrastructure.persistency.task-repository.";


    String COUNT_TASKS_WITH_FILTER = TASK_REPO_PREFIX +
            "count-with-filter.timing";


    String FIND_TASKS_WITH_FILTER = TASK_REPO_PREFIX +
            "find-with-filter.timing";

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


    String DELETE_TASKS_TIMING = TASK_REPO_PREFIX +
            "delete.timing";

}
