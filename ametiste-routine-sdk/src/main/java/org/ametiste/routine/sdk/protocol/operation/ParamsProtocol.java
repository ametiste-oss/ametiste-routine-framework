package org.ametiste.routine.sdk.protocol.operation;

import org.ametiste.laplatform.sdk.protocol.Protocol;

import java.util.Map;

/**
 *
 * @since
 */
public interface ParamsProtocol extends Protocol {

    // TODO: want to have it as immutable, so this metho must return copy of original object
    void fromMap(Map<String, String> params);

    Map<String, String> asMap();

    /**
     * <p>
     * Proxies this {@code params object} values to the given {@code params object}.
     * </p>
     *
     * <p>
     * For example, may be used as shortcut method
     * when a task and an operation schemas using the same {@link ParamsProtocol} type:
     * </p>
     *
     * <pre>
     * protected void fulfillOperations(final TaskOperationInstaller task, final BacklogParams schemeParams) {
     *      task.addOperation(BacklogRenewOperationScheme.class, <b>schemeParams::proxy</b>);
     * }
     * </pre>
     * <p>
     * Without {@code proxy} method same may be expressed as
     * </p>
     * <pre>
     * protected void fulfillOperations(final TaskOperationInstaller task, final BacklogParams schemeParams) {
     *      task.addOperation(BacklogRenewOperationScheme.class, params -&gt; {
     *          <b>params.schemeName(schemeParams.schemeName())</b>
     *      });
     * }
     * </pre>
     *
     */
    // TODO: want to have it as immutable, so this metho must return copy of original object
    <T extends ParamsProtocol> void proxy(T params);

}
