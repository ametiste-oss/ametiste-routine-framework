package org.ametiste.routine.sdk.protocol.modreport;

import java.time.Instant;

/**
 *
 * @since
 */
public interface ReportForm {

    void type(String reportType);

    void data(String name, String value);

}
