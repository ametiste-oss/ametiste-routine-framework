package org.ametiste.routine.mod.tasklog.interfaces.web.resources;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.core.Relation;

/**
 *
 * @since
 */
@Relation(value="sro:task-log-entry", collectionRelation = "sro:task-log-entries")
public class TaskLogEntryResource extends Resource<TaskLogEntryContent> {

    public TaskLogEntryResource(TaskLogEntryContent content, Link... links) {
        super(content, links);
    }

}
