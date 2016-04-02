package org.ametiste.routine.interfaces.web.task;

import org.ametiste.routine.domain.scheme.SchemeRepository;
import org.ametiste.routine.dsl.application.DynamicTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @since
 */
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private DynamicTaskService taskIssueService;

    @Autowired
    private SchemeRepository schemeRepository;

    @RequestMapping(method = RequestMethod.POST)
    public void issueTask(@RequestBody IssueTaskData issueTaskData) {
        taskIssueService.issueTask(
                issueTaskData.taskSchemeName(),
                issueTaskData.schemeParams(),
                "web-api"
        );
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<String> listTaskSchemas() {
        return schemeRepository.loadTaskSchemeNames();
    }

}
