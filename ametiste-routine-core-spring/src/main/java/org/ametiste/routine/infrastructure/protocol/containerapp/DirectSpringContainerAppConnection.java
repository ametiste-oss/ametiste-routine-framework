package org.ametiste.routine.infrastructure.protocol.containerapp;

import org.ametiste.laplatform.dsl.LambdaProtocol;
import org.ametiste.laplatform.dsl.ProtocolMeta;
import org.ametiste.routine.sdk.protocol.containerapp.ContainerAppProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@LambdaProtocol
@ProtocolMeta(group = "core.infrastructure.protocol", shortName = "spring-app-direct")
public class DirectSpringContainerAppConnection implements ContainerAppProtocol {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void systemOut(final String operationOut) {
        System.out.println(applicationContext.getStartupDate() + ">" + operationOut);
    }

    @Override
    @ProtocolMeta(shortName = "env-prop")
    public <T> T envProperty(final String propertyName, final Class<T> type) {
        return applicationContext.getEnvironment().getProperty(propertyName, type);
    }

    @Override
    @ProtocolMeta(shortName = "object")
    public <T> T object(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    @Override
    @ProtocolMeta(shortName = "named-object")
    public <T> T namedObject(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }
}
