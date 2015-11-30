package org.ametiste.routine.mod.tasklog.infrastructure.persistency.jpa;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.infrastructure.persistency.jpa.data.OperationNoticeData;
import org.ametiste.routine.infrastructure.persistency.jpa.data.TaskData;
import org.ametiste.routine.infrastructure.persistency.jpa.data.TaskNoticeData;
import org.ametiste.routine.infrastructure.persistency.jpa.data.TaskPropertyData;
import org.ametiste.routine.mod.tasklog.domain.NoticeEntry;
import org.ametiste.routine.mod.tasklog.domain.OperationLog;
import org.ametiste.routine.mod.tasklog.domain.TaskLogEntry;
import org.ametiste.routine.mod.tasklog.domain.TaskLogRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @since
 */
public class SpringDataTaskLogRepository implements TaskLogRepository {

    private final JPATaskLogRepository jpaTaskLogRepository;

    public SpringDataTaskLogRepository(JPATaskLogRepository jpaTaskLogRepository) {
        this.jpaTaskLogRepository = jpaTaskLogRepository;
    }

    @Override
    @Transactional
    public long countActiveTasks() {
        return jpaTaskLogRepository.countTaskByStateIn(
            statesToString(Task.State.activeStatesList)
        );
    }

    @Override
    @Transactional
    public List<TaskLogEntry> findEntries() {
        throw new UnsupportedOperationException("Reimplemnt it as limited query.");
    }

    @Override
    @Transactional
    public TaskLogEntry findTaskLog(UUID taskId) {
        return processReflectedEntry(
            jpaTaskLogRepository.findOne(taskId)
        );
    }

    @Override
    @Transactional
    public List<UUID> findActiveTasksAfterDate(Instant timePoint) {
        return null;
    }

    @Override
    @Transactional
    public List<TaskLogEntry> findEntries(List<Task.State> states, int offset, int limit) {
        return jpaTaskLogRepository.
                findTaskByStateIn(statesToString(states), new PageRequest(offset, limit))
                .getContent()
                .stream()
                .map(this::processReflectedEntry)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<TaskLogEntry> findEntries(List<Task.State> states, List<TaskProperty> properties, int offset, int limit) {
        return jpaTaskLogRepository.findTaskByStateInAndPropertiesNameInAndPropertiesValueIn(
                        statesToString(states),
                        properties.stream().map(TaskProperty::name).collect(Collectors.toList()),
                        properties.stream().map(TaskProperty::value).collect(Collectors.toList()),
                        new PageRequest(offset, limit))
                .getContent()
                .stream()
                .map(this::processReflectedEntry)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int countEntriesByStatus(String byStatus) {
        return jpaTaskLogRepository.countTaskByState(byStatus);
    }

    @Override
    @Transactional
    public int countByTaskState(Task.State[] states, TaskProperty[] properties) {
        return jpaTaskLogRepository.countTaskByStateInAndPropertiesIn(
                statesToString(Arrays.asList(states)), Arrays.asList(properties).stream().map(
                        (p) -> {
                            return propsToData(p);
                        }
                ).collect(Collectors.toList())
        );
    }

    private List<String> statesToString(List<Task.State> states) {
        return states.stream().map(Task.State::name).collect(Collectors.toList());
    }

    private TaskPropertyData propsToData(TaskProperty p) {
        final TaskPropertyData data = new TaskPropertyData();
        data.name = p.name();
        data.value = p.value();
        return data;
    }

    private NoticeEntry createNoticeEntry(OperationNoticeData notice) {
        return new NoticeEntry(notice.creationTime, notice.text);
    }

    private NoticeEntry createNoticeEntry(TaskNoticeData notice) {
        return new NoticeEntry(notice.creationTime, notice.text);
    }

    private TaskLogEntry processReflectedEntry(TaskData reflectedData) {
        return new TaskLogEntry(
                reflectedData.id,
                reflectedData.creationTime,
                reflectedData.executionStartTime,
                reflectedData.completionTime,
                reflectedData.notices.stream()
                        .map(this::createNoticeEntry)
                        .collect(Collectors.toList()),
                reflectedData.state,
                reflectedData.properties.stream()
                        .collect(Collectors.toMap((p) -> p.name, (p) -> p.value)),
                reflectedData.operationData.stream()
                        .map((x) -> {
                            return new OperationLog(
                                    x.id,
                                    x.label,
                                    x.state,
                                    x.notices.stream()
                                            .map(this::createNoticeEntry)
                                            .collect(Collectors.toList()));
                        })
                        .collect(Collectors.toList())
        );
    }
}
