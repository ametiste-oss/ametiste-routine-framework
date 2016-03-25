package org.ametiste.routine.printer.backlog;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.infrastructure.protocol.taskpool.TaskPoolProtocol;
import org.ametiste.routine.mod.backlog.infrastructure.BacklogPopulationStrategy;
import org.ametiste.routine.printer.scheme.PrintTaskScheme;
import org.ametiste.routine.sdk.protocol.moddata.ModDataClient;
import org.ametiste.routine.infrastructure.protocol.taskpool.TaskPoolClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component(PrintTaskScheme.NAME + "-population")
public class PrintTaskPopulationStrategy implements BacklogPopulationStrategy {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${org.ametiste.routine.eg.printer.populationCount:1000}")
    private int populationCount;

    @Override
    public void populate(ProtocolGateway gateway) {

        final TaskPoolProtocol tasksPool = gateway.session(TaskPoolProtocol.class);;
        final ModDataClient data = new ModDataClient(gateway);

        Integer issuedTasksCount = data
                .loadModDataInt("backlog-print-tasksPool-count")
                .orElse(0);

        logger.debug("Create backlog entries: {}", issuedTasksCount);

        for (int i = 0; i < populationCount; i++, issuedTasksCount++) {
            final Integer taskNumber = issuedTasksCount;
            tasksPool.issueTask(PrintTaskScheme.class, printParams -> {
                printParams.taskNumber(taskNumber);
                printParams.taskOut("I am task #" + taskNumber);
            });
        }

        data.storeModData("backlog-print-tasksPool-count", issuedTasksCount);

    }

}
