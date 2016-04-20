package org.ametiste.dynamics.foundation.feature;

import org.ametiste.dynamics.SurfaceFeature;

/**
 *
 * @since 1.0
 */
@SurfaceFeature
public class ReferenceFeature<T> {

    private final Class<T> type;

    public ReferenceFeature(Class<T> type) {
        this.type = type;
    }

    public Class<T> type() {
        return type;
    };

    public boolean referencesTo(Class<?> other) {
        return other.isAssignableFrom(type);
    }

}
