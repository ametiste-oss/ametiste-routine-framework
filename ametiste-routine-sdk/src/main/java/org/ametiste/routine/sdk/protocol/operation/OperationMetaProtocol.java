package org.ametiste.routine.sdk.protocol.operation;

import org.ametiste.laplatform.sdk.protocol.Protocol;

import java.util.UUID;

/**
 *
 * @since
 */
public interface OperationMetaProtocol extends Protocol {

    UUID operationId();

    String operationName();

}
