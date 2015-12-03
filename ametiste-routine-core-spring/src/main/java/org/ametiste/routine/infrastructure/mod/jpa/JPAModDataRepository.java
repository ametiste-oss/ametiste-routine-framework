package org.ametiste.routine.infrastructure.mod.jpa;

import org.ametiste.routine.infrastructure.mod.jpa.data.ModData;
import org.ametiste.routine.infrastructure.mod.jpa.data.ModDataKey;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @since
 */
public interface JPAModDataRepository extends CrudRepository<ModData, ModDataKey> {



}
