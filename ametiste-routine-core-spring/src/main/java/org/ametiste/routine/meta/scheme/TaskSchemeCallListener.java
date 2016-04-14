package org.ametiste.routine.meta.scheme;

import org.ametiste.routine.dsl.annotations.OperationParameter;
import org.ametiste.lang.object.trace.MethodCallEvent;
import org.ametiste.lang.object.trace.MethodCallListener;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

class TaskSchemeCallListener implements MethodCallListener {

    final private String schemeName;
    private final ParamValueConverter converter;

    // NOTE: at the moment we only trace call parameters, without actual call names
    final private List<TaskSchemeCall> calls = new ArrayList<>(4);

    public TaskSchemeCallListener(String schemeName, ParamValueConverter converter) {
        this.schemeName = schemeName;
        this.converter = converter;
    }

    @Override
    public void methodCalled(final MethodCallEvent methodCallEvent) {
        int p = 0;

        final HashMap<String, String> callParams = new HashMap<>();

        // TODO: copypaste from TaskDSLConfiguration, generalize it somehow
        for (Annotation[] parameterType : methodCallEvent.method().getParameterAnnotations()) {
            // NOTE: take only first parameter annotation in account, other annotations just ignored
            if (parameterType[0] instanceof OperationParameter) {
                final String name = ((OperationParameter) parameterType[0]).value();
                final String value = converter.convert(methodCallEvent.args()[p]);
                callParams.put(name, value);
            } else {
                throw new IllegalStateException("");
            }
            p++;
        }

        calls.add(TaskSchemeCall.of(callParams));

    }

    public void provideTracedCallScheme(Consumer<TaskSchemeTrace> schemeCallConsumer) {
        schemeCallConsumer.accept(new TaskSchemeTrace(schemeName, calls));
    }

}
