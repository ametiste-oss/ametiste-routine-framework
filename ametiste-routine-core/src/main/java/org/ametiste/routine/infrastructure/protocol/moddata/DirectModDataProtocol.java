package org.ametiste.routine.infrastructure.protocol.moddata;

import org.ametiste.routine.domain.ModRepository;
import org.ametiste.routine.sdk.mod.ModDataProtocol;

import java.util.Optional;

/**
 *
 * @since
 */
public class DirectModDataProtocol implements ModDataProtocol {

    private final String modId;
    private final ModRepository modRepository;

    public DirectModDataProtocol(String modId, ModRepository modRepository) {
        this.modId = modId;
        this.modRepository = modRepository;
    }

    @Override
    public Optional<String> loadData(String name) {
        return modRepository.loadModProperty(modId, name);
    }

    @Override
    public void storeData(String name, String value) {
        modRepository.saveModProperty(modId, name, value);
    }
}