package org.ametiste.routine.dsl.application;

import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.Collections;
import java.util.Map;

public class DynamicParamsProtocol implements ParamsProtocol {

    private Map<String, String> params;

    public DynamicParamsProtocol() {
        this(Collections.emptyMap());
    }

    public DynamicParamsProtocol(final Map<String, String> params) {
        fromMap(params);
    }

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