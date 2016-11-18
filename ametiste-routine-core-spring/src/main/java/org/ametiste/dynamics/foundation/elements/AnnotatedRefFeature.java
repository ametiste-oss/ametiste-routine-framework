package org.ametiste.dynamics.foundation.elements;

import org.ametiste.dynamics.foundation.reflection.structures.Annotated;
import org.ametiste.dynamics.foundation.reflection.structures.Reference;

/**
 * Type alias to hold type of element that {@link Annotated} as well as {@link Reference}.
 *
 * <p>Designed to hide low-level runtime details of a surface implemenation and represent
 * a composite features set as a single type.
 *
 * @param <R> type on which this element holds reference
 *
 * @see AnnotatedRef
 * @see AnnotatedRefSurge
 * @since 1.0
 */
public interface AnnotatedRefFeature<R> extends Annotated, Reference<R> { }
