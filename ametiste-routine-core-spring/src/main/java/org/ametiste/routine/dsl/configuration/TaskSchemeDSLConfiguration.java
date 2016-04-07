package org.ametiste.routine.dsl.configuration;

import org.ametiste.lang.Pair;
import org.ametiste.laplatform.protocol.GatewayContext;
import org.ametiste.laplatform.protocol.ProtocolFactory;
import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.domain.scheme.SchemeRepository;
import org.ametiste.routine.domain.scheme.TaskBuilder;
import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.domain.scheme.TaskSchemeException;
import org.ametiste.routine.dsl.annotations.RoutineTask;
import org.ametiste.routine.dsl.annotations.SchemeMapping;
import org.ametiste.routine.dsl.annotations.TaskOperation;
import org.ametiste.routine.dsl.application.DynamicOperationFactory;
import org.ametiste.routine.dsl.application.DynamicOperationScheme;
import org.ametiste.routine.dsl.application.DynamicParamsProtocol;
import org.ametiste.routine.dsl.application.DynamicTaskService;
import org.ametiste.routine.sdk.mod.ModGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
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

    @Bean
    @Scope(scopeName = "prototype")
    public DynamicParamsProtocol dynamicParamsProtocol(GatewayContext context) {
        return new DynamicParamsProtocol(context.lookupMap("params"));
    }

    @Bean
    public ProtocolFactory<DynamicParamsProtocol> dynamicParamsProtocolConnection() {
        return c -> dynamicParamsProtocol(c);
    }

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
                .map(m -> Pair.of(resolveOperationName(m), new DynamicOperationFactory(controllerClass, m)))
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

                final DynamicParamsProtocol dynamicParamsProtocol = new DynamicParamsProtocol();

                paramsInstaller.accept(dynamicParamsProtocol);

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
