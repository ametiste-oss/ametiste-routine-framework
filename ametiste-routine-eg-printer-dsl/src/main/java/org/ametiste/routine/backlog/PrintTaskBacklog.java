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

    @Connect
    private ModDataProtocol modData;

    @Connect
    private ContainerAppProtocol containerApp;

    // @ModDescriptionData("backlog-print-tasksPool-count")
    // private ModDataDescriptor<Integer> tasksCount;

    @BacklogPopulator
    public void populate(PrintTask taskMetaScheme) {

        final int populationCount = containerApp.envProperty(POPULATION_COUNT_PROPERTY, Integer.class);

        Integer issuedTasksCount = modData.loadData("backlog-print-tasksPool-count")
                .map(Integer::parseInt)
                .orElse(0);

        for (int i = 0; i < populationCount; i++, issuedTasksCount++) {
            taskMetaScheme.printOperation("I am task #", issuedTasksCount);
        }

        modData.storeData("backlog-print-tasksPool-count", issuedTasksCount.toString());

    }

}
