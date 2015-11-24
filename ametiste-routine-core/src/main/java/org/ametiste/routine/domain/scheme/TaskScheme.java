package org.ametiste.routine.domain.scheme;

import org.ametiste.routine.domain.task.Task;

import java.util.Map;

/**
 *
 * @since
 */
public interface TaskScheme {

    Task createTask(Map<String, String> schemeParams);

}
