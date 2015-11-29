package org.ametiste.routine.mod.tasklog.interfaces.web;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.properties.BasicTaskProperty;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.mod.tasklog.domain.NoticeEntry;
import org.ametiste.routine.mod.tasklog.domain.TaskLogEntry;
import org.ametiste.routine.mod.tasklog.domain.TaskLogNotFoundException;
import org.ametiste.routine.mod.tasklog.domain.TaskLogRepository;
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

    // TODO: rework for Optional parameters
    @RequestMapping(method = RequestMethod.GET)
    public TaskLogEntryResources provideLogEntries(
            @RequestParam(value = "status", required = false) List<String> byStatus,
            @RequestParam(value = "property", required = false) List<String> byProperty,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {

        final List<TaskProperty> propertiesFilter;
        final List<Task.State> states;

        if (byProperty != null) {
            //
            // NOTE: properties filter format is:
            //
            //          property=name.one:value.one,name.two:value.two
            //
            // So, just split each part by ':' for two pieces, note is allows for values
            // to contain ':' symbol also.
            //
            propertiesFilter = byProperty.stream()
                    .map(s -> s.split(":", 2))
                    .map(a -> new BasicTaskProperty(a[0], a[1]))
                    .collect(Collectors.toList());
        } else {
            propertiesFilter = Collections.emptyList();
        }

        if (byStatus != null) {
            states = byStatus.stream().map(Task.State::valueOf).collect(Collectors.toList());
        } else {
            states = Collections.emptyList();
        }

        final List<TaskLogEntry> entryList;
        final List<TaskLogEntryResource> entryResources = new ArrayList<>();

        if (byProperty == null && byStatus == null) {
            entryList = taskLogRepository.findEntries();
        } else if (byProperty != null) {
            entryList = taskLogRepository.findEntries(states, propertiesFilter, offset, limit);
        } else {
            entryList = taskLogRepository.findEntries(states, offset, limit);
        }

        entryList.stream()
                .map(this::assembleLogEntryResource)
                .forEach(entryResources::add);

        final TaskLogEntryResources resources = new TaskLogEntryResources(entryResources);

        resources.add(
                linkTo(methodOn(TaskLogController.class)
                                .provideLogEntries(byStatus, byProperty, offset, limit)).withSelfRel()
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
                taskLogEntry.getProperties(),
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
