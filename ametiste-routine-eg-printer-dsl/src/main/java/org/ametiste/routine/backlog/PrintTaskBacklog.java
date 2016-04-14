package org.ametiste.routine.backlog;

import org.ametiste.routine.dsl.annotations.Connect;
import org.ametiste.routine.dsl.annotations.SchemeMapping;
import org.ametiste.routine.mod.backlog.dsl.annotations.BacklogController;
import org.ametiste.routine.mod.backlog.dsl.annotations.BacklogPopulator;
import org.ametiste.routine.sdk.protocol.containerapp.ContainerAppProtocol;
import org.ametiste.routine.sdk.protocol.moddata.ModDataProtocol;
import org.ametiste.routine.task.PrintTask;


@BacklogController
@SchemeMapping(schemeClass = PrintTask.class)
public class PrintTaskBacklog {

    private final static String POPULATION_COUNT_PROPERTY = "org.ametiste.routine.eg.printer.populationCount";

    private final static String DELAY_PROPERTY = "org.ametiste.routine.eg.printer.delayTime";

    @Connect
    private ModDataProtocol modData;

    @Connect
    private ContainerAppProtocol containerApp;

    // @ModDescriptionData("backlog-print-tasks-count")
    // private ModDataDescriptor<Integer> tasksCount;

    @BacklogPopulator
    public void populate(PrintTask taskMetaScheme) {

        final int populationCount = containerApp.envProperty(POPULATION_COUNT_PROPERTY, Integer.class);
        final long delayTime = containerApp.envProperty(DELAY_PROPERTY, Long.class);

        Integer issuedTasksCount = modData.loadData("backlog-print-tasks-count", Integer.class)
                .orElse(0);

        for (int i = 0; i < populationCount; i++, issuedTasksCount++) {
            taskMetaScheme.printOperationOut("I am task #", issuedTasksCount, delayTime);
        }

        modData.storeData("backlog-print-tasks-count", issuedTasksCount);

    }

}
