package org.ametiste.dynamics;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Base implementation of a {@link LeftSurface}.
 *
 * @apiNote This class is open to extensions, feel free to use this class as starting point when you need
 * a special {@code LeftSurface}.
 *
 * @see RightSurface
 * @see Surface
 * @since 1.0
 */
public class BaseLeftSurface<E> extends BaseSurface<E, Object> implements LeftSurface<E> {

    public BaseLeftSurface(final E enclosingSurface, final Function<E, Object> subSurface) {
        super(enclosingSurface, subSurface);
    }

    public BaseLeftSurface(final Supplier<Object> subSurface) {
        super(subSurface);
    }

    public BaseLeftSurface(final E enclosingSurface) {
        super(enclosingSurface);
    }

}
