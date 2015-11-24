package org.ametiste.routine.application.service.issue;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public interface TaskIssueService {

    UUID issueTask(String taskSchemeName, Map<String, String> params);

}
