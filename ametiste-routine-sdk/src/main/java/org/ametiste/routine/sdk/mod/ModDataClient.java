package org.ametiste.routine.sdk.mod;

import org.ametiste.routine.sdk.mod.protocol.GatewayMappers;
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

    }

    @Override
    public void storeModData(String name, Integer value) {

    }

    @Override
    public void storeModData(String name, Long value) {

    }

    @Override
    public void storeModData(String name, Boolean value) {

    }

    @Override
    public Optional<String> loadModData(String name) {

        final String value = protocol.session(p ->
                p.protocol("mod-data")
                        .message("query-data")
                        .param("query-data.with-name", name)
        ).collect(
                GatewayMappers.asString("value")
        );

        return Optional.ofNullable(value);
    }

    @Override
    public Optional<Integer> loadModDataInt(String name) {
        return Optional.ofNullable(
                Integer.valueOf(loadModData(name).get())
        );
    }

    @Override
    public Optional<Long> loadModDataLong(String name) {
        return Optional.ofNullable(
                Long.valueOf(loadModData(name).get())
        );
    }

    @Override
    public Optional<Boolean> loadModDataBool(String name) {
        return Optional.ofNullable(
                Boolean.valueOf(loadModData(name).get())
        );
    }

}
