package org.ametiste.routine.infrastructure.mod.jpa.data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ModDataKey implements Serializable {

    @Column(name = "mod_id", nullable = false)
    public String modId;

    @Column(name = "name", nullable = false)
    public String name;

    /** getters and setters **/

    public static ModDataKey create(String modId, String name) {

        final ModDataKey modDataKey = new ModDataKey();
        modDataKey.modId = modId;
        modDataKey.name = name;

        return modDataKey;
    }
}