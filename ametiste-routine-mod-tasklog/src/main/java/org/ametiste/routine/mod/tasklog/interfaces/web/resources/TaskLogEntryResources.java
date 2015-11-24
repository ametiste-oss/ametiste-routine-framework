package org.ametiste.routine.mod.tasklog.interfaces.web.resources;

import org.springframework.hateoas.Resources;

import java.util.List;

/**
 *
 * @since
 */
public class TaskLogEntryResources extends Resources<TaskLogEntryResource> {

    private int count;

    public TaskLogEntryResources(List<TaskLogEntryResource> resources) {
        super(resources);
        count = resources.size();
    }

    public int getCount() {
        return count;
    }

}
