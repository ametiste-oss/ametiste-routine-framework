package org.ametiste.routine.mod.backlog.application.operation;

import org.ametiste.routine.sdk.protocol.operation.GuardedParamProtocol;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @since 1.1
 */
public class DirectBacklogParams extends GuardedParamProtocol implements BacklogParams {

    private static final String BACKLOGED_SCHEME_NAME = "backlog.scheme.name";

    private static final List<String> DEFINED_PARAMS = Arrays.asList(
        BACKLOGED_SCHEME_NAME
    );

    public DirectBacklogParams() {
        super(DEFINED_PARAMS);
    }

    public DirectBacklogParams(final Map<String, String> params) {
        super(DEFINED_PARAMS, params);
    }

    @Override
    public void schemeName(String schemeName) {
        addParam(BACKLOGED_SCHEME_NAME, schemeName);
    }

    @Override
    public String schemeName() {
        return takeParam(BACKLOGED_SCHEME_NAME);
    }

}
