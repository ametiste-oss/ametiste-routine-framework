package org.ametiste.routine.mod.backlog.domain;

/**
 *
 * @since
 */
public class RenewScheme {

    private final String taskSchemeName;
    private final String populationStrategyName;

    public RenewScheme(String taskSchemeName, String populationStrategyName) {
        this.taskSchemeName = taskSchemeName;
        this.populationStrategyName = populationStrategyName;
    }

    public String taskSchemeName() {
        return taskSchemeName;
    }

    public String populationStrategyName() {
        return populationStrategyName;
    }

}
