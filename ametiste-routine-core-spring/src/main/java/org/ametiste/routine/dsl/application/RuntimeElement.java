package org.ametiste.routine.dsl.application;

import java.lang.annotation.Annotation;
import java.util.function.Function;

/**
 * Depicts any element of a runtime, such a dsl-operation field, a dsl method parameters and so one; providing its
 * description in a general terms.
 *
 * <p>
 *     At the moment of 1.1, it is used as base abstraction for value providers mechanism to generalize concept
 *     of a runtime part that belongs to {@code Task DSL Module} and values resolving.
 *
 * <p>
 *     But, it can be used to abstract other aspects of runtime beyond the values resolving. In general,
 *     this interface describes any part of the framework at a runtime.
 *
 * @see RuntimeElementValueProvider
 * @since 1.1
 */
public interface RuntimeElement {

    <T> T mapName(Function<String, T> transform);

    boolean mayHaveValueOf(Class<?> valueType);

    boolean isAnnotatedBy(Class<? extends Annotation> annotation);

    <S extends Annotation, T> T annotationValue(Class<S> annotation, Function<S, T> valueProvider);

    Class<?> valueType();

}
