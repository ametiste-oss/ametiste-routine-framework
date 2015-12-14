package org.ametiste.routine.infrastructure.protocol;

import org.ametiste.routine.sdk.mod.protocol.ProtocolDescriptor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @since
 */
public class DirectProtocolDescriptor implements ProtocolDescriptor {

    String protocolType;

    String messageType;

    final Map<String, Map<String, String>> params = new HashMap<>();

    Consumer<Map<String, String>> callback;

    public DirectProtocolDescriptor() {

    }

    @Override
    public ProtocolDescriptor protocol(String protocolType) {
        this.protocolType = protocolType;
        return this;
    }

    @Override
    public ProtocolDescriptor message(String messageType) {
        this.messageType = messageType;
        return this;
    }

    @Override
    public ProtocolDescriptor param(String name, String value) {
        params.computeIfAbsent(name,
                k -> new HashMap<String, String>()).put("value", value);
        return this;
    }

    @Override
    public ProtocolDescriptor param(String name, Map<String, String> value) {
        params.computeIfAbsent(name,
                k -> new HashMap<String, String>()).putAll(value);
        return this;
    }

    @Override
    public void accept(Consumer<Map<String, String>> callback) {
        this.callback = callback;
    }

}
