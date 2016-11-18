package org.ametiste.dynamics.foundation.elements;

import org.ametiste.dynamics.RightSurface;
import org.ametiste.dynamics.Surface;
import org.ametiste.dynamics.BaseRightSurface;
import org.ametiste.dynamics.foundation.reflection.structures.Annotated;
import org.ametiste.dynamics.foundation.reflection.structures.AnnotatedDescriptor;
import org.ametiste.dynamics.foundation.reflection.structures.Reference;

import java.util.function.Function;

/**
 * Represents {@link Annotated} element of the runtime that is {@link Reference}. Usually
 * used to provide a value to some object on the basis of its annotations and type analysis.
 *
 * <p>Note, along with the convenient high-level interface objects of this class also provides
 * low-level {@link Surface} interface that can be used in a case where additional capabilities
 * are required.
 *
 * <p>Object of this class are mainly used by the {@link AnnotatedRefProcessor} and implemenatations.
 *
 * <h5>Examples</h5>
 *
 * Next snippets shows how to use {@code AnnotatedRef} instance, {@code element} is {@code AnnotatedRef}.
 *
 * <p>Example of high-level interface usage:
 *
 * <pre>
 * <code>
 *
 * if (element.isRefereceTo(UUID.class)) {
 *      value = operationId;
 * } else if (element.isReferenceTo(String.class)) {
 *      value = operationId.toString();
 * } else {
 *      throw new IllegalStateException("Element must have valueType of java.util.UUID or java.lang.String.");
 * }
 *
 * element.feature(ref -> ref.referencesTo(value));
 * </code>
 * </pre>
 *
 * <p>Same may be achieved using low-level surface interface, example of low-level interface usage:
 *
 * <pre>
 * <code>
 *
 * if (element.map(f -> f.ofType(UUID.class))) {
 *      value = operationId;
 * } else if (element.map(f -> f.ofType(String.class))) {
 *      value = operationId.toString();
 * } else {
 *      throw new IllegalStateException("Element must have valueType of java.util.UUID or java.lang.String.");
 * }
 *
 * element.feature(ref -> ref.referencesTo(value));
 * </code>
 * </pre>
 *
 * @implNote This class defines a special type of a {@link Surface} that used to hide math
 *           abstraction from an application level. So, for a some reasons, it may be referenced
 *           as <i>application-level structure</i>, informal it's means that this
 *           surface is more concrete than others, but formal it's means nothing special.
 *           See formal model definition for details.
 *
 *           <p>From the surface model point of view, this objects are
 *           {@link RightSurface} depicted by set of {@link AnnotatedRefFeature}s.
 *
 * @apiNote This class is <i>final</i> so there is complete hierarchy of annotated references.
 *          <p> Constructor of this class have default visisbility level because
 *          the class is designed to provide a convenient high-level interface of the module,
 *          so only an internal clients are allowed to use create instances of the class object.
 *          External clients are allowed only to use these objects as element of the module interface.
 *
 * @see Surface
 * @see Annotated
 * @see Reference
 * @see AnnotatedRefFeature
 * @since 1.0
 */
public final class AnnotatedRef<S> extends BaseRightSurface<AnnotatedRefFeature<S>> {

    /**
     * Creates new {@link AnnotatedRef} surface over the given features set.
     *
     * @param refFeature features set to depict new surface.
     */
    AnnotatedRef(final AnnotatedRefFeature<S> refFeature) {
        super(() -> refFeature);
    }

    /**
     * Checks that a represented element is a reference to the given type.
     *
     * @param type type to be checked
     * @return {@code true} if an element is a reference, otherwise {@code false}
     * @see Reference#ofType(Class)
     *
     * @apiNote It's equal to a mapping of surface feature like an
     * <i>element.map(f -> f.ofType(type))==true</i>, where the {@code element} is a surface.
     *
     * @since 1.0
     */
    public final boolean isRefeneceTo(final Class<?> type) {
        return map(f -> f.ofType(type));
    }

    /**
     * Provide the given value to a represented element.
     *
     * @param ref new value of the reference element
     * @see Reference#referencesTo(Object)
     *
     * @apiNote It's equal to a surface feature invocation like an
     * <i>element.feature(f -> f.referenceTo(ref))</i>, where the {@code element} is a surface.
     *
     * @since 1.0
     */
    public final void provideValue(final S ref) {
        feature(f -> f.referencesTo(ref));
    }

    /**
     * Returns a type of a represented element.
     *
     * @return type of element
     *
     * @apiNote It's equal to a mapping of surface feature like an
     * <i>element.map(f -> f.type())</i>, where the {@code element} is a surface.
     *
     * @since 1.0
     */
    public Class<S> type() {
        return map(f -> f.type());
    }

    /**
     * Creates instance of {@link AnnotationSpec} created by the given function. The instance
     * will be bound to an internal {@link AnnotatedDescriptor} object to handle element declared annotations.
     *
     * @param <T> type of used {@code AnnotationSpec}
     * @return annotation handler of the given type that bound to annotations of a represented element.
     *
     * @apiNote It's equal to a mapping of surface feature like an
     * <i>element.map(f -> f).annotation(handler)</i>, where the {@code element} is a surface.
     *
     * @since 1.0
     */
    public <T extends AnnotationSpec> T annotation(final Function<AnnotatedDescriptor, T> handler) {
        // NOTE: this transformation just extracts AnnotatedRefFeature
        // to allow call annotation(handler) method
        return map(f -> f).annotation(handler);
    }

}
