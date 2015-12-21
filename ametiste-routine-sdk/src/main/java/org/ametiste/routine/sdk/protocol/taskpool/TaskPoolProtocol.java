package org.ametiste.routine.sdk.protocol.taskpool;

import org.ametiste.laplatform.protocol.Protocol;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public interface TaskPoolProtocol extends Protocol {

    UUID issueTask(String taskScheme, Map<String, String> params);

    // void removeTask();
}
