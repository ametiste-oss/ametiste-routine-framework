package org.ametiste.dynamics;

import java.util.function.Predicate;

/**
 *
 *
 * @since 1.1
 * @see SurfaceElement
 */
public interface DynamicSurfaceStructure<S extends Surface> extends Predicate<S> {

    @Override
    boolean test(S feature);

}
