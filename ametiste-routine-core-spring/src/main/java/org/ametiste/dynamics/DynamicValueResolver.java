package org.ametiste.dynamics;

/**
 *
 * @since 1.0
 */
// TODO: naming
public interface DynamicValueResolver<T, S, C> {

    DynamicInitializer<S, C> resolve(T type);

}
