package org.ametiste.routine.dsl.configuration.task;

import org.ametiste.dynamics.Surface;
import org.ametiste.dynamics.foundation.elements.AnnotatedRefProcessor;
import org.ametiste.dynamics.foundation.spring.SpringAppContextDynamicsRuntime;
import org.ametiste.routine.domain.scheme.SchemeRepository;
import org.ametiste.routine.dsl.annotations.FieldValueProvider;
import org.ametiste.routine.dsl.annotations.ParamValueProvider;
import org.ametiste.routine.dsl.application.DynamicOperationScheme;
import org.ametiste.routine.dsl.application.DynamicTaskScheme;
import org.ametiste.routine.dsl.domain.surface.RoutineDSLStructure;
import org.ametiste.routine.dsl.infrastructure.protocol.DynamicParamsProtocolRuntime;
import org.ametiste.routine.sdk.mod.ModGateway;
import org.ametiste.routine.sdk.mod.ModInfoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

import org.ametiste.dynamics.foundation.reflection.structures.ClassPool;

import static org.ametiste.lang.AmeCollections.entry;
import static org.ametiste.lang.AmeCollections.readOnlyList;
import static org.ametiste.lang.AmeCollections.readOnlyMap;

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

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    @ParamValueProvider
    private List<AnnotatedRefProcessor> params;

    @Autowired
    @FieldValueProvider
    private List<AnnotatedRefProcessor> fields;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public DynamicParamsProtocolRuntime dynamicParamsProtocolRuntimeFactory() {
        return new DynamicParamsProtocolRuntime();
    }

    @Bean
    public ModGateway modTaskSchemeDSL() {

        final Map<String, String> opSchemas = new HashMap<>();

        /*

        Plnanar.surfaceOver(
            DynamicsRuntime.definedBy(new SpringAppContextDynamicsRuntime(applicationContext))
        ).forms(fields, params).
        .depict(ClassPool::new, pool ->

        )

         */

        Surface.ofPreSurface(
            new SpringAppContextDynamicsRuntime(applicationContext)
        ).depict(ClassPool::new, pool ->
            pool.map(e -> new RoutineDSLStructure(e, fields, params), dsl ->
                dsl.tasks(task ->
                    schemeRepository.saveScheme(
                        new DynamicTaskScheme(
                            task.name(),
                            task.operations()
                                .peek(op -> opSchemas.put(op.name(), op.actionName()))
                                .map(DynamicOperationScheme::new)
                                .peek(schemeRepository::saveScheme)
                                .collect(toList())
                        )
                    )
                )
            )
        );

        return gw -> {
            // TODO: how can I propagate artifact version?
            gw.modInfo("dsl-task-scheme", "1.1",
                readOnlyMap(
                    entry("repository-used", schemeRepository.getClass().getName())
                ),
                readOnlyList(
                    // TODO: add more info, I want to see task controller classes and operation methods
                    // Also I want to see task-schemas filled with operation lists
                    // May be something else
                    ModInfoProvider.basic("task-schemas", readOnlyMap()),
                    ModInfoProvider.basic("operation-schemas", readOnlyMap(opSchemas)),
                    ModInfoProvider.basic("value-providers", readOnlyMap(
                            entry("field-values", fields.stream().map(this::extractClassName).collect(toList())),
                            entry("param-values", params.stream().map(this::extractClassName).collect(toList()))
                    ))
                )
            );
        };
    }

    private String extractClassName(Object object) {
        return object.getClass().getName();
    }

}
