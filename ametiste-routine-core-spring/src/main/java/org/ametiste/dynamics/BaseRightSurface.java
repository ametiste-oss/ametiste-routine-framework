package org.ametiste.dynamics;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Base implementation of a {@link RightSurface}.
 *
 * @apiNote This class is open to extensions, feel free to use this class as starting point when you need
 * a special {@code RightSurface}.
 *
 * @see RightSurface
 * @see Surface
 * @since 1.0
 */
public class BaseRightSurface<S> extends BaseSurface<Object, S> implements RightSurface<S> {

    public BaseRightSurface(final Object enclosingSurface, final Function<Object, S> subSurface) {
        super(enclosingSurface, subSurface);
    }

    public BaseRightSurface(final Supplier<S> subSurface) {
        super(subSurface);
    }

    public BaseRightSurface(final Object enclosingSurface) {
        super(enclosingSurface);
    }

}
