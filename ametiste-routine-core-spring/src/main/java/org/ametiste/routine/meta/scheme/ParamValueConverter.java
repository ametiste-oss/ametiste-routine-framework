package org.ametiste.routine.meta.scheme;

/**
 * Scheme parameter converter, converts given
 * value to the string representation.
 *
 * @since 1.1
 */
public interface ParamValueConverter {

    String convert(Object value);

}
