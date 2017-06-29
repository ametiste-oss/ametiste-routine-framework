package org.ametiste.routine.sdk.mod;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 *
 * @since
 */
class InternalModInfoProvider implements ModInfoProvider {

    private final String section;
    private final Map<String, ? extends Object> content;

    public InternalModInfoProvider(@NotNull final String section,
                                   @NotNull final Map<String, ? extends Object> content) {
        this.section = section;
        // TODO: make read only
        this.content = content;
    }

    @NotNull
    @Override
    public String sectionName() {
        return section;
    }

    @NotNull
    @Override
    public Map<String, ? extends Object> content() {
        return content;
    }

}
