package org.ametiste.routine.domain.scheme;

import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.function.Consumer;

/**
 *
 * @since
 */
public interface TaskScheme<T extends ParamsProtocol> {

    String schemeName();

    void setupTask(final TaskBuilder<T> taskBuilder,
                   final Consumer<T> paramsInstaller,
                   final String creatorIdenifier) throws TaskSchemeException;

}
