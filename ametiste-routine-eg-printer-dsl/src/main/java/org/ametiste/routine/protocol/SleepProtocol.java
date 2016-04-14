package org.ametiste.routine.protocol;

import org.ametiste.laplatform.sdk.protocol.Protocol;

/**
 *
 * @since
 */
public interface SleepProtocol extends Protocol {

    void sleep(long period);

}
