package org.ametiste.routine.mod.backlog.application.operation;

import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public class BacklogParams implements ParamsProtocol {

    private String schemeName;

    public void schemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String schemeName() {
        return schemeName;
    }

    @Override
    public Map<String, String> asMap() {
        return Collections.singletonMap("schemeName", schemeName);
    }

    @Override
    public void fromMap(Map<String, String> params) {
        schemeName = params.get("schemeName");
    }

    public static BacklogParams createFromMap(Map<String, String> params) {
        final BacklogParams backlogParams = new BacklogParams();
        backlogParams.fromMap(params);
        return backlogParams;
    }

}
