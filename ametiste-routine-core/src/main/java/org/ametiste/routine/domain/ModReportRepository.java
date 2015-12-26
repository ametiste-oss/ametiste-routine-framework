package org.ametiste.routine.domain;

import java.util.List;
import java.util.Map;

/**
 *
 * @since
 */
public interface ModReportRepository {

    void saveModReport(String modId, Map<String, String> report);

    List<Map<String, String>> loadModReports(String modId);

    List<Map<String, String>> loadModReports();

}
