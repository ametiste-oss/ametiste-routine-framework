package org.ametiste.routine.dsl.infrastructure.protocol;

import org.ametiste.routine.dsl.application.DynamicParamsProtocol;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.Collections;
import java.util.Map;

// TODO: add metrics interface, provide client identifer to metrics
public class DirectDynamicParamsProtocol implements DynamicParamsProtocol {

    private Map<String, String> params;

    public DirectDynamicParamsProtocol() {
        this(Collections.emptyMap());
    }

    public DirectDynamicParamsProtocol(final Map<String, String> params) {
        fromMap(params);
    }

    @Override
    public Object param(String paramName) {
        return params.get(paramName);
    }

    @Override
    public void fromMap(final Map<String, String> params) {
        this.params = params;
    }

    @Override
    public Map<String, String> asMap() {
        return Collections.unmodifiableMap(params);
    }

    @Override
    public <T extends ParamsProtocol> void proxy(final T params) {
        this.params.putAll(params.asMap());
    }
}