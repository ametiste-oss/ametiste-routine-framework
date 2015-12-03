package org.ametiste.routine.infrastructure.mod.jpa.data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @since
 */
@Entity
@Table(name = "ame_routine_mod_data")
public class ModData {

    @EmbeddedId
    public ModDataKey modDataKey;

    public String value;

    public static ModData create(String modId, String name, String value) {

        final ModData modData = new ModData();
        modData.modDataKey = ModDataKey.create(modId, name);
        modData.value = value;

        return modData;
    }

}
