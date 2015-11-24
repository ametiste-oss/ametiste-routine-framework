package org.ametiste.routine.interfaces.web.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 *
 * @since
 */
public class IssueTaskData {

    private final String taskSchemeName;
    private final Map<String, String> schemeParams;

    @JsonCreator
    public IssueTaskData(
            @JsonProperty("scheme") String taskSchemeName,
            @JsonProperty("params") Map<String, String> schemeParams) {
        this.taskSchemeName = taskSchemeName;
        this.schemeParams = schemeParams;
    }

    public String taskSchemeName() {
        return taskSchemeName;
    }

    public Map<String, String> schemeParams() {
        return schemeParams;
    }
}
