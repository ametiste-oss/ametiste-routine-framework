package org.ametiste.routine.sdk.protocol.containerapp;

import org.ametiste.laplatform.protocol.Protocol;

public interface ContainerAppProtocol extends Protocol {

    void systemOut(String operationOut);

    <T> T envProperty(String propertyName, Class<T> type);

}