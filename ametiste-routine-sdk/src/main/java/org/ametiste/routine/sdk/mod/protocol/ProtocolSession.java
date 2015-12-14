package org.ametiste.routine.sdk.mod.protocol;

import java.util.Map;
import java.util.function.Function;

/**
 *
 * @since
 */
public interface ProtocolSession {

    MessageSession message(String messageType);

}
