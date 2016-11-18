package org.ametiste.dynamics.foundation.reflection.structures;

import org.ametiste.dynamics.Surface;
import org.ametiste.dynamics.SurfaceFeature;
import org.ametiste.dynamics.SurfaceStructure;
import org.ametiste.dynamics.Surge;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Represents a method element of a runtime objects graph, that belongs to an object instance.
 *
 * @apiNote This class is structural representation of the {@link Method} class in a context
 * of the Surface Model.
 **
 * @see ObjectInstance
 * @see ClassMethod
 * @since 1.0
 */
@SurfaceStructure(superStructure = {ObjectInstance.class, ClassMethod.class})
public class ObjectInstanceMethod {

    private final ObjectInstance<?> object;
    private final ClassMethod method;

    public ObjectInstanceMethod(final @NotNull ObjectInstance<?> object,
                                final @NotNull ClassMethod method) {
        this.object = object;
        this.method = method;
    }

    /**
     * Proceeds to an actual invocation of a method.
     * <p>The given list of {@link Surge}s will be applied to a {@link Surface}s that formed by parameters
     * of this invocation.
     *
     * @apiNote The list of surges is mechanics to extend process of an paramteres resolving, for example if there is
     * a customization that have need to provide custom values of parameters, this list is the right
     * place to start such customization.
     * <p> At the moment parameters explosion mechanics
     * is implemented at the place, so there is a context of explosion. But in a future this will be moved
     * to a {@link org.ametiste.dynamics.Planar} implementation that will provide such capabilities.
     *
     * @implNote Note, this method will pass <b>each</b> parameter, there is no any special filter.
     * <p>Each surface of a parameter of an invocation that passed to the given list of surges will have
     * type of {@link org.ametiste.dynamics.RightSurface} typed as {@link MethodParameter}.
     *
     * @param surges a list of surges to explode parameters of invocation
     * @param context a context of invocation ( primarily used to explode paramters, see api note )
     * @param <C> a type of invocation context
     *
     * @since 1.0
     */
    // TODO: represent this method as surge or something else, something planar may be?
    @SurfaceFeature
    public <C> void invoke(final @NotNull List<? extends Surge> surge, final @NotNull C context) {
        method.invoke(
            object,
            m -> m,
            parameter -> {
                surge.forEach(s -> s.explode(Surface.rightSurface(parameter), context));
                return parameter;
            }
        );
    }

}
