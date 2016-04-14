package org.ametiste.routine.infrastructure.protocol.moddata;

/**
 * Simple data conversion interface, used as plugin interface to adopt platform-specific
 * solutions for types convertion.
 *
 * @since 1.1
 */
public interface DataConverter {

    /**
     * Just converst the given value to the object of the specified type.
     *
     * @param value source value, can be null.
     * @param type  class of target type
     * @param <T> target type
     * @return object of the target type, can be null
     */
    <T> T convert(Object value, Class<T> type);

}
