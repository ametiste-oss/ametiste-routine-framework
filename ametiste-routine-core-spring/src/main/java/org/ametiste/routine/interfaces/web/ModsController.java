package org.ametiste.routine.interfaces.web;

import org.ametiste.routine.infrastructure.mod.ModRegistry;
import org.ametiste.routine.interfaces.web.data.ModData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @since
 */
@RestController
@RequestMapping("/mods")
public class ModsController {

    @Autowired
    private ModRegistry modRegistry;

    @RequestMapping(method = RequestMethod.GET)
    public List<ModData> listModData() {
        return modRegistry.loadMods().stream().map(
                m -> new ModData(m.getName(), m.getVersion())
        ).collect(Collectors.toList());
    }

}
