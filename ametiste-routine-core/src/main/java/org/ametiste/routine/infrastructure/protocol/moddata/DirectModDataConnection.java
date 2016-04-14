package org.ametiste.routine.infrastructure.protocol.moddata;

import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.routine.domain.ModRepository;
import org.ametiste.routine.sdk.protocol.moddata.ModDataProtocol;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 *
 * @since
 */
public class DirectModDataConnection implements ModDataProtocol, DirectModDataConnectionMetrics {

    private final String clientId;

    private final ModRepository modRepository;

    private final DataConverter converter;

    public DirectModDataConnection(final String clientId,
                                   final ModRepository modRepository,
                                   final DataConverter converter) {
        this.clientId = clientId;
        this.modRepository = modRepository;
        this.converter = converter;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    @Timeable(name = OVERAL_LOAD_TIMING)
    @Timeable(name = CLIENTS_PREFIX, nameSuffixExpression = CLIENT_LOAD_TIMING)
    @Deprecated
    public Optional<String> loadData(final String name) {
        return loadData(name, String.class);
    }

    @Override
    public <T> Optional<T> loadData(final String name, final Class<T> type) {
        return modRepository.loadModProperty(clientId, name)
                .map(s -> Optional.ofNullable(converter.convert(s, type)))
                .orElse(Optional.<T>empty());
    }

    @Override
    @Timeable(name = OVERAL_STORE_TIMING)
    @Timeable(name = CLIENTS_PREFIX, nameSuffixExpression = CLIENT_STORE_TIMING)
    public void storeData(final String name, final Object value) {
        modRepository.saveModProperty(clientId, name, converter.convert(notNull(value), String.class));
    }

    private Object notNull(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Mod data value can't be bull.");
        }
        return object;
    }

}
