package org.ametiste.routine.task;

import org.ametiste.routine.dsl.annotations.*;
import org.ametiste.routine.protocol.ContainerAppProtocol;

@RoutineTask
@SchemeMapping(schemeName = "printTaskScheme")
public class PrintTask {

    @Connect
    private ContainerAppProtocol containerAppProtocol;

    @TaskOperation
    public void printOperation(@OperationParameter("operationOut") String operationOut,
            @OperationParameter("secondParameter") String secondOut) {

        containerAppProtocol.systemOut(operationOut);
        containerAppProtocol.systemOut(secondOut);
        containerAppProtocol.systemOut(containerAppProtocol.toString());

    }

}