package org.ametiste.routine.infrastructure.protocol.taskpool;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.sdk.mod.TaskPool;
import org.ametiste.routine.sdk.mod.protocol.MessageSession;
import org.ametiste.routine.sdk.mod.protocol.Protocol;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.ametiste.routine.sdk.mod.TaskPool.Message.*;

/**
 *
 * @since
 */
public class TaskPoolProtocol implements Protocol {

    private final TaskIssueService taskIssueService;

    public TaskPoolProtocol(TaskIssueService taskIssueService) {
        this.taskIssueService = taskIssueService;
    }

    @Override
    public MessageSession message(String messageType) {

        if (messageType.equals(IssueTask.TYPE)) {
            return new IssueTaskMessageSession(taskIssueService);
        } else {
            // TODO: add ProtocolException -> UnsupportedMessageException
            throw new RuntimeException("Unsupported session: " + messageType);
        }

    }
}
