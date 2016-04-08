package org.ametiste.routine.mod.shredder.application.operation;

import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 *
 * @since
 */
public interface ShreddingParams extends ParamsProtocol {
    Integer threshold();

    ChronoUnit unit();

    List<String> staleStates();

    void staleStates(List<String> staleStates);

    void staleThresholdValue(int staleThresholdValue);

    void staleThresholdUnit(ChronoUnit staleThresholdUnit);
}
