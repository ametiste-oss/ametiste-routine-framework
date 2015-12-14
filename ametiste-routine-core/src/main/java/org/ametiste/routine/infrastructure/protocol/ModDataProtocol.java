package org.ametiste.routine.infrastructure.protocol;

import org.ametiste.routine.domain.ModRepository;
import org.ametiste.routine.sdk.mod.protocol.Protocol;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @since
 */
public class ModDataProtocol implements Protocol {

    private final ModRepository modRepository;

    public ModDataProtocol(ModRepository modRepository) {
        this.modRepository = modRepository;
    }

    @Override
    public void message(String messageType, Map<String, Map<String, String>> params,
                        Consumer<Map<String, String>> callback) {

        if (messageType.equals("query-data")) {
            final Optional<String> value = modRepository.loadModProperty("MOD-ID",
                    params.get("query-data.with-name").get("value"));
            callback.accept(Collections.singletonMap("value", value.orElse(null)));
        }

    }

}
