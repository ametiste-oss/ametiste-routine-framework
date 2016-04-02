package org.ametiste.routine.infrastructure.protocol.containerapp;

import org.ametiste.routine.dsl.annotations.LambdaProtocol;
import org.ametiste.routine.sdk.protocol.containerapp.ContainerAppProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@LambdaProtocol
public class DirectSpringContainerAppConnection implements ContainerAppProtocol {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void systemOut(final String operationOut) {
        System.out.println(applicationContext.getStartupDate() + ">" + operationOut);
    }

    @Override
    public <T> T envProperty(final String propertyName, final Class<T> type) {
        return applicationContext.getEnvironment().getProperty(propertyName, type);
    }

}
