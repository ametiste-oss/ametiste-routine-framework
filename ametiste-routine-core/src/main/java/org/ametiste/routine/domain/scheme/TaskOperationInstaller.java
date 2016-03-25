package org.ametiste.routine.domain.scheme;

import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.Map;

/**
 *
 * @since
 */
public interface TaskOperationInstaller {

    void addOperation(String name, ParamsProtocol params);

}
