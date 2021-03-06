package org.ametiste.routine.infrastructure.protocol.moddata;

import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.routine.domain.ModRepository;
import org.ametiste.routine.sdk.protocol.moddata.ModDataProtocol;

import java.util.Optional;

/**
 *
 * @since
 */
public class DirectModDataConnection implements ModDataProtocol, DirectModDataConnectionMetrics {

    private final String clientId;

    private final ModRepository modRepository;

    public DirectModDataConnection(String clientId, ModRepository modRepository) {
        this.clientId = clientId;
        this.modRepository = modRepository;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    @Timeable(name = OVERAL_LOAD_TIMING)
    @Timeable(name = CLIENTS_PREFIX, nameSuffixExpression = CLIENT_LOAD_TIMING)
    public Optional<String> loadData(String name) {
        return modRepository.loadModProperty(clientId, name);
    }

    @Override
    @Timeable(name = OVERAL_STORE_TIMING)
    @Timeable(name = CLIENTS_PREFIX, nameSuffixExpression = CLIENT_STORE_TIMING)
    public void storeData(String name, String value) {
        modRepository.saveModProperty(clientId, name, value);
    }

}
