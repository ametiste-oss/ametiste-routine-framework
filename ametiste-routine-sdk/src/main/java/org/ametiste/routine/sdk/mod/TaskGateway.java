package org.ametiste.routine.sdk.mod;

import java.util.Map;

/**
 * <p>
 *     Defines entry point to task related service for external population strategies.
 * </p>
 *
 * @since 0.1.0
 */
@Deprecated
public interface TaskGateway {

    void issueTask(String schemaName, Map<String, String> schemaParams);

}
