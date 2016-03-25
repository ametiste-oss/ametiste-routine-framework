package org.ametiste.routine.interfaces.web;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.domain.scheme.TaskSchemeRepository;
import org.ametiste.routine.interfaces.web.data.IssueTaskData;
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
    private TaskIssueService taskIssueService;

    @Autowired
    private TaskSchemeRepository taskSchemeRepository;

    @RequestMapping(method = RequestMethod.POST)
    public void issueTask(@RequestBody IssueTaskData issueTaskData) {
        // TODO: I guess we need midleware service to convert public shema names to classes
        throw new UnsupportedOperationException("Unsupported yet.");
//        taskIssueService.issueTask(
//                issueTaskData.taskSchemeName(),
//                issueTaskData.schemeParams(),
//                "web-api"
//        );
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<String> listTaskSchemas() {
        return taskSchemeRepository.loadSchemeNames();
    }

}
