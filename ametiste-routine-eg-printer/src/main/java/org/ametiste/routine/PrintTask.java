package org.ametiste.routine;

import org.ametiste.routine.app.annotations.*;

interface ContainerAppProtocol {

    void systemOut(String operationOut);

}

@RoutineTask
@SchemeMapping(schemeName = "printTaskScheme")
public class PrintTask {

    @Connect
    private ContainerAppProtocol containerAppConnection;

    @TaskOperation
    public void printOperation(@OperationParameter("operationOut") String operationOut) {
        containerAppConnection.systemOut(operationOut);
    }

}
