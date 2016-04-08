package org.ametiste.routine.dsl.application;

import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

/**
 *
 * @since
 */
public interface DynamicParamsProtocol extends ParamsProtocol {
    Object param(String paramName);
}
