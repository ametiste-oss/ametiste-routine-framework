package org.ametiste.routine.sdk.protocol.operation;

import org.ametiste.laplatform.protocol.Protocol;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public interface ParamsProtocol extends Protocol {

    void fromMap(Map<String, String> params);

    Map<String, String> asMap();

}
