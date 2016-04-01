package org.ametiste.routine;

import org.ametiste.routine.app.annotations.*;
import org.ametiste.routine.infrastructure.protocol.taskpool.TaskPoolProtocol;
import org.ametiste.routine.mod.backlog.protocol.BacklogProtocol;

interface ContainerAppProtocol {

    void systemOut(String operationOut);

}

@RoutineTask
@SchemeMapping(schemeName = "printTaskScheme")
public class PrintTask {

//    @Connect
//    private ContainerAppProtocol containerAppConnection;

    @Connect
    private TaskPoolProtocol taskPoolProtocol;

    @TaskOperation
    public void printOperation(
            @OperationParameter("operationOut") String operationOut,
            @OperationParameter("secondParameter") String secondOut) {
        System.out.println(">>>" + operationOut);
        System.out.println(">>>" + secondOut);
    }

}
