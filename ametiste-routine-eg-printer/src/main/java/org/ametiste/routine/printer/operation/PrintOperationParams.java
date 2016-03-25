package org.ametiste.routine.printer.operation;

import org.ametiste.routine.sdk.protocol.operation.AbstractParamProtocol;

import java.util.*;

/**
 *
 * @since
 */
public class PrintOperationParams extends AbstractParamProtocol {

    private static final String OPERATION_OUT = "operation.out";
    private static final String OPERATION_DELAY = "operation.delay";

    private static final List<String> DEFINED_PARAMS = Arrays.asList(
        OPERATION_OUT, OPERATION_DELAY
    );

    public PrintOperationParams() {
        super(DEFINED_PARAMS);
    }

    public PrintOperationParams(final Map<String, String> params) {
        super(DEFINED_PARAMS, params);
    }

    public void delayTime(long delay) {
        addParam(OPERATION_DELAY, Long.toString(delay));
    }

    public long delayTime() {
        return takeParam(OPERATION_DELAY, Long::parseLong);
    }

    public void printOut(String operationOut) {
        addParam(OPERATION_OUT, operationOut);
    }

    public String operationOut() {
        return takeParam(OPERATION_OUT);
    }

}
