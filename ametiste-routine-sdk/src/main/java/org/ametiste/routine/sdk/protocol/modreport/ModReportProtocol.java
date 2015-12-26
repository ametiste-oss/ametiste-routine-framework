package org.ametiste.routine.sdk.protocol.modreport;

import org.ametiste.laplatform.protocol.Protocol;

import java.util.function.Consumer;

/**
 *
 * @since
 */
public interface ModReportProtocol extends Protocol {

    void submitReport(Consumer<ReportForm> reportBuilder);

}
