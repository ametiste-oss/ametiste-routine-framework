package org.ametiste.routine.dsl.configuration.protocol;

import org.ametiste.laplatform.sdk.protocol.GatewayContext;
import org.ametiste.laplatform.sdk.protocol.Protocol;
import org.ametiste.laplatform.sdk.protocol.ProtocolFactory;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 *     This factory creates instance of the specified protocol dynamicaly, using
 *     the given protocol class.
 * </p>
 *
 * <p>
 *     Used by {@link ProtocolDSLConfiguration} to register dsl-described protocols.
 * </p>
 *
 * <p>
 *     Note, this factory defines protocol valueType explicitly overriding the default factory interface
 *     behavior of {@link ProtocolFactory#protocolType()}.
 * </p>
 *
 * @see ProtocolDSLConfiguration
 * @see ProtocolFactory
 * @param <T> protocol valueType
 * @since 1.1
 */
class DynamicProtocolFactory<T extends Protocol> implements ProtocolFactory<T> {

    private final Class<T> protocolType;
    private final ApplicationContext applicationContext;

    public DynamicProtocolFactory(Class<T> protocolType, ApplicationContext applicationContext) {
        this.protocolType = protocolType;
        this.applicationContext = applicationContext;
    }

    @Override
    public T createProtocol(final GatewayContext gatewayContext) {
        try {
            final T protocol = (T) protocolType.newInstance();
            applicationContext.getAutowireCapableBeanFactory().autowireBean(protocol);
            return protocol;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<T> protocolType() {
        return protocolType;
    }
}