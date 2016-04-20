package org.ametiste.routine.dsl.application;

import org.ametiste.dynamics.Surface;
import org.ametiste.dynamics.foundation.BaseSurface;

import static org.ametiste.lang.ReadOnlyCollections.readonlyMap;

/**
 *
 * @since 1.1
 */
public class SpringApplicationSurface extends BaseSurface {

    public SpringApplicationSurface(final Surface enclosingSurface) {
        super(
            enclosingSurface,
            readOnlyMap(

            )
        );
    }
}
