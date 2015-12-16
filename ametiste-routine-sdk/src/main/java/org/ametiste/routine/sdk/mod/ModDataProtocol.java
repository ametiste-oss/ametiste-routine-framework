package org.ametiste.routine.sdk.mod;

import org.ametiste.laplatform.protocol.Protocol;

import java.util.Optional;

/**
 *
 * @since
 */
public interface ModDataProtocol extends Protocol {

    Optional<String> loadData(String name);

    void storeData(String name, String value);

}
