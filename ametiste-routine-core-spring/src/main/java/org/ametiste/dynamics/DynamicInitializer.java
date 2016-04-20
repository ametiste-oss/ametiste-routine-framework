package org.ametiste.dynamics;

import java.util.function.Consumer;

/**
 *
 * @param <T>
 * @param <C>
 * @since 1.0
 */
public interface DynamicInitializer<T, C> {

    InitializationData apply(T instance, C context);

}

