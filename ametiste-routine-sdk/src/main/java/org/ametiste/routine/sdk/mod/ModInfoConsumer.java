package org.ametiste.routine.sdk.mod;

import java.util.List;
import java.util.Map;

/**
 *
 * @since
 */
public interface ModInfoConsumer {

    void modInfo(final String modName,
                 final String version,
                 final Map<String, String> attributes,
                 final List<ModInfoProvider> infoProviders);

}
