package org.ametiste.routine.meta.scheme;

import org.ametiste.lang.Elective;
import org.ametiste.routine.dsl.annotations.RoutineTask;
import org.ametiste.routine.dsl.annotations.SchemeMapping;
import org.ametiste.lang.object.trace.CallsTraceScaner;

import java.util.Collections;
import java.util.function.Consumer;

public class TaskMetaScheme<T> {

    private final Class<T> schemeType;
    private final String schemeName;
    private final ParamValueConverter converter;

    public TaskMetaScheme(Class<T> schemeType, ParamValueConverter converter) {

        if(!(schemeType.isAnnotationPresent(SchemeMapping.class)
                && schemeType.isAnnotationPresent(RoutineTask.class))) {
            throw new IllegalArgumentException("Only classes marked as @RoutineTask and @SchemeMapping are task scheme classes.");
        }

        this.converter = converter;
        this.schemeType = schemeType;
        this.schemeName = schemeType.getDeclaredAnnotation(SchemeMapping.class).schemeName();
    }

    public String schemeName() {
        return schemeName;
    }

    public Elective<TaskSchemeTrace> trace(Consumer<T> callsProducer) {

        final TaskSchemeCallListener schemeCallListener =
                new TaskSchemeCallListener(schemeName, converter);

        final CallsTraceScaner<T> traceScaner = new CallsTraceScaner<>(schemeType,
                Collections.singletonList(schemeCallListener)
        );

        traceScaner.createTrace().recordTrace(callsProducer);

        return schemeCallListener::provideTracedCallScheme;

    }

    public static final <T> TaskMetaScheme<T> of(Class<T> taskScheme, ParamValueConverter converter) {
        return new TaskMetaScheme<>(taskScheme, converter);
    }

}
