package org.ametiste.routine.meta.scheme;

import org.ametiste.routine.dsl.annotations.OperationParameter;
import org.amtetiste.uttil.object.trace.MethodCallEvent;
import org.amtetiste.uttil.object.trace.MethodCallListener;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

class TaskSchemeCallListener implements MethodCallListener {

    final private String schemeName;

    // NOTE: at the moment we only trace call parameters, without actual call names
    final private List<Map<String, String>> calls = new ArrayList<>(4);

    public TaskSchemeCallListener(String schemeName) {
        this.schemeName = schemeName;
    }

    @Override
    public void methodCalled(final MethodCallEvent methodCallEvent) {
        int p = 0;

        final HashMap<String, String> callParams = new HashMap<>();

        // TODO: copypaste from TaskDSLConfiguration, generalize it mb?
        for (Annotation[] parameterType : methodCallEvent.method().getParameterAnnotations()) {
            // NOTE: take only first parameter annotation in account, other annotations just ignored
            if (parameterType[0] instanceof OperationParameter) {
                callParams.put(((OperationParameter) parameterType[0]).value(),
                        methodCallEvent.args()[p].toString());
            } else {
                throw new IllegalStateException("");
            }
            p++;
        }

        calls.add(callParams);

    }

    public void schemeCall(Consumer<TaskSchemeTrace> schemeCallConsumer) {
        schemeCallConsumer.accept(new TaskSchemeTrace(schemeName, calls));
    }

}