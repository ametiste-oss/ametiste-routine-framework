package org.ametiste.dynamics.foundation.structure;

import org.ametiste.dynamics.DynamicSurfaceStructure;
import org.ametiste.dynamics.SurfaceStructure;
import org.ametiste.dynamics.foundation.feature.ReferenceFeature;

/**
 *
 * @since 1.1
 */
public final class FoundationSurfaceStructure {

    private FoundationSurfaceStructure() { }

    @SurfaceStructure
    public static final DynamicSurfaceStructure referenceTo(Class<?> type) {
        return surface -> surface.satisfiedBy(f -> f.referencesTo(type), ReferenceFeature.class);
    }

}
