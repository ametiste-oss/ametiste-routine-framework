package org.ametiste.routine.sdk.mod.protocol;

import java.util.function.Consumer;

/**
 *
 * @since
 */
public interface QueryDescriptor {

    QueryDescriptor dataSource(String sourceName);

    QueryDescriptor field(String name, String value);

    QueryDescriptor select(String fieldName);

    /**
     * <p>
     * Installs callback for query result into current descriptor,
     * selected field value will be produced to callback as String
     * </p>
     *
     * @param callback
     *
     */
    void accept(Consumer<String> callback);

}
