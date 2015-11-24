package org.ametiste.routine.domain.task.reflect;

import org.ametiste.domain.DomainReflection;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.notices.Notice;

import java.time.Instant;
import java.util.UUID;

/**
 *
 * @since
 */
public interface TaskReflection extends DomainReflection {

    void flareTaskId(UUID taskId);

    void flareTaskState(Task.State state);

    // TODO: Can I use flat values here?
    void flareOperation(OperationFlare operationFlare);

    void flareProperty(String name, String value);

    void flareTaskTimes(Instant creationTime, Instant executionStartTime, Instant completionTime);

    void reflect(TaskReflection reflection);

    void flareNotice(Notice notice);

}
