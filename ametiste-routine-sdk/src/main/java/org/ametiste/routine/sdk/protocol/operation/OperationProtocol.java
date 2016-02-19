package org.ametiste.routine.sdk.protocol.operation;

import org.ametiste.laplatform.protocol.Protocol;

import java.util.UUID;

/**
 *
 * @since
 */
public interface OperationProtocol extends Protocol {

    UUID operationId();

}
