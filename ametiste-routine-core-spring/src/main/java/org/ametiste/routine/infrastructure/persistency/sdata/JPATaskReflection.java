package org.ametiste.routine.infrastructure.persistency.sdata;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.notices.Notice;
import org.ametiste.routine.domain.task.reflect.OperationFlare;
import org.ametiste.routine.domain.task.reflect.TaskReflection;
import org.ametiste.routine.infrastructure.persistency.ClosedTaskReflection;

import java.time.Instant;
import java.util.UUID;

/**
 *
 * @since
 */
public class JPATaskReflection implements TaskReflection {

    private TaskData taskData;

    public JPATaskReflection() {
        this.taskData = new TaskData();
    }

    public JPATaskReflection(TaskData taskData) {
        this.taskData = taskData;
    }

    @Override
    public void flareTaskId(UUID taskId) {
        taskData.id = taskId;
    }

    @Override
    public void flareTaskState(Task.State state) {
        taskData.state = state.name();
    }

    @Override
    public void flareOperation(OperationFlare operationFlare) {

        final OperationData operationData = new OperationData();

        operationData.id = operationFlare.flashId();
        operationData.label = operationFlare.flashLabel();
        operationData.state = operationFlare.flashState();

        operationFlare.flashNotices().forEach(
                (notice) -> {
                    final OperationNoticeData noticeData = new OperationNoticeData();
                    noticeData.creationTime = notice.creationTime();
                    noticeData.text = notice.text();
                    operationData.notices.add(noticeData);
                }
        );

        operationFlare.flashProperties().forEach(
                (n, v) -> {
                    final OperationPropertyData propertyData= new OperationPropertyData();
                    propertyData.name = n;
                    propertyData.value = v;
                    operationData.properties.add(propertyData);
                }
        );

        this.taskData.operationData.add(operationData);
    }

    @Override
    public void flareProperty(String name, String value) {
        final TaskPropertyData taskPropertyData = new TaskPropertyData();
        taskPropertyData.name = name;
        taskPropertyData.value = value;
        this.taskData.properties.add(taskPropertyData);
    }

    @Override
    public void flareTaskTimes(Instant creationTime, Instant executionStartTime, Instant completionTime) {
        this.taskData.creationTime = creationTime;
        this.taskData.executionStartTime = executionStartTime;
        this.taskData.completionTime = completionTime;
    }

    @Override
    public void flareNotice(Notice notice) {
        final TaskNoticeData noticeData = new TaskNoticeData();
        noticeData.text = notice.text();
        noticeData.creationTime = notice.creationTime();
        this.taskData.notices.add(noticeData);
    }

    @Override
    public void reflect(TaskReflection reflection) {

        reflection.flareTaskId(taskData.id);
        reflection.flareTaskTimes(taskData.creationTime,
                taskData.executionStartTime, taskData.completionTime);
        reflection.flareTaskState(Task.State.valueOf(taskData.state));

        taskData.operationData.forEach((d) -> {
            reflection.flareOperation(new OperationFlare(d.id, d.label, null, d.state, null));
        });

        taskData.notices.forEach((d) -> {
            reflection.flareNotice(new Notice(d.text));
        });

        taskData.properties.forEach((p) -> {
            reflection.flareProperty(p.name, p.value);
        });

    }

    public TaskData reflectedTaskData() {
        return taskData;
    }

}
