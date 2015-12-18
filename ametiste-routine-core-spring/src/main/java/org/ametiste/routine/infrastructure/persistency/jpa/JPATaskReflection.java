package org.ametiste.routine.infrastructure.persistency.jpa;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.notices.Notice;
import org.ametiste.routine.domain.task.reflect.OperationFlare;
import org.ametiste.routine.domain.task.reflect.TaskReflection;
import org.ametiste.routine.infrastructure.persistency.jpa.data.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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

        if (name.equals(Task.SCHEME_PROPERTY_NAME))  {
            taskData.schemeId = value;
            return;
        }

        if (name.equals(Task.CREATOR_PROPERTY_NAME)) {
            taskData.creatorId = value;
            return;
        }

        final TaskPropertyData taskPropertyData = new TaskPropertyData();
        taskPropertyData.name = name;
        taskPropertyData.value = value;

        taskData.properties.add(taskPropertyData);
    }

    @Override
    public void flareTaskTimes(Instant creationTime, Instant executionStartTime, Instant completionTime) {
        taskData.creationTime = mapInstantToDate(creationTime);
        taskData.executionStartTime = mapInstantToDate(executionStartTime);
        taskData.completionTime = mapInstantToDate(completionTime);
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
        reflection.flareTaskTimes(
                mapDateToInstant(taskData.creationTime),
                mapDateToInstant(taskData.executionStartTime),
                mapDateToInstant(taskData.completionTime));
        reflection.flareTaskState(Task.State.valueOf(taskData.state));

        taskData.operationData.forEach((operation) -> {

            final List<Notice> notices = operation.notices.stream()
                    .map(notice -> new Notice(notice.text)).collect(Collectors.toList());

            final Map<String, String> properties = operation.properties.stream()
                    .collect(Collectors.toMap(p -> p.name, p -> p.value));

            reflection.flareOperation(
                new OperationFlare(operation.id, operation.label, properties, operation.state, notices)
            );
        });

        taskData.notices.forEach((d) -> {
            reflection.flareNotice(new Notice(d.text));
        });

        taskData.properties.forEach((p) -> {
            reflection.flareProperty(p.name, p.value);
        });

        reflection.flareProperty(Task.SCHEME_PROPERTY_NAME, taskData.schemeId);
        reflection.flareProperty(Task.CREATOR_PROPERTY_NAME, taskData.creatorId);
    }

    public TaskData reflectedTaskData() {
        return taskData;
    }

    private Date mapInstantToDate(Instant instant) {
        return Optional.ofNullable(instant).map(Date::from).orElse(null);
    }

    private Instant mapDateToInstant(Date date) {
        return Optional.ofNullable(date).map(Date::toInstant).orElse(null);
    }


}
