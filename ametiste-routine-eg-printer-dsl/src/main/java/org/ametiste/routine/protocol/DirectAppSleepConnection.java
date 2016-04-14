package org.ametiste.routine.protocol;

import org.ametiste.laplatform.dsl.LambdaProtocol;
import org.ametiste.laplatform.dsl.ProtocolMeta;

/**
 *
 * @since
 */
@LambdaProtocol
@ProtocolMeta(group = "mod.sleep", shortName = "sleep-protocol")
public class DirectAppSleepConnection implements SleepProtocol {

    @Override
    public void sleep(final long period) {
        try {
            Thread.sleep(period);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
