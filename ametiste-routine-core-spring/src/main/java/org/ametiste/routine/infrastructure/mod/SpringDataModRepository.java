package org.ametiste.routine.infrastructure.mod;

import org.ametiste.routine.infrastructure.mod.jpa.JPAModDataRepository;
import org.ametiste.routine.infrastructure.mod.jpa.data.ModData;
import org.ametiste.routine.infrastructure.mod.jpa.data.ModDataKey;

import java.util.Optional;

/**
 *
 * @since
 */
public class SpringDataModRepository implements ModRepository {

    private final JPAModDataRepository modDataRepository;

    public SpringDataModRepository(JPAModDataRepository modDataRepository) {
        this.modDataRepository = modDataRepository;
    }

    @Override
    public void saveModProperty(String modId, String name, String value) {
         modDataRepository.save(ModData.create(modId, name, value));
    }

    @Override
    public Optional<String> loadModProperty(String modId, String name) {

        final ModData modData = modDataRepository
                .findOne(ModDataKey.create(modId, name));

        if (modData == null) {
            return Optional.empty();
        } else {
            return Optional.of(modData.value);
        }
    }

}
