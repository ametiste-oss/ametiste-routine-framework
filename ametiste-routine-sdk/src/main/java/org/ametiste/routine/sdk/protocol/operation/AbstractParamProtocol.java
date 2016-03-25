package org.ametiste.routine.sdk.protocol.operation;

import java.util.*;
import java.util.function.Function;

/**
 *
 * @since
 */
abstract public class AbstractParamProtocol implements ParamsProtocol {

    private final List<String> definedParams;
    private Map<String, String> paramsMap;

    public AbstractParamProtocol(List<String> definedParams) {
        this.definedParams = definedParams;
        this.paramsMap = new HashMap<>();
    }

    protected AbstractParamProtocol(List<String> definedParams, Map<String, String> params) {
        this(definedParams);
        fromMap(params);
    }

    @Override
    public void fromMap(final Map<String, String> params) {
        params.keySet().forEach(this::definedParametersGuard);
        paramsMap = Collections.unmodifiableMap(params);
    }

    @Override
    public Map<String, String> asMap() {
        paramsMap.keySet().forEach(this::definedParametersGuard);
        return Collections.unmodifiableMap(paramsMap);
    }

    @Override
    public <T extends ParamsProtocol> void proxy(final T params) {
        paramsMap.keySet().forEach(this::definedParametersGuard);
        params.fromMap(asMap());
    }

    protected void addParam(String name, String value) {
        definedParametersGuard(name);
        paramsMap.put(name, value);
    }

    protected String takeParam(String name) {
        definedParametersGuard(name);
        return paramsMap.get(name);
    }

    protected <R> R takeParam(String name, Function<String, R> mapper) {
        return mapper.apply(paramsMap.get(name));
    }

    private void definedParametersGuard(final String name) {
        if (!definedParams.contains(name)){
            throw new IllegalStateException("Parameter action error. Parameter witih the name " + name + " " +
                    "is unexpected for the given params protocol. Expected parameters are: " + definedParams);
        }
    }

}
