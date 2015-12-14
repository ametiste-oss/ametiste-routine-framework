package org.ametiste.routine.sdk.mod;

import org.ametiste.routine.sdk.mod.protocol.ProtocolGateway;

import java.util.Optional;

/**
 *
 * @since
 */
public class ModDataClient implements ModDataGateway {

    private final ProtocolGateway protocol;

    public ModDataClient(ProtocolGateway protocol) {
        this.protocol = protocol;
    }

    @Override
    public void storeModData(String name, String value) {
        protocol.session(ModDataProtocol.class)
                .storeData(name, value);
    }

    @Override
    public Optional<String> loadModData(String name) {
        return protocol.session(ModDataProtocol.class)
                .loadData(name);
    }

    // Shortcut methods section

    @Override
    public void storeModData(String name, Integer value) {
        storeModData(name, Integer.toString(value));
    }

    @Override
    public void storeModData(String name, Long value) {
        storeModData(name, Long.toString(value));
    }

    @Override
    public void storeModData(String name, Boolean value) {
        storeModData(name, Boolean.toString(value));
    }

    @Override
    public Optional<Integer> loadModDataInt(String name) {
        return loadModData(name).map(Integer::valueOf);
    }

    @Override
    public Optional<Long> loadModDataLong(String name) {
        return loadModData(name).map(Long::valueOf);
    }

    @Override
    public Optional<Boolean> loadModDataBool(String name) {
        return loadModData(name).map(Boolean::valueOf);
    }

}
