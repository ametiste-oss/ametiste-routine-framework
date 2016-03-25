package org.ametiste.routine.printer.scheme;

import org.ametiste.routine.sdk.protocol.operation.AbstractParamProtocol;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @since
 */
public class PrintTaskSchemeParams extends AbstractParamProtocol {

    private static final String TASK_NUMBER = "task.number";
    private static final String TASK_MESSAGE = "task.message";
    private static final String TASK_MAX_DELAY = "task.maxDelay";

    private static final List<String> DEFINED_PARAMS = Arrays.asList(
        TASK_NUMBER, TASK_MESSAGE, TASK_MAX_DELAY
    );

    public PrintTaskSchemeParams() {
        super(DEFINED_PARAMS);
    }

    public PrintTaskSchemeParams(final Map<String, String> params) {
        super(DEFINED_PARAMS, params);
    }

    public void delayTime(long delay) {
        addParam(TASK_MAX_DELAY, Long.toString(delay));
    }

    public long delayTime() {
        return takeParam(TASK_MAX_DELAY, Long::parseLong);
    }

    public void taskMessage(String taskOut) {
        addParam(TASK_MESSAGE, taskOut);
    }

    public String taskMessage() {
        return takeParam(TASK_MESSAGE);
    }

    public void taskNumber(int number) {
        addParam(TASK_NUMBER, Integer.toString(number));
    }

    public int taskNumber() {
        return takeParam(TASK_NUMBER, Integer::parseInt);
    }

}
