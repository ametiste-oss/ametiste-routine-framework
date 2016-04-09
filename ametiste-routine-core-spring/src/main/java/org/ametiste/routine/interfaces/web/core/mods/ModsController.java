package org.ametiste.routine.interfaces.web.core.mods;

import org.ametiste.routine.domain.ModReportRepository;
import org.ametiste.routine.infrastructure.mod.ModRegistry;
import org.ametiste.routine.interfaces.web.mod.ModDescriptionData;
import org.ametiste.routine.sdk.mod.ModInfoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @since
 */
@RestController
@RequestMapping("/core/mods")
public class ModsController {

    @Autowired
    private ModRegistry modRegistry;

    @RequestMapping(method = RequestMethod.GET)
    public List<ModDescriptionData> listModsDescriptions() {
        return modRegistry.loadMods().stream()
            .map(m -> new ModDescriptionData(
                    m.getName(),
                    m.getVersion(),
                    createSections(m.getAttributes(), m.getProviders())
            ))
            .collect(Collectors.toList());
    }

    private Map<String, Map<String, ? extends Object>> createSections(final Map<String, String> attributes, final List<ModInfoProvider> providers) {

        final HashMap<String, Map<String, ? extends Object>> sections = new HashMap<>();

        sections.put("attributes", attributes);

        providers.forEach(p ->
            sections.put(p.sectionName(), p.content())
        );

        return sections;
    }

}
