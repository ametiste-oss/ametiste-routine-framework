package org.ametiste.routine.infrastructure.protocol.moddata;

import static javafx.scene.input.KeyCode.T;

/**
 *
 * @since
 */
public interface DataConverter {

    <T> T convert(Object value, Class<T> type);

}
