package org.ametiste.routine.infrastructure.mod;

import org.ametiste.routine.domain.ModReportRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @since
 */
// TODO: make it threadsafe and rework it, PoC purposes only usage
public class InMemoryModReportRepository implements ModReportRepository{

    private final ConcurrentHashMap<String, List<Map<String, String>>> reports = new ConcurrentHashMap<>();

    @Override
    public void saveModReport(final String modId, final Map<String, String> report) {
        final List<Map<String, String>> modReports =
                reports.computeIfAbsent(modId, k -> new ArrayList<Map<String, String>>());

        if (modReports.size() == 3) {
            modReports.remove(0);
            modReports.add(report);
        } else {
            modReports.add(report);
        }
    }

    @Override
    public List<Map<String, String>> loadModReports(final String modId) {
        return Collections.unmodifiableList(reports.getOrDefault(modId, Collections.emptyList()));
    }

    @Override
    public List<Map<String, String>> loadModReports() {

        final ArrayList<Map<String, String>> allReports = new ArrayList<>();

        reports.values().forEach(
            allReports::addAll
        );

        return allReports;

    }

}
