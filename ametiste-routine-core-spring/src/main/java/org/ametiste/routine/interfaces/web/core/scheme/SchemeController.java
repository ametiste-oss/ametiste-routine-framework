package org.ametiste.routine.interfaces.web.core.scheme;

import org.ametiste.routine.domain.scheme.SchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @since
 */
@RestController
@RequestMapping("/core/schemes")
public class SchemeController {

    @Autowired
    private SchemeRepository schemeRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, List<String>> listSchemes() {

        final HashMap<String, List<String>> schemaNames = new HashMap<>();

        schemaNames.put("operation", schemeRepository.loadOperationSchemeNames());
        schemaNames.put("task", schemeRepository.loadTaskSchemeNames());

        return schemaNames;
    }

}
