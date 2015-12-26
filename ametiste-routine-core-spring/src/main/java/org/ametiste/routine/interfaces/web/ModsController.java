package org.ametiste.routine.interfaces.web;

import org.ametiste.routine.domain.ModReportRepository;
import org.ametiste.routine.infrastructure.mod.ModRegistry;
import org.ametiste.routine.interfaces.web.data.ModData;
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
@RequestMapping("/mod")
public class ModsController {

    @Autowired
    private ModRegistry modRegistry;

    @Autowired
    private ModReportRepository modReportRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<ModData> listModData() {
        return modRegistry.loadMods().stream().map(
                m -> new ModData(m.getName(), m.getVersion())
        ).collect(Collectors.toList());
    }

    @RequestMapping(value = "/{modId}/reports", method = RequestMethod.GET)
    public List<Map<String, String>> listModReports(@PathVariable("modId") String modId) {
        return modReportRepository.loadModReports(modId);
    }

    @RequestMapping(value = "/all/reports", method = RequestMethod.GET)
    public List<Map<String, String>> listModReports() {
        return modReportRepository.loadModReports();
    }

}
