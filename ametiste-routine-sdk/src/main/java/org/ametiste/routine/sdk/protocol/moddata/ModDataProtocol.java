package org.ametiste.routine.sdk.protocol.moddata;

import org.ametiste.laplatform.sdk.protocol.Protocol;

import java.util.Optional;

/**
 *
 * @since 1.0
 */
public interface ModDataProtocol extends Protocol {

    /**
     * @deprecated use {@link #loadData(String, Class)} instead, even for strings, yep. This method
     * will be removed at 1.2.
     */
    @Deprecated
    Optional<String> loadData(String name);

    /**
     * @since 1.1
     */
    <T> Optional<T> loadData(String name, Class<T> type);

    /**
     * @since 1.0
     */
    void storeData(String name, Object value);

}
