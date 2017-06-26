package org.ametiste.routine.sdk.protocol.operation;

import java.util.List;
import java.util.Map;

/**
 *
 * @since
 */
abstract public class GuardedParamProtocol extends AbstractParamProtocol {

    private final List<String> definedParams;

    public GuardedParamProtocol(List<String> definedParams) {
        super();
        this.definedParams = definedParams;
    }

    protected GuardedParamProtocol(List<String> definedParams, Map<String, String> params) {
        super();
        this.definedParams = definedParams;
        fromMap(params);
    }

    @Override
    public void fromMap(final Map<String, String> params) {
        params.keySet().forEach(this::definedParametersGuard);
        super.fromMap(params);
    }

    @Override
    public Map<String, String> asMap() {
        paramsMap.keySet().forEach(this::definedParametersGuard);
        return super.asMap();
    }

    @Override
    public <T extends ParamsProtocol> void proxy(final T params) {
        paramsMap.keySet().forEach(this::definedParametersGuard);
        super.proxy(params);
    }

    protected void addParam(String name, String value) {
        definedParametersGuard(name);
        super.addParam(name, value);
    }

    protected String takeParam(String name) {
        definedParametersGuard(name);
        return super.takeParam(name);
    }

    private void definedParametersGuard(final String name) {
        if (!definedParams.contains(name)){
            throw new IllegalStateException("Parameter action error. Parameter witih the name " + name + " " +
                    "is unexpected for the given params protocol. Expected parameters are: " + definedParams);
        }
    }

}
