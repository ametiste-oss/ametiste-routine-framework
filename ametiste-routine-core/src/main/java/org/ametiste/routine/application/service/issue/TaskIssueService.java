package org.ametiste.routine.application.service.issue;

import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.UUID;
import java.util.function.Consumer;

/**
 *
 * @since
 */
public interface TaskIssueService {

    <T extends ParamsProtocol> UUID issueTask(Class<? extends TaskScheme<T>> taskSchemeName,
                                              Consumer<T> paramsProtocol,
                                              String creatorIdentifier);

}
