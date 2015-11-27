package org.ametiste.routine.mod.backlog.domain;

/**
 *
 * @since
 */
public class Backlog {

    private final String taskSchemeName;

    private final String populationStrategyName;

    public Backlog(String taskSchemeName, String populationStrategyName) {
        this.taskSchemeName = taskSchemeName;
        this.populationStrategyName = populationStrategyName;
    }

    public RenewScheme createRenewScheme() {
        return new RenewScheme(taskSchemeName, populationStrategyName);
    }

    public String boundTaskScheme() {
        return taskSchemeName;
    }
}
