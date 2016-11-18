package org.ametiste.routine.sdk.mod;

import java.util.Map;

/**
 *
 * @since
 */
public interface ModInfoProvider {

    String sectionName();

    Map<String, ? extends Object> content();

    static ModInfoProvider basic(final String section, final Map<String, ? extends Object> content) {
        return new InternalModInfoProvider(section, content);
    };

}
