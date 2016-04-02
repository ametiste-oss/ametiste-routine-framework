package org.ametiste.routine.backlog;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.dsl.annotations.Connect;
import org.ametiste.routine.dsl.annotations.SchemeMapping;
import org.ametiste.routine.meta.scheme.TaskMetaScheme;
import org.ametiste.routine.mod.backlog.dsl.annotations.BacklogController;
import org.ametiste.routine.mod.backlog.dsl.annotations.BacklogPopulator;
import org.ametiste.routine.protocol.ContainerAppProtocol;
import org.ametiste.routine.sdk.protocol.moddata.ModDataProtocol;
import org.ametiste.routine.task.PrintTask;

import java.util.function.Consumer;


@BacklogController
@SchemeMapping(schemeClass = PrintTask.class)
public class PrintTaskBacklog {

    // TODO: provide properties through ContainerAppProtocol
    private int populationCount = 10;

    @Connect
    private ModDataProtocol modData;

    @Connect
    private ContainerAppProtocol containerAppProtocol;

    // @ModDescriptionData("backlog-print-tasksPool-count")
    // private ModDataDescriptor<Integer> tasksCount;

    @BacklogPopulator
    public void populate(PrintTask taskMetaScheme) {

        containerAppProtocol.systemOut(modData.toString());

        Integer issuedTasksCount = modData
                .loadData("backlog-print-tasksPool-count")
                .map(Integer::parseInt)
                .orElse(0);

        for (int i = 0; i < populationCount; i++, issuedTasksCount++) {
            taskMetaScheme.printOperation("I am task #" + issuedTasksCount, "out");
        }

        modData.storeData("backlog-print-tasksPool-count", issuedTasksCount.toString());

    }

}
