package org.ametiste.routine.mod.tasklog.infrastructure.persistency;

/**
 * <p>
 *     Defines metric interface of task log persistency module.
 *     Used to hold names of metrics.
 * </p>
 *
 * <p>
 *     Variaous persistency implementations <b>must</b> provide
 *     defined metrics as required subset to unify monitoring and trace capabilities.
 * </p>
 *
 * @since 0.1.0
 */
public interface PersistencyMetrics {

    String TASK_LOG_REPO_PREFIX = "mods.task-log.infrastructure.persistency.task-repository.";

    String COUNT_ACTIVE_TASKS_TIMING = TASK_LOG_REPO_PREFIX +
            "count-active-tasks.timing";

    String COUNT_BY_STATE_AND_PROP_TIMING = TASK_LOG_REPO_PREFIX +
            "count-by-state-and-props.timing";

    String COUNT_BY_STATE_TIMING = TASK_LOG_REPO_PREFIX +
            "count-by-state.timing";

    String FIND_ENTRY_TIMING = TASK_LOG_REPO_PREFIX +
            "find-entry.timing";

    String FIND_ENTRIES_TIMING = TASK_LOG_REPO_PREFIX +
            "find-entries.timing";

    String FIND_BY_STATE_TIMING = TASK_LOG_REPO_PREFIX +
            "find-by-state.timing";

    String FIND_BY_STATE_AND_PROPS_TIMING = TASK_LOG_REPO_PREFIX +
            "find-by-state-and-props.timing";

    String FIND_ACTIVE_AFTER_DATE_TIMING = TASK_LOG_REPO_PREFIX +
            "find-active-after-date.timing";

}
