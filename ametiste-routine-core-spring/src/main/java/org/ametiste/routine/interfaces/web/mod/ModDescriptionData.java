package org.ametiste.routine.interfaces.web.mod;

/**
 *
 * @since
 */
public class ModDescriptionData {

    private final String name;
    private final String version;

    public ModDescriptionData(String name, String version) {
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
