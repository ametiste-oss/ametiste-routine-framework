package org.ametiste.routine.printer.operation;

import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.Collections;
import java.util.Map;

/**
 *
 * @since
 */
public class PrintOperationParams implements ParamsProtocol {

    private String operationOut;

    public void printOut(String operationOut) {
        this.operationOut = operationOut;
    }

    public String operationOut() {
        return operationOut;
    }

    @Override
    public void fromMap(final Map<String, String> params) {
        operationOut = params.get("operation.out");
    }

    @Override
    public Map<String, String> asMap() {
        return Collections.singletonMap("operation.out", operationOut);
    }

    public static PrintOperationParams createFromMap(final Map<String, String> params) {
        final PrintOperationParams operationParams = new PrintOperationParams();
        operationParams.fromMap(params);
        return operationParams;
    }

}
