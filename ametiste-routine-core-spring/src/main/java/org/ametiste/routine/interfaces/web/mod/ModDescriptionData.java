package org.ametiste.routine.interfaces.web.mod;

import java.util.Map;

/**
 *
 * @since
 */
public class ModDescriptionData {

    private final String name;
    private final String version;
    private final Map<String, String> attributes;

    public ModDescriptionData(String name, String version, final Map<String, String> attributes) {
        this.name = name;
        this.version = version;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

}
