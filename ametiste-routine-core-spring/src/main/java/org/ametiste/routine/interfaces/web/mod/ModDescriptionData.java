package org.ametiste.routine.interfaces.web.mod;

import java.util.Map;

/**
 *
 * @since
 */
public class ModDescriptionData {

    private final String name;
    private final String version;

    private final Map<String, Map<String, ? extends Object>> sections;

    public ModDescriptionData(String name, String version, final Map<String, Map<String, ? extends Object>> sections) {
        this.name = name;
        this.version = version;
        this.sections = sections;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, Map<String, ? extends Object>> getSections() {
        return sections;
    }

}
