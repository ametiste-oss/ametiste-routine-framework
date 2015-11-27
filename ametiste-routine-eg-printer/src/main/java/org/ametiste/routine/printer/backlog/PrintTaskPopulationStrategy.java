package org.ametiste.routine.printer.backlog;

import org.ametiste.routine.sdk.mod.DataGateway;
import org.ametiste.routine.sdk.mod.TaskGateway;
import org.ametiste.routine.printer.scheme.PrintTaskScheme;
import org.ametiste.routine.mod.backlog.infrasturcture.BacklogPopulationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component(PrintTaskScheme.NAME + "-population")
public class PrintTaskPopulationStrategy implements BacklogPopulationStrategy {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void populate(TaskGateway taskGateway, DataGateway dataGateway) {

        logger.warn("Create task entries from backlog.");

        int issuedTasksCount = dataGateway
                .loadModDataInt("backlog-print-tasks-count")
                .orElse(0);

        for (int i = 0; i < 500; i++, issuedTasksCount++) {
            taskGateway.issueTask(PrintTaskScheme.NAME,
                    Collections.singletonMap("out", "I am task #" + issuedTasksCount)
            );
        }

        dataGateway.storeModData("backlog-print-tasks-count", issuedTasksCount);

    }

}
