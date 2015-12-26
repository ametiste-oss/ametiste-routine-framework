package org.ametiste.routine.infrastructure.protocol.modreport;

import org.ametiste.routine.domain.ModReportRepository;
import org.ametiste.routine.sdk.protocol.modreport.ModReportProtocol;
import org.ametiste.routine.sdk.protocol.modreport.ReportForm;

import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

/**
 *
 * @since
 */
public class DirectModReportConnection implements ModReportProtocol {

    private final String clientId;
    private final ModReportRepository reportRepository;

    private final static class SimpleReportForm implements ReportForm {

        private static final List<String> reserved = Arrays.asList(
            "report.type", "report.date", "report.clientId"
        );

        private Map<String, String> report = new HashMap<>();

        public SimpleReportForm(String clientId) {
            report.put("report.date", Instant.now().toString());
            report.put("report.clientId", clientId);
        }

        @Override
        public void type(final String reportType) {
            report.put("report.type", reportType);
        }

        @Override
        public void data(final String name, final String value) {

            if (reserved.contains(name)) {
                throw new IllegalArgumentException("Reserved data field name: " + name);
            }

            report.put(name, value);

        }

    }

    public DirectModReportConnection(String clientId, ModReportRepository reportRepository) {
        this.clientId = clientId;
        this.reportRepository = reportRepository;
    }

    @Override
    public void submitReport(final Consumer<ReportForm> reportBuilder) {
        final SimpleReportForm simpleReportForm = new SimpleReportForm(clientId);
        reportBuilder.accept(simpleReportForm);
        reportRepository.saveModReport(clientId, simpleReportForm.report);
    }
}
