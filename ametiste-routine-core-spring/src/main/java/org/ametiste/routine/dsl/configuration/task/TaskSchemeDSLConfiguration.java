package org.ametiste.routine.dsl.configuration.task;

import org.ametiste.lang.Pair;
import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.domain.scheme.SchemeRepository;
import org.ametiste.routine.domain.scheme.TaskBuilder;
import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.domain.scheme.TaskSchemeException;
import org.ametiste.routine.dsl.annotations.RoutineTask;
import org.ametiste.routine.dsl.annotations.SchemeMapping;
import org.ametiste.routine.dsl.annotations.TaskOperation;
import org.ametiste.routine.dsl.application.*;
import org.ametiste.routine.dsl.application.ParamaterProvider;
import org.ametiste.routine.sdk.mod.ModGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @since
 */
@Configuration
public class TaskSchemeDSLConfiguration {

    @Autowired(required = false)
    @RoutineTask
    private List<Object> taskControllers = Collections.emptyList();

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private TaskIssueService taskIssueService;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private List<ParamaterProvider> paramProviders;

    @Bean
    public DynamicTaskService dynamicTaskService() {
        return new DynamicTaskService(schemeRepository, taskIssueService);
    }

    @Bean
    public ModGateway modTaskSchemeDSL() {

        final List<TaskScheme> schemes = taskControllers.stream()
                .map(Object::getClass)
                .map(this::mapToTaskScheme)
                .collect(Collectors.toList());

        schemes.forEach(schemeRepository::saveScheme);

        return gw -> {
            // TODO: how can I propagate artifact version?
            gw.modInfo("dsl-task-scheme", "1.1",
                schemes.stream().collect(Collectors.toMap(s -> s.schemeName(), s -> s.getClass().getName()))
            );
        };
    }

    private TaskScheme mapToTaskScheme(Class<?> controllerClass) {

        if (!controllerClass.isAnnotationPresent(RoutineTask.class)) {
            throw new IllegalArgumentException("Only @RoutineTask classes are allowed.");
        }

        final String schemeName = controllerClass
                .getDeclaredAnnotation(SchemeMapping.class)
                .schemeName();

        final List<DynamicOperationScheme> operations = Stream
                .of(ReflectionUtils.getAllDeclaredMethods(controllerClass))
                .filter(m -> m.isAnnotationPresent(TaskOperation.class))
                // NOTE: operations order support, just sort list of schemas in a defined order
                .sorted((t, o)  -> {
                    final int first = t.getDeclaredAnnotation(TaskOperation.class).order();
                    final int second = o.getDeclaredAnnotation(TaskOperation.class).order();
                    if (first > second) {
                        return 1;
                    } else if (first < second) {
                        return -1;
                    } else {
                        // TODO: add scheme name to exception
                        throw new IllegalStateException("Operations order is undefined. " +
                                "Please define unique operations order explicitly.");
                    }
                })
                .map(m -> Pair.of(resolveOperationName(m), new DynamicOperationFactory(controllerClass, m, paramProviders)))
                .map(p -> new DynamicOperationScheme(p.first(), p.second()))
                .collect(Collectors.toList());

        operations.forEach(schemeRepository::saveScheme);

        return new TaskScheme<DynamicParamsProtocol>() {

            @Override
            public String schemeName() {
                return schemeName;
            }

            @Override
            public void setupTask(final TaskBuilder<DynamicParamsProtocol> taskBuilder, final Consumer<DynamicParamsProtocol> paramsInstaller, final String creatorIdenifier) throws TaskSchemeException {

                final DynamicParamsProtocol dynamicParamsProtocol = new DirectDynamicParamsProtocol();
                paramsInstaller.accept(dynamicParamsProtocol);

                // NOTE: operations are sorted in defined order, so just adding em one after one
                operations.forEach(
                    op -> taskBuilder.addOperation(op.schemeName(), dynamicParamsProtocol)
                );
            }
        };
    }

    private String resolveOperationName(final Method m) {

        final String resolvedOpName;
        final String declaredName = m.getDeclaredAnnotation(TaskOperation.class).schemeName();

        if(declaredName.isEmpty()) {
            resolvedOpName = m.getName();
        } else {
            resolvedOpName = declaredName;
        }

        return resolvedOpName;
    }

}
