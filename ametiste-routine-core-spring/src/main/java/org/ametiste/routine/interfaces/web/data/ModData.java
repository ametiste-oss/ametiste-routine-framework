package org.ametiste.routine.interfaces.web.data;

/**
 *
 * @since
 */
public class ModData {

    private final String name;
    private final String version;

    public ModData(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
