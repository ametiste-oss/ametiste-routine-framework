package org.ametiste.routine.sdk.protocol.operation;

import org.ametiste.laplatform.sdk.protocol.Protocol;

import java.util.UUID;

/**
 * <p>
 *     Provides access to operation runtime meta-data, such enclosing task id,
 *     operation id and operation runtime name.
 * </p>
 *
 * @since 1.1
 */
public interface OperationMetaProtocol extends Protocol {

    /**
     * <p>
     *     Enclosing task identifier.
     * </p>
     *
     * @return task identifier, can't be null.
     */
    UUID taskId();

    /**
     * <p>
     *     Current operation identifier.
     * </p>
     *
     * @return operation identifier, can't be null.
     */
    UUID operationId();

    /**
     * <p>
     *     Current operation name.
     * </p>
     *
     * @return operation name, can't be null.
     */
    String operationName();

}
