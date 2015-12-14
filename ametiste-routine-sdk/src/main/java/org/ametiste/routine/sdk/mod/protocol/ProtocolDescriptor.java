package org.ametiste.routine.sdk.mod.protocol;

import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @since
 */
public interface ProtocolDescriptor {

    ProtocolDescriptor protocol(String protocolType);

    ProtocolDescriptor message(String messageType);

    ProtocolDescriptor param(String name, String value);

    ProtocolDescriptor param(String name, Map<String, String> value);

    void accept(Consumer<Map<String, String>> callback);

}
