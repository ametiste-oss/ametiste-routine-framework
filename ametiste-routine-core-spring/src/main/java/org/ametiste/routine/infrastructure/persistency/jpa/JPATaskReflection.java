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
        taskData.setId(taskId);
    }

    @Override
    public void flareTaskState(Task.State state) {
        taskData.setState(state.name());
    }

    @Override
    public void flareOperation(OperationFlare operationFlare) {

        final OperationData operationData = new OperationData();

        operationData.setId(operationFlare.flashId());
        operationData.setLabel(operationFlare.flashLabel());
        operationData.setState(operationFlare.flashState());

        operationFlare.flashNotices().stream().map(n -> {
            final OperationNoticeData noticeData = new OperationNoticeData();
            noticeData.setCreationTime(n.creationTime());
            noticeData.setText(n.text());
            return noticeData;
        }).forEach(operationData::addNoticeData);

        operationFlare.flashProperties().entrySet().stream().map(e -> {
            final OperationPropertyData propertyData= new OperationPropertyData();
            propertyData.setName(e.getKey());
            propertyData.setValue(e.getValue());
            return propertyData;
        }).forEach(operationData::addPropertyData);

        taskData.addOperationData(operationData);

    }

    @Override
    public void flareProperty(String name, String value) {

        if (name.equals(Task.SCHEME_PROPERTY_NAME))  {
            taskData.setSchemeId(value);
            return;
        }

        if (name.equals(Task.CREATOR_PROPERTY_NAME)) {
            taskData.setCreatorId(value);
            return;
        }

        final TaskPropertyData taskPropertyData = new TaskPropertyData();
        taskPropertyData.setName(name);
        taskPropertyData.setValue(value);
        taskData.addPropertyData(taskPropertyData);

    }

    @Override
    public void flareTaskTimes(Instant creationTime, Instant executionStartTime, Instant completionTime) {
        taskData.setCreationTime(mapInstantToDate(creationTime));
        taskData.setExecutionStartTime(mapInstantToDate(executionStartTime));
        taskData.setCompletionTime(mapInstantToDate(completionTime));
    }

    @Override
    public void flareNotice(Notice notice) {
        final TaskNoticeData noticeData = new TaskNoticeData();
        noticeData.setText(notice.text());
        noticeData.setCreationTime(notice.creationTime());
        taskData.addNoticeData(noticeData);
    }

    @Override
    public void reflect(TaskReflection reflection) {

        reflection.flareTaskId(taskData.getId());
        reflection.flareTaskTimes(
                mapDateToInstant(taskData.getCreationTime()),
                mapDateToInstant(taskData.getExecutionStartTime()),
                mapDateToInstant(taskData.getCompletionTime()));

        reflection.flareTaskState(Task.State.valueOf(taskData.getState()));

        taskData.getOperations().forEach((operation) -> {

            final List<Notice> notices = operation.getNotices().stream()
                    .map(notice -> new Notice(notice.getText())).collect(Collectors.toList());

            final Map<String, String> properties = operation.getProperties().stream()
                    .collect(Collectors.toMap(OperationPropertyData::getName, OperationPropertyData::getValue));

            reflection.flareOperation(
                new OperationFlare(operation.getId(),
                        operation.getLabel(), properties, operation.getState(), notices)
            );
        });

        taskData.getNotices().forEach((d) -> {
            reflection.flareNotice(new Notice(d.getText()));
        });

        taskData.getProperties().forEach((p) -> {
            reflection.flareProperty(p.getName(), p.getValue());
        });

        reflection.flareProperty(Task.SCHEME_PROPERTY_NAME, taskData.getSchemeId());
        reflection.flareProperty(Task.CREATOR_PROPERTY_NAME, taskData.getCreatorId());
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
