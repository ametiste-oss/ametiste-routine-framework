package org.ametiste.routine.stat.interfaces.web;

import org.ametiste.routine.stat.CoreStatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 *
 * @since
 */
@RestController
@RequestMapping("/core/stat")
public class StatController {

    @Autowired
    public CoreStatRepository coreStatRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Long> showCoreStat() {
        return coreStatRepository.loadStat();
    }

}
