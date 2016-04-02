package org.ametiste.routine.meta.scheme;

import org.ametiste.lang.Let;
import org.ametiste.routine.dsl.annotations.RoutineTask;
import org.ametiste.routine.dsl.annotations.SchemeMapping;
import org.ametiste.lang.object.trace.CallsTraceScaner;

import java.util.Collections;
import java.util.function.Consumer;

public class TaskMetaScheme<T> {

    private final Class<T> schemeType;
    private final String schemeName;

    public TaskMetaScheme(Class<T> schemeType) {

        if(!(schemeType.isAnnotationPresent(SchemeMapping.class)
                && schemeType.isAnnotationPresent(RoutineTask.class))) {
            throw new IllegalArgumentException("Only classes marked as @RoutineTask and @SchemeMapping are task scheme classes.");
        }

        this.schemeType = schemeType;
        this.schemeName = schemeType.getDeclaredAnnotation(SchemeMapping.class).schemeName();
    }

    public String schemeName() {
        return schemeName;
    }

    public Let<TaskSchemeTrace> trace(Consumer<T> callsProducer) {

        final TaskSchemeCallListener schemeCallListener =
                new TaskSchemeCallListener(schemeName);

        final CallsTraceScaner<T> traceScaner = new CallsTraceScaner<>(schemeType,
                Collections.singletonList(schemeCallListener)
        );

        traceScaner.createTrace().recordTrace(callsProducer);

        return schemeCallListener::schemeCall;

    }

    public static final <T> TaskMetaScheme<T> of(Class<T> taskScheme) {
        return new TaskMetaScheme<>(taskScheme);
    }

}
