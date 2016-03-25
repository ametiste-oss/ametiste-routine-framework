package org.ametiste.routine.printer.scheme;

import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @since
 */
public class PrintTaskSchemeParams implements ParamsProtocol {

    private Integer taskNumber;
    private String taskOut;

    public void taskOut(String taskOut) {
        this.taskOut = taskOut;
    }

    public void taskNumber(Integer number) {
        this.taskNumber = number;
    }

    public String taskNumber() {
        return taskNumber.toString();
    }

    @Override
    public void fromMap(final Map<String, String> params) {
        taskOut = params.get("task.out");
        taskNumber = Integer.parseInt(params.get("task.taskNumber"));
    }

    @Override
    public Map<String, String> asMap() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("task.out", taskOut);
        map.put("task.number", taskNumber.toString());
        return map;
    }

    public String taskOut() {
        return taskOut;
    }

    public static PrintTaskSchemeParams createFromMap(final Map<String, String> params) {
        final PrintTaskSchemeParams operationParams = new PrintTaskSchemeParams();
        operationParams.fromMap(params);
        return operationParams;
    }
}
