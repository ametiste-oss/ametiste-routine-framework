package org.ametiste.routine.printer.backlog;

import org.ametiste.routine.sdk.mod.DataGateway;
import org.ametiste.routine.sdk.mod.TaskGateway;
import org.ametiste.routine.printer.scheme.PrintTaskScheme;
import org.ametiste.routine.mod.backlog.infrastructure.BacklogPopulationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;

@Component(PrintTaskScheme.NAME + "-population")
public class PrintTaskPopulationStrategy implements BacklogPopulationStrategy {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${org.ametiste.routine.eg.printer.populationCount:1000}")
    private int populationCount;

    @Override
    public void populate(TaskGateway taskGateway, DataGateway dataGateway) {

        logger.warn("Create task entries from backlog.");

        int issuedTasksCount = dataGateway
                .loadModDataInt("backlog-print-tasks-count")
                .orElse(0);

        for (int i = 0; i < populationCount; i++, issuedTasksCount++) {

            final HashMap<String, String> params = new HashMap<>();
            params.put("task.number", Integer.toString(issuedTasksCount));
            params.put("task.out", "I am task #" + issuedTasksCount);

            taskGateway.issueTask(PrintTaskScheme.NAME, params);
        }

        dataGateway.storeModData("backlog-print-tasks-count", issuedTasksCount);

    }

}
