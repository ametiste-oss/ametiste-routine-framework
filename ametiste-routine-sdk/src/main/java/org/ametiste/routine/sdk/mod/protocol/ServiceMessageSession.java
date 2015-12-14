package org.ametiste.routine.sdk.mod.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @since
 */
public abstract class ServiceMessageSession<T> implements MessageSession {

    private Map<String, Map<String, String>> params = new HashMap<>();

    private GatewayCallback callback;

    private final T service;

    public ServiceMessageSession(T service) {
        this.service = service;
    }

    @Override
    public MessageSession params(Map<String, Map<String, String>> params) {
        this.params.putAll(params);
        return this;
    }

    @Override
    public MessageSession param(String name, String value) {
        this.params.computeIfAbsent(name, k -> new HashMap<String, String>()).put("value", value);
        return this;
    }

    @Override
    public MessageSession callback(GatewayCallback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public <T> T collect(GatewayResponseMapper<T> collector) {
        return collector.map(invokeService(service, params));
    }

    @Override
    public void end() {

        final Map<String, String> result = invokeService(service, params);

        if (callback !=null) {
            this.callback.call(result);
        }
    }

    protected abstract Map<String, String> invokeService(T service, Map<String, Map<String, String>> params);

}
