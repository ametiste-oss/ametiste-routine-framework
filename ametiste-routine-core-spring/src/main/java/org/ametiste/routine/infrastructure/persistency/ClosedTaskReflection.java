package org.ametiste.routine.infrastructure.persistency;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.notices.Notice;
import org.ametiste.routine.domain.task.reflect.OperationFlare;
import org.ametiste.routine.domain.task.reflect.TaskReflection;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

public abstract class ClosedTaskReflection implements TaskReflection {

    public static class ReflectedTaskData implements Serializable {

        public UUID taskId;

        public Task.State state;

        public List<OperationFlare> operationFlare = new ArrayList<>();

        public Map<String, String> properties = new HashMap<>();

        public Instant creationTime;

        public Instant executionStartTime;

        public Instant completionTime;

        public List<Notice> notices = new ArrayList<>();

    }

    protected ReflectedTaskData reflectedTaskData = new ReflectedTaskData();

    @Override
    public void flareTaskId(UUID taskId) {
        this.reflectedTaskData.taskId = taskId;
    }

    @Override
    public void flareTaskState(Task.State state) {
        this.reflectedTaskData.state = state;
    }

    @Override
    public void flareOperation(OperationFlare operationFlare) {
        this.reflectedTaskData.operationFlare.add(operationFlare);
    }

    @Override
    public void flareProperty(String name, String value) {
        this.reflectedTaskData.properties.put(name, value);
    }

    @Override
    public void flareTaskTimes(Instant creationTime, Instant executionStartTime, Instant completionTime) {
        this.reflectedTaskData.creationTime = creationTime;
        this.reflectedTaskData.executionStartTime = executionStartTime;
        this.reflectedTaskData.completionTime = completionTime;
    }

    @Override
    public void flareNotice(Notice notice) {
        this.reflectedTaskData.notices.add(notice);
    }

    @Override
    public void reflect(TaskReflection reflection) {

        reflection.flareTaskId(reflectedTaskData.taskId);
        reflection.flareTaskTimes(reflectedTaskData.creationTime,
                reflectedTaskData.executionStartTime, reflectedTaskData.completionTime);
        reflection.flareTaskState(reflectedTaskData.state);

        reflectedTaskData.operationFlare.forEach(reflection::flareOperation);
        reflectedTaskData.notices.forEach(reflection::flareNotice);
        reflectedTaskData.properties.forEach(reflection::flareProperty);

    }

}
