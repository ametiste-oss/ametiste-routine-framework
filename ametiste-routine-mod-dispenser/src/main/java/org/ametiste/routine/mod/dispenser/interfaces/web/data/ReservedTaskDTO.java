package org.ametiste.routine.mod.dispenser.interfaces.web.data;

import java.util.Map;

/**
 * <p>
 *     Represents task's data which execution was reserved by the external worker.
 * </p>
 *
 * <p>
 *     This object transfers all data that required to execute the given task.
 * </p>
 *
 * @since 0.0.1
 */
public class ReservedTaskDTO {

    private final String id;

    private final String line;

    private final Map<String, String> properties;

    public ReservedTaskDTO(String id,String line, Map<String, String> properties) {
        this.id = id;
        this.line = line;
        this.properties = properties;
    }

    public String getId() {
        return id;
    }

    public String getLine() {
        return line;
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
