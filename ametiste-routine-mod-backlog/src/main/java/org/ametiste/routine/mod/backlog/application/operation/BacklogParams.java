package org.ametiste.routine.mod.backlog.application.operation;

import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

/**
 *
 * @since
 */
public interface BacklogParams extends ParamsProtocol {

    void schemeName(String schemeName);

    String schemeName();

}
