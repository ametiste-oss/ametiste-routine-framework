package org.ametiste.routine.infrastructure.persistency;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.notices.Notice;
import org.ametiste.routine.domain.task.reflect.OperationFlare;
import org.ametiste.routine.domain.task.reflect.TaskReflection;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;

public abstract class ClosedTaskReflection implements TaskReflection {

    public static class ReflectedTaskData implements Serializable {

        @Id
        public UUID id;

        public Task.State state;

        @OneToMany
        public List<ReflectedOperationData> operationData = new ArrayList<>();

        @OneToMany
        public List<ReflectedTaskPropertyData> properties = new ArrayList<>();

        @Column(name = "cr_time")
        public Instant creationTime;

        @Column(name = "execs_time")
        public Instant executionStartTime;

        @Column(name = "co_time")
        public Instant completionTime;

        @OneToMany(targetEntity = ReflectedTaskNoticeData.class)
        public List<ReflectedTaskNoticeData> notices = new ArrayList<>();

    }

    public static class ReflectedOperationData implements Serializable {

        @Id
        public UUID id;

        public String label;

        public String state;

        @OneToMany
        public List<ReflectedOperationPropertyData> properties = new ArrayList<>();

        @OneToMany
        public List<ReflectedOperationNoticeData> notices = new ArrayList<>();

    }

    public static class ReflectedOperationPropertyData implements Serializable {

        @Id
        @GeneratedValue
        public int id;

        @Column(name = "operation_id")
        public UUID operationId;

        public String name;

        public String value;

    }

    public static class ReflectedTaskPropertyData implements Serializable {

        @Id
        @GeneratedValue
        public int id;

        @Column(name = "task_id")
        public UUID taskId;

        public String name;

        public String value;

    }

    public static class ReflectedOperationNoticeData implements Serializable {

        @Id
        @GeneratedValue
        public int id;

        @Column(name = "operation_id")
        public UUID operationId;

        @Column(name = "cr_time")
        public Instant creationTime;

        public String text;

    }

    public static class ReflectedTaskNoticeData implements Serializable {

        @Id
        @GeneratedValue
        public int id;

        @Column(name = "task_id")
        public UUID taskId;

        @Column(name = "cr_time")
        public Instant creationTime;

        public String text;

    }

    protected ReflectedTaskData reflectedTaskData = new ReflectedTaskData();

    @Override
    public void flareTaskId(UUID taskId) {
        this.reflectedTaskData.id = taskId;
    }

    @Override
    public void flareTaskState(Task.State state) {
        this.reflectedTaskData.state = state;
    }

    @Override
    public void flareOperation(OperationFlare operationFlare) {

        final ReflectedOperationData operationData = new ReflectedOperationData();
        operationData.id = operationFlare.flashId();
        operationData.label = operationFlare.flashLabel();
        operationData.state = operationFlare.flashState();

        operationFlare.flashNotices().forEach(
                (r) -> {
                    final ReflectedOperationNoticeData noticeData = new ReflectedOperationNoticeData();
                    noticeData.operationId = operationData.id;
                    noticeData.creationTime = r.creationTime();
                    noticeData.text = r.text();
                    operationData.notices.add(noticeData);
                }
        );

        operationFlare.flashProperties().forEach(
                (n, v) -> {
                    final ReflectedOperationPropertyData propertyData= new ReflectedOperationPropertyData();
                    propertyData.operationId = operationData.id;
                    propertyData.name = n;
                    propertyData.value = v;
                    operationData.properties.add(propertyData);
                }
        );

        this.reflectedTaskData.operationData.add(operationData);
    }

    @Override
    public void flareProperty(String name, String value) {

        final ReflectedTaskPropertyData taskPropertyData = new ReflectedTaskPropertyData();
        taskPropertyData.taskId = reflectedTaskData.id;
        taskPropertyData.name = name;
        taskPropertyData.value = value;

        this.reflectedTaskData.properties.add(taskPropertyData);
    }

    @Override
    public void flareTaskTimes(Instant creationTime, Instant executionStartTime, Instant completionTime) {
        this.reflectedTaskData.creationTime = creationTime;
        this.reflectedTaskData.executionStartTime = executionStartTime;
        this.reflectedTaskData.completionTime = completionTime;
    }

    @Override
    public void flareNotice(Notice notice) {

        final ReflectedTaskNoticeData noticeData = new ReflectedTaskNoticeData();
        noticeData.taskId = reflectedTaskData.id;
        noticeData.text = notice.text();
        noticeData.creationTime = notice.creationTime();

        this.reflectedTaskData.notices.add(noticeData);
    }

    @Override
    public void reflect(TaskReflection reflection) {

        reflection.flareTaskId(reflectedTaskData.id);
        reflection.flareTaskTimes(reflectedTaskData.creationTime,
                reflectedTaskData.executionStartTime, reflectedTaskData.completionTime);
        reflection.flareTaskState(reflectedTaskData.state);

        reflectedTaskData.operationData.forEach((d) -> {
            reflection.flareOperation(new OperationFlare(d.id, d.label, null, d.state, null));
        });

        reflectedTaskData.notices.forEach((d) -> {
            reflection.flareNotice(new Notice(d.text));
        });

        reflectedTaskData.properties.forEach((p) -> {
            reflection.flareProperty(p.name, p.value);
        });
    }

    public ReflectedTaskData reflectedTaskData() {
        return reflectedTaskData;
    }

}
