package org.ametiste.routine.dsl.configuration.task;

import org.ametiste.dynamics.DynamicValueProvider;
import org.ametiste.dynamics.Surface;
import org.ametiste.lang.Pair;
import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.application.service.issue.NamedTaskSchemeService;
import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.domain.scheme.SchemeRepository;
import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.dsl.annotations.*;
import org.ametiste.routine.dsl.application.DynamicOperationScheme;
import org.ametiste.routine.dsl.application.DynamicTaskScheme;
import org.ametiste.routine.dsl.infrastructure.protocol.DynamicParamsProtocolRuntime;
import org.ametiste.routine.sdk.mod.ModGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.ametiste.dynamics.foundation.structure.ReflectFoundation.*;
import static org.ametiste.routine.dsl.application.RoutineTaskDSLStructures.RoutineDSLSurface;

/**
 * Configures dsl-based task schemas.
 *
 * <p>
 *     In general, this configuration module is <i>Task DSL</i> core module, for the moment
 *     of 1.1.
 * </p>
 *
 * <p>
 *     All initialization logis is under the {@link #modTaskSchemeDSL()} method,
 *     so, note, atm there is no special place to hold it.
 * </p>
 *
 * @since 1.1
 */
@Configuration
public class TaskSchemeDSLConfiguration {

    @Autowired(required = false)
    @RoutineTask
    private List<Object> taskControllers = emptyList();

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private TaskIssueService taskIssueService;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    @ParamValueProvider
    private List<DynamicValueProvider<ProtocolGateway>> paramProviders;

    @Autowired
    @FieldValueProvider
    private List<DynamicValueProvider<ProtocolGateway>> fieldProviders;

    private ApplicationContext applicationContext;

    @Bean
    public DynamicParamsProtocolRuntime dynamicParamsProtocolRuntimeFactory() {
        return new DynamicParamsProtocolRuntime();
    }

    @Bean
    public NamedTaskSchemeService dynamicTaskService() {
        return new NamedTaskSchemeService(schemeRepository, taskIssueService);
    }

    @Bean
    public ModGateway modTaskSchemeDSL() {

        final List<TaskScheme> schemes = taskControllers.stream()
                .map(Object::getClass)
                .map(this::mapToTaskScheme)
                .collect(toList());

        schemes.forEach(schemeRepository::saveScheme);

        return gw -> {
            // TODO: how can I propagate artifact version?
            gw.modInfo("dsl-task-scheme", "1.1",
                schemes.stream().collect(Collectors.toMap(s -> s.schemeName(), s -> s.getClass().getName())),
                emptyList()
            );
        };
    }

    public static class SpringAppContextPreSurface implements RuntimePreSurface {

        private final ApplicationContext appContext;

        public SpringAppContextPreSurface(final ApplicationContext appContext) {
            this.appContext = appContext;
        }

        @Override
        public void findClasses(final UnaryOperator<RuntimePattern> matcher, final Consumer<Class<?>> consumer) {
            RuntimePattern.over(matcher).let((annotation, annotations) ->
                appContext.getBeansWithAnnotation(annotation).values().stream()
                    .filter(o -> Stream.of(annotations).allMatch(o.getClass()::isAnnotationPresent))
                    .map(Object::getClass)
                    .forEach(consumer)
            );
        }

    }

    private TaskScheme mapToTaskScheme(Class<?> controllerClass) {

        Surface.byPreSurface(
            RuntimePreSurface.definedBy(new SpringAppContextPreSurface(applicationContext))
        ).depictSurface(ClassPoolSurface::new, pool ->
            pool.depictFeatures(RoutineDSLSurface::new, dsl ->
                dsl.tasks(task -> task.operations(
                        op -> createDynamicOperationScheme(op.controller(), op.name(), op.method())
                    )
                )
            )
        );

/// TODO: могу ли я построить вот такое описание? Какие должны быть поверхности для этого?
//        runtimeSurface.depictSurface(ClassPoolSurface::new).ifPresent(pool ->
//            pool.depictSurface(RoutineDSLSurface::new).ifPresent(dsl ->
//                dsl.depictSurface(RoutineTaskSurface::new).ifPresent(task ->
//                    task.depictSurface(RoutineOperationSurface::new).ifPresent(
//                        opSurface -> opSurface.feature(
//                            op -> createDynamicOperationScheme(op.type(), op.name(), op.method())
//                        )
//                    )
//                )
//            )
//        );

//        runtimeSurface.depictFeatures(ClassPoolSurface::new).ifPresent(pool ->
//            pool.peekClass(
//                that -> that.hasAnnotations(RoutineTask.class, SchemeMapping.class),
//                RoutineTaskSurface::new,
//                (klass, task) -> {
//                    task.operations(op -> createDynamicOperationScheme(klass.type(), op.name(), op.method()));
//                }
//            )
//        );
//
//        // TODO: думаю в такой метод можно будет упаковать весь трейс от RuntimeSurface
//        RoutineDSLSurface.enclosedBy(runtimeSurface).peekTasks(t ->
//            t.peekOperation(o -> createDynamicOperationScheme(controllerClass, t.name(), o.method()))
//        );

        if (!controllerClass.isAnnotationPresent(RoutineTask.class)) {
            throw new IllegalArgumentException("Only @RoutineTask classes are allowed.");
        }

        final String schemeName = controllerClass
                .getDeclaredAnnotation(SchemeMapping.class)
                .schemeName();

        final List<DynamicOperationScheme> operations = Stream.of(controllerClass.getDeclaredMethods())
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
                .map(m -> Pair.of(m, resolveOperationName(m)))
                .map(p -> createDynamicOperationScheme(controllerClass, p.second, p.first))
                .collect(toList());

        operations.forEach(schemeRepository::saveScheme);

        return new DynamicTaskScheme(schemeName, operations);
    }

    private DynamicOperationScheme createDynamicOperationScheme(final Class<?> controllerClass, String schemeName, Method schemeMethod) {
        return new DynamicOperationScheme(schemeName,
                controllerClass, schemeMethod, fieldProviders, paramProviders);
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
