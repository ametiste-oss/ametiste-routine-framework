package org.ametiste.routine.infrastructure.persistency.memory;

import org.ametiste.routine.domain.log.NoticeEntry;
import org.ametiste.routine.domain.log.OperationLog;
import org.ametiste.routine.domain.log.TaskLogEntry;
import org.ametiste.routine.domain.log.TaskLogRepository;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.domain.task.notices.Notice;
import org.ametiste.routine.domain.task.reflect.OperationFlare;
import org.ametiste.routine.domain.task.reflect.TaskReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 *
 * @since
 */
public class InMemoryTaskRepository implements TaskRepository {

    private static class InMemoryTaskReflection implements TaskReflection {

        UUID taskId;

        Task.State state;

        List<OperationFlare> operations = new ArrayList<>();

        Map<String, String> properties = new HashMap<>();

        Instant creationTime;

        Instant executionStartTime;

        Instant completionTime;

        List<Notice> notices = new ArrayList<>();

        @Override
        public void flareTaskId(UUID taskId) {
            this.taskId = taskId;
        }

        @Override
        public void flareTaskState(Task.State state) {
            this.state = state;
        }

        @Override
        public void flareOperation(OperationFlare operationFlare) {
            operations.add(operationFlare);
        }

        @Override
        public void flareProperty(String name, String value) {
            properties.put(name, value);
        }

        @Override
        public void flareTaskTimes(Instant creationTime, Instant executionStartTime, Instant completionTime) {
            this.creationTime = creationTime;
            this.executionStartTime = executionStartTime;
            this.completionTime = completionTime;
        }

        @Override
        public void reflect(TaskReflection reflection) {

            reflection.flareTaskId(taskId);
            reflection.flareTaskState(state);

            reflection.flareTaskTimes(creationTime, executionStartTime, completionTime);

            notices.forEach(reflection::flareNotice);
            operations.forEach(reflection::flareOperation);
            properties.forEach(reflection::flareProperty);
        }

        @Override
        public void flareNotice(Notice notice) {
            this.notices.add(notice);
        }

        public UUID flashTaskId() {
            return taskId;
        }

        public Task.State flashTaskState() {
            return state;
        }

        public Collection<OperationFlare> flashOperation() {
            return Collections.unmodifiableCollection(operations);
        }

        public Map<String, String> flashProperties() {
            return Collections.unmodifiableMap(properties);
        }


    }

    private Map<UUID, ReentrantLock> transactions = new ConcurrentHashMap<>();

    private Map<UUID, InMemoryTaskReflection> reflections = new HashMap<>();

    private TaskLogRepository logRepository;

    // private final ActionActuatorDelegate actuatorDelegate;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public InMemoryTaskRepository(TaskLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public Task findTask(UUID taskId) {

        if (logger.isDebugEnabled()) {
            logger.debug("Trying to find task : " + taskId.toString());
        }

        if (transactions.containsKey(taskId)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Locking task : " + taskId.toString());
            }
            transactions.get(taskId).lock();
        }

        try {
            if (reflections.containsKey(taskId)) {
                return new Task(reflections.get(taskId));
            } else {
                if (transactions.containsKey(taskId)) {
                    transactions.get(taskId).unlock();
                }
                throw new RuntimeException("Can't find task.");
            }
        } finally {
        }

    }

    @Override
    public List<Task> findTasksByState(Task.State state, int limit) {
        throw new RuntimeException("Not implemented yet.");
    }

    @Override
    public void saveTask(Task task) {

        if (transactions.containsKey(task.entityId())) {
            if(!transactions.get(task.entityId()).isHeldByCurrentThread()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Locking task : " + task.entityId().toString());
                }
                transactions.get(task.entityId()).lock();
            }
        } else {
            final ReentrantLock lock = new ReentrantLock();
            lock.lock();
            if (logger.isDebugEnabled()) {
                logger.debug("Locking task : " + task.entityId().toString());
            }
            transactions.put(task.entityId(), lock);
        }

        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Saving task : " + task.entityId().toString());
            }

            final InMemoryTaskReflection taskReflection = new InMemoryTaskReflection();

            task.reflectAs(taskReflection);

            reflections.put(taskReflection.flashTaskId(), taskReflection);

            // TODO: need to move it, I want to implement sane logging subsystem, but don't have idea how to do it

            logRepository.saveTaskLog(new TaskLogEntry(
                    taskReflection.taskId,
                    taskReflection.creationTime,
                    taskReflection.executionStartTime,
                    taskReflection.completionTime,
                    taskReflection.notices.stream()
                            .map(this::createNoticeEntry)
                            .collect(Collectors.toList()),
                    taskReflection.state.name(),
                    taskReflection.operations.stream()
                            .map((x) -> {
                                return new OperationLog(
                                        x.flashId(),
                                        x.flashLabel(),
                                        x.flashState(),
                                        x.flashNotices().stream()
                                                .map(this::createNoticeEntry)
                                                .collect(Collectors.toList()));
                            })
                            .collect(Collectors.toList())
            ));

            if (taskReflection.flashTaskState().equals(Task.State.NEW)) {
                // actuatorDelegate.produceEvent("global.taskArrived");
            }

        } finally {

            if (logger.isDebugEnabled()) {
                logger.debug("Unlocking task : " + task.entityId().toString());
            }

            transactions.get(task.entityId()).unlock();
        }
    }

    @Override
    public Task findTaskByOperationId(UUID operationId) {

        for (InMemoryTaskReflection taskReflection : reflections.values()) {
            for (OperationFlare operationFlare : taskReflection.flashOperation()) {
                if (operationFlare.flashId().equals(operationId)) {

                    if (logger.isDebugEnabled()) {
                        logger.debug("Trying to load task : " + taskReflection.flashTaskId());
                    }

                    if (transactions.containsKey(taskReflection.flashTaskId())) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Locking task : " + taskReflection.flashTaskId());
                        }

                        transactions.get(taskReflection.flashTaskId()).lock();

                        // NOTE: need to load fresh version of the reflection, after lock is acquired
                        // in other hand we would work on old data
                        return new Task(reflections.get(taskReflection.flashTaskId()));

                    }
                }
            }
        }

        throw new RuntimeException("Task not found.");

    }

    private NoticeEntry createNoticeEntry(Notice notice) {
        return new NoticeEntry(notice.creationTime(), notice.text());
    }

}
