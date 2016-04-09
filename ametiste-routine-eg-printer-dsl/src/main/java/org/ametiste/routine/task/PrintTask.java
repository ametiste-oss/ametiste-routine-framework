package org.ametiste.routine.task;

import org.ametiste.routine.dsl.annotations.*;
import org.ametiste.routine.sdk.protocol.containerapp.ContainerAppProtocol;

@RoutineTask
@SchemeMapping(schemeName = "printTaskScheme")
public class PrintTask {

    @Connect
    private ContainerAppProtocol containerAppProtocol;

    @TaskOperation
    public void printOperation(@OperationParameter("operationOut") String prefix,
            @OperationParameter("secondParameter") int taskNumber) {
        containerAppProtocol.systemOut(prefix + Integer.toString(taskNumber));
    }

    @TaskOperation(order = 2)
    public void cleanupOperation() {
        containerAppProtocol.systemOut("Just Second Print");
    }

}
