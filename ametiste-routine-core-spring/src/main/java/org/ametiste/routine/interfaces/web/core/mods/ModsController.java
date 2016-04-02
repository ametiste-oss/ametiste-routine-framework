package org.ametiste.routine.interfaces.web.core.mods;

import org.ametiste.routine.domain.ModReportRepository;
import org.ametiste.routine.infrastructure.mod.ModRegistry;
import org.ametiste.routine.interfaces.web.mod.ModDescriptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        return modRegistry.loadMods().stream().map(
                m -> new ModDescriptionData(m.getName(), m.getVersion(), m.getAttributes())
        ).collect(Collectors.toList());
    }

}
