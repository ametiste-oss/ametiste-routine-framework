package org.ametiste.routine.meta.scheme;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @since
 */
public class TaskSchemeCall {

    private final Map<String, String> params;

    TaskSchemeCall(final Map<String, String> params) {
        this.params = Collections.unmodifiableMap(new HashMap<>(params));
    }

    public final Map<String, String> params() {
        return params;
    }

    static final TaskSchemeCall of(final Map<String, String> params) {
        return new TaskSchemeCall(params);
    }

}