package org.ametiste.routine.sdk.mod;

import org.ametiste.routine.sdk.mod.protocol.Protocol;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public interface TaskPoolProtocol extends Protocol {

    UUID issueTask(String taskScheme, Map<String, String> params);

}
