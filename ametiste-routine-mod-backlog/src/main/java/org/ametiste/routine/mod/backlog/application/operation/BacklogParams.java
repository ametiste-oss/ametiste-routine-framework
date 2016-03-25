package org.ametiste.routine.mod.backlog.application.operation;

import org.ametiste.routine.sdk.protocol.operation.AbstractParamProtocol;

import java.util.*;

/**
 *
 * @since 1.1
 */
public class BacklogParams extends AbstractParamProtocol {

    private static final String BACKLOGED_SCHEME_NAME = "backlog.scheme.name";

    private static final List<String> DEFINED_PARAMS = Arrays.asList(
        BACKLOGED_SCHEME_NAME
    );

    public BacklogParams() {
        super(DEFINED_PARAMS);
    }

    public BacklogParams(final Map<String, String> params) {
        super(DEFINED_PARAMS, params);
    }

    public void schemeName(String schemeName) {
        addParam(BACKLOGED_SCHEME_NAME, schemeName);
    }

    public String schemeName() {
        return takeParam(BACKLOGED_SCHEME_NAME);
    }

}
