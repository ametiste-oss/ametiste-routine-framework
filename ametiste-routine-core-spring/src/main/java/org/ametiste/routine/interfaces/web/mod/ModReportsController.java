package org.ametiste.routine.interfaces.web.mod;

import org.ametiste.routine.domain.ModReportRepository;
import org.ametiste.routine.infrastructure.mod.ModRegistry;
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
public class ModReportsController {

    @Autowired
    private ModReportRepository modReportRepository;

    @RequestMapping(value = "/{modId}/reports", method = RequestMethod.GET)
    public List<Map<String, String>> listModReports(@PathVariable("modId") String modId) {
        return modReportRepository.loadModReports(modId);
    }

    @RequestMapping(value = "/all/reports", method = RequestMethod.GET)
    public List<Map<String, String>> listModReports() {
        return modReportRepository.loadModReports();
    }

}
