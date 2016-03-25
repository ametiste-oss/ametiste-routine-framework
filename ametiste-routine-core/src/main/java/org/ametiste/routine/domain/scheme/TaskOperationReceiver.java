package org.ametiste.routine.domain.scheme;

import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.Map;

/**
 *
 * @since
 */
public interface TaskOperationReceiver {

    void addOperation(String name, ParamsProtocol params);

}
