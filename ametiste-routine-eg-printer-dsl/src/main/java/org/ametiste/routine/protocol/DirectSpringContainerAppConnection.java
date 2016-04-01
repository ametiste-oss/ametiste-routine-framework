package org.ametiste.routine.protocol;

import org.ametiste.routine.dsl.annotations.LambdaProtocol;
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

}
