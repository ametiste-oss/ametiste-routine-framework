package org.ametiste.routine.application.service.issue;

import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import static javafx.scene.input.KeyCode.T;

/**
 *
 * @since
 */
public interface TaskIssueService {

    <T extends ParamsProtocol> UUID issueTask(final Class<? extends TaskScheme<T>> taskSchemeName,
                                              final Consumer<T> paramsProtocol,
                                              final String creatorIdentifier);

}
