package org.ametiste.routine.meta.scheme;

import java.util.List;
import java.util.Map;

public class TaskSchemeTrace {

    private final String schemeName;
    private final List<Map<String, String>> params;

    TaskSchemeTrace(String schemeName, List<Map<String, String>> params) {
        this.schemeName = schemeName;
        this.params = params;
    }

    public String name() {
        return schemeName;
    }

    public List<Map<String, String>> params() {
        return params;
    }

}
