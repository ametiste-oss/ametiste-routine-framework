package org.ametiste.routine.mod.backlog.domain;

import org.ametiste.routine.mod.backlog.application.service.BacklogRenewConstraint;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @since
 */
public class Backlog {

    private final String taskSchemeName;
    private final String populationStrategyName;
    private final List<BacklogRenewConstraint> renewConstraints;

    public Backlog(String taskSchemeName, String populationStrategyName) {
        this(taskSchemeName, populationStrategyName, Collections.emptyList());
    }

    public Backlog(String taskSchemeName, String populationStrategyName, List<BacklogRenewConstraint> renewConstraints) {
        this.taskSchemeName = taskSchemeName;
        this.populationStrategyName = populationStrategyName;
        this.renewConstraints = renewConstraints;
    }

    public RenewScheme createRenewScheme() {
        return new RenewScheme(taskSchemeName, populationStrategyName);
    }

    public String boundTaskScheme() {
        return taskSchemeName;
    }

    public List<BacklogRenewConstraint> boundConstraints() {
        return renewConstraints;
    }
}
