package org.ametiste.routine.sdk.protocol.operation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @since
 */
public abstract class AbstractParamProtocol implements ParamsProtocol {

    protected Map<String, String> paramsMap;

    public AbstractParamProtocol() {
        this.paramsMap = new HashMap<>();
    }

    @Override
    public void fromMap(final Map<String, String> params) {
        paramsMap = Collections.unmodifiableMap(params);
    }

    @Override
    public Map<String, String> asMap() {
        return Collections.unmodifiableMap(paramsMap);
    }

    @Override
    public <T extends ParamsProtocol> void proxy(final T params) {
        params.fromMap(asMap());
    }

    protected void addParam(String name, String value) {
        paramsMap.put(name, value);
    }

    protected String takeParam(String name) {
        return paramsMap.get(name);
    }

    protected <R> R takeParam(String name, Function<String, R> mapper) {
        return mapper.apply(paramsMap.get(name));
    }
}
