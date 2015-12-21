package org.ametiste.routine.infrastructure.protocol.moddata;

import org.ametiste.metrics.annotations.markers.MetricsInterface;
import org.ametiste.routine.sdk.protocol.moddata.ModDataProtocol;

/**
 * <p>
 *     Set of metrics for {@link DirectModDataConnection} implementation of {@link ModDataProtocol}.
 * </p>
 *
 * @since 0.1.0
 */
@MetricsInterface
public interface DirectModDataConnectionMetrics {

    String __PREFIX = "core.infrastructure.protocol.direct-mod-data";

    String __OVERALL_PREFIX = __PREFIX + ".overall";

    String __LOAD_TIMING = ".load.timing";

    String __STORE_TIMING = ".store.timing";


    String CLIENTS_PREFIX = __PREFIX + ".clients";

    String OVERAL_LOAD_TIMING = __OVERALL_PREFIX + __LOAD_TIMING;

    String OVERAL_STORE_TIMING = __OVERALL_PREFIX + __STORE_TIMING;

    String CLIENT_STORE_TIMING = "target.clientId + '" + __STORE_TIMING + "'";

    String CLIENT_LOAD_TIMING = "target.clientId + '" + __LOAD_TIMING + "'";

    /**
     * <p>
     *     Returns current protocol client identifier.
     * </p>
     *
     * @return client identifier, can't be null.
     */
    String getClientId();

}
