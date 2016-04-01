package org.ametiste.routine.interfaces.web;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.domain.scheme.SchemeRepository;
import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.interfaces.taskdsl.service.DynamicTaskService;
import org.ametiste.routine.interfaces.web.data.IssueTaskData;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;
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
