package org.ametiste.routine.mod.backlog.dsl;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.laplatform.sdk.protocol.Protocol;
import org.ametiste.routine.dsl.annotations.Connect;
import org.ametiste.routine.dsl.annotations.SchemeMapping;
import org.ametiste.routine.dsl.application.DynamicTaskService;
import org.ametiste.routine.meta.scheme.TaskMetaScheme;
import org.ametiste.routine.meta.util.MetaMethod;
import org.ametiste.routine.meta.util.MetaObject;
import org.ametiste.routine.mod.backlog.domain.Backlog;
import org.ametiste.routine.mod.backlog.domain.BacklogRepository;
import org.ametiste.routine.mod.backlog.dsl.annotations.BacklogController;
import org.ametiste.routine.mod.backlog.dsl.annotations.BacklogPopulator;
import org.ametiste.routine.mod.backlog.infrastructure.BacklogPopulationStrategiesRegistry;
import org.ametiste.routine.mod.backlog.infrastructure.BacklogPopulationStrategy;
import org.ametiste.routine.mod.backlog.mod.ModBacklog;
import org.ametiste.routine.sdk.mod.ModGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 *
 * @since
 */
@Configuration
@ConditionalOnBean(ModBacklog.class)
public class BacklogDSLConfiguration {

    @BacklogController
    @Autowired(required = false)
    private List<Object> backlogControllers;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private BacklogPopulationStrategiesRegistry populationStrategiesRegistry;

    @Autowired
    private DynamicTaskService dynamicTaskService;

    @Bean
    public ModGateway modBacklogDSL() {

        final List<? extends Class<?>> backlogControllers = this.backlogControllers.stream()
                .map(Object::getClass)
                .collect(Collectors.toList());

        backlogControllers.forEach(this::createBacklogEntry);

        return gw -> {
            // TODO: how can I propagate artifact version?
            gw.modInfo("dsl-backlog", "1.1",
                backlogControllers.stream().collect(Collectors.toMap(s -> s.getName(), s -> ""))
            );
        };
    }

    private void createBacklogEntry(final Class<? extends Object> controllerClass) {

        // NOTE: this meta-class is used to validate controller object
        // Each population strategy call will create their own instance of the controller
        final MetaObject backlogController = MetaObject.from(controllerClass);

        backlogController.assertTypeAnnotation(BacklogController.class);

        final Class<?> taskScheme = backlogController
            .annotationValue(SchemeMapping.class, SchemeMapping::schemeClass)
            .orElseThrow(processError(controllerClass, "@BacklogController annotation is required."));

        backlogController.oneAnnotatedMethod(BacklogPopulator.class)
                .orElseThrow(processError(controllerClass, "Exactly one @BacklogPopulator method are expected."))
                .assertParameterTypes(taskScheme);

        final String schemeName = MetaObject.from(taskScheme)
                .annotationValue(SchemeMapping.class, SchemeMapping::schemeName)
                .orElseThrow(processError(controllerClass, "@SchemeMapping of " +
                        "@BacklogController is required."));

        final String strategyName = schemeName + "-population";

        final BacklogPopulationStrategy populationStrategy = createStrategy(controllerClass, taskScheme);

        backlogRepository.save(new Backlog(schemeName, strategyName));
        populationStrategiesRegistry.registerPopulationStrategy(strategyName, populationStrategy);
    }

    private BacklogPopulationStrategy createStrategy(Class<?> backlogController, Class<?> taskScheme) {

        // TODO: must be registered within DSLCreatedRegistry
        // TODO: can we optimize it? alot of reflection
        // TODO: extract to class, DynamicPopulationStrategy or DSLPopulationStrategy
        return gw -> {

            final MetaObject metaObject = MetaObject.from(backlogController);

            final TaskMetaScheme<?> metaScheme = metaObject
                    .annotationValue(SchemeMapping.class, SchemeMapping::schemeClass)
                    .map(TaskMetaScheme::of)
                    .orElseThrow(() -> new RuntimeException("Can't obtain backlog populator mapping"));

            final MetaMethod metaMethod = metaObject.oneAnnotatedMethod(BacklogPopulator.class)
                    .orElseThrow(processError(backlogController, "Exactly one @BacklogPopulator method are expected."));

            connectProtocols(gw, metaObject);

            // NOTE: invokes @BacklogPopulator method with the traced instance of
            // @SchemeMapping::schemeClass.
            metaScheme.trace(metaMethod::invoke).let(
                scheme -> scheme.params().forEach(p ->
                    dynamicTaskService.issueTask(scheme.name(), p, "mod::backlog")
                )
            );
        };
    }

    private static Supplier<IllegalStateException> processError(final Class<? extends Object> backlogControllerClass, String message) {
        return () -> new IllegalStateException("Error during " + backlogControllerClass.getName() + " processing: " + message);
    }

    // TODO: copypaste with TaskDSLConfiguration
    private void connectProtocols(ProtocolGateway protocolGateway, MetaObject metaObject) {
        metaObject.streamOfAnnotatedFields(Connect.class).forEach(
            f -> {
                // TODO: add exception, if @Connect does not point on LambdaProtocol field
                final Protocol session = protocolGateway
                        .session((Class<? extends Protocol>) f.getType());

                ReflectionUtils.makeAccessible(f);
                ReflectionUtils.setField(f, metaObject.object(), session);
            }
        );
    }

}
