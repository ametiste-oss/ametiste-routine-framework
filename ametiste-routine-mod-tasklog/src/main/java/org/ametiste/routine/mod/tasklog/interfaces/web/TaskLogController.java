package org.ametiste.routine.mod.tasklog.interfaces.web;

import org.ametiste.routine.domain.log.NoticeEntry;
import org.ametiste.routine.domain.log.TaskLogEntry;
import org.ametiste.routine.domain.log.TaskLogNotFoundException;
import org.ametiste.routine.domain.log.TaskLogRepository;
import org.ametiste.routine.mod.tasklog.interfaces.web.resources.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 *
 * @since
 */
@RestController
@RequestMapping("/log")
public class TaskLogController {

    @Autowired
    private TaskLogRepository taskLogRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // TODO: runtime exceptions handling

    @ExceptionHandler(TaskLogNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Requested Task Log Not Found")
    public void handleTaskLogNotFoundException(TaskLogNotFoundException e) {
        if (logger.isDebugEnabled()) {
            logger.debug("Can't find requested task log.", e);
        }
    }

    @RequestMapping(value="/count", method = RequestMethod.GET)
    // TODO: make me restful
    public Map provideLogEntriesCount(@RequestParam("status") String[] byStatus) {

        Map<String, Integer> counts = new HashMap<>();

        for (String status : byStatus) {
            final int entriesCount = taskLogRepository.countEntriesByStatus(status);
            counts.put(status, entriesCount);
        }

        return counts;
    }

    @RequestMapping(method = RequestMethod.GET)
    public TaskLogEntryResources provideLogEntries(
            @RequestParam(value = "status", required = false) List<String> byStatus,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {

        final List<TaskLogEntryResource> entries = new ArrayList<>();

        if (byStatus == null) {
            taskLogRepository.findEntries().stream()
                    .map(this::assembleLogEntryResource)
                    .forEach(entries::add);
        } else {
            byStatus.stream()
                    .map((s) -> taskLogRepository.findEntries(s, offset, limit))
                    .flatMap(List::stream)
                    .map(this::assembleLogEntryResource)
                    .forEach(entries::add);
        }

        final TaskLogEntryResources resources = new TaskLogEntryResources(entries);

        resources.add(
                linkTo(methodOn(TaskLogController.class)
                                .provideLogEntries(byStatus, offset, limit)).withSelfRel()
        );

        return resources;
    }

    @RequestMapping(value = "/task/{taskId}", method = RequestMethod.GET)
    public TaskLogEntryResource provideTaskLog(@PathVariable("taskId") UUID taskId) {
        final TaskLogEntry taskLogEntry = taskLogRepository.findTaskLog(taskId);
        return assembleLogEntryResource(taskLogEntry);
    }

    private TaskLogEntryResource assembleLogEntryResource(TaskLogEntry taskLogEntry) {

        final TaskLogEntryContent content = new TaskLogEntryContent(
                taskLogEntry.getTaskId(),
                taskLogEntry.getState(),
                taskLogEntry.getNotices().stream()
                        .map(this::createNoticeDTO)
                        .collect(Collectors.toList()),
                taskLogEntry.getLines().stream()
                        .map((o) -> {
                            return new OperationLogDTO(
                                    o.getOperationId(),
                                    o.getState(),
                                    o.getLine(),
                                    o.getNotices().stream()
                                            .map(this::createNoticeDTO)
                                            .collect(Collectors.toList()));
                        }).collect(Collectors.toList())
        );

        final TaskLogEntryResource resource = new TaskLogEntryResource(content);

        resource.add(
                linkTo(methodOn(TaskLogController.class)
                        .provideTaskLog(taskLogEntry.getTaskId())).withSelfRel()
        );

        return resource;
    }

    private NoticeDTO createNoticeDTO(NoticeEntry o) {
        return new NoticeDTO(o.getTime(), o.getText());
    }

}
