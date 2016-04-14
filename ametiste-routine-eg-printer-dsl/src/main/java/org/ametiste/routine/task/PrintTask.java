package org.ametiste.routine.task;

import org.ametiste.routine.dsl.annotations.*;
import org.ametiste.routine.protocol.SleepProtocol;
import org.ametiste.routine.sdk.protocol.containerapp.ContainerAppProtocol;

import java.util.Arrays;
import java.util.stream.Stream;

@RoutineTask
@SchemeMapping(schemeName = "printTaskScheme")
public class PrintTask {

    @Connect
    private ContainerAppProtocol containerAppProtocol;

    @Connect
    private SleepProtocol sleepProtocol;

    @TaskOperation
    public void printOperationOut(@OperationParameter("operationOut") String prefix,
                                  @OperationParameter("secondParameter") int taskNumber,
                                  @OperationParameter("delay") long delay) {
        containerAppProtocol.systemOut(prefix + Integer.toString(taskNumber));
    }

    @TaskOperation(order = 2)
    public void printOperationId(@OperationId String operationId) {
        containerAppProtocol.systemOut("Operation id: " + operationId);
    }

    @TaskOperation(order = 3)
    public void printOperationName(@OperationName String operationName) {
        containerAppProtocol.systemOut("Operation name: " + operationName);
    }

    @TaskOperation(order = 4)
    public void printTaskId(@TaskId String taskId) {
        containerAppProtocol.systemOut("Task id: " + taskId);
    }

}
