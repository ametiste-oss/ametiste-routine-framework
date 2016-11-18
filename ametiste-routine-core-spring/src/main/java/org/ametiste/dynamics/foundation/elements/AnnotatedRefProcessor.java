package org.ametiste.dynamics.foundation.elements;

import org.ametiste.dynamics.RightSurface;
import org.ametiste.dynamics.Surge;
import org.ametiste.dynamics.foundation.reflection.structures.AnnotatedDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Base class to define a processor for an {@link AnnotatedRef} elements.
 *
 * <p>This class is foundation to build elements processors that have declared annotations,
 * in the defined context.
 *
 * @apiNote This class is defined to hide model abstraction of the  {@link AnnotatedRefSurge} from
 * the clients. But if the clinet wants to have low-lewel acces to the <i>Surface Model</i> elements it may have it,
 * the {@link AnnotatedRef} is the {@link RightSurface}. See {@link AnnotatedRef} for details
 * on hidding of model abstractions.
 *
 * @implNote This class is desgined to be foundation for elements processors, subclasses
 * must provide an {@link AnnotationSpec} for which this processor will be bound,
 * must define {@link #resolveValue(AnnotatedRef, Object)} method to resolve references.
 * <p>Subclasses may have access to the provided <i>annotation specification</i> by the field {@link #annotationSpec}.
 *
 * @param <A> an annotation specification that used to match elements for which this processor should be invoked
 * @param <R> a type on which the {@link AnnotatedRef} element holds a reference
 * @param <C> a context in which this processor will be executed
 *
 * @see AnnotationSpec
 * @see AnnotatedRef
 * @see AnnotatedRefSurge
 * @since 1.0
 */
public abstract class AnnotatedRefProcessor<A extends AnnotationSpec, R, C>
        implements AnnotatedRefSurge<R, AnnotatedRefFeature<R>, C> {

    /**
     * A factory to create bound {@link AnnotationSpec} from the given {@link AnnotatedDescriptor}.
     *
     * @apiNote This filed may be used by subclasses to have access to the bound annotation specification,
     * since there is no singletone specification, the subclass must create new instance of the specification
     * each time when required.
     *
     * @since 1.0
     */
    // TODO: I want to pass this object as paramter to the resolve method
    protected final Function<AnnotatedDescriptor, A> annotationSpec;

    /**
     * Creates new element proccessor that bound to the given annotation specification.
     *
     * @apiNote In fact, an annotation specification will be resolved in the runtime by creating a
     * new instance of the spec when required. So there is the way to define such processer that
     * bound to a set of annotations that shares a common type. In such case the class must be defined by
     * a general type of annotation and a factory function must resolve
     * the given {@link AnnotatedDescriptor} to the concrete annotation type that is extension of the general type.
     *
     * @param annotationSpec a factory function to create annotation specification bound to this
     *                       and used by this processor, must be not null
     *
     * @since 1.0
     */
    protected AnnotatedRefProcessor(final @NotNull Function<AnnotatedDescriptor, A> annotationSpec) {
        this.annotationSpec = annotationSpec;
    }

    /**
     * Resolves the value of the given element in the given context, if this element has declared
     * annotation that defined by an {@link AnnotationSpec} that is bound to this elements processor.
     *
     * @apiNote This method implements {@link Surge} interface, that is part of low-level model. Subclasses
     * must not take care on this method, the method is desinged to hide low-level model abstractions from clinets.
     * <p>Subclasses must define the {{@link #resolveValue(AnnotatedRef, Object)}} method only.
     *
     * @param element an element to resolve value for, must be not null
     * @param context a context in which value for element will be resolved, must be not null
     *
     * @since 1.0
     */
    @Override
    public final void explode(final @NotNull RightSurface<AnnotatedRefFeature<R>> element, final @NotNull C context) {
        element.depictRight(
            ref -> ref.hasAnnotations(annotationSpec),
            AnnotatedRef::new,
            ref -> resolveValue(ref, context)
        );
    }

    /**
     * Resolves the value of the given element in the given context, if this element has declared
     * annotation that defined by an {@link AnnotationSpec} that is bound to this elements processor.
     *
     * @param element an element to resolve value for, must be not null
     * @param context a context in which value for element will be resolved, must be not null
     *
     * @see AnnotatedRef
     * @since 1.0
     */
    abstract protected void resolveValue(final @NotNull AnnotatedRef<R> element, final @NotNull C context);

}