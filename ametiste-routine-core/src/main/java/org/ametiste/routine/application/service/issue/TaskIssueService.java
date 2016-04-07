package org.ametiste.routine.application.service.issue;

import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 *
 * @since
 */
public interface TaskIssueService {

    /**
     * Issues task using given scheme and params protocol builder.
     *
     * @param taskScheme
     * @param paramsProtocolBuilder
     * @param creatorIdentifier
     * @return
     */
    UUID issueTask(final TaskScheme taskScheme,
                   final Consumer<ParamsProtocol> paramsProtocolBuilder,
                   final String creatorIdentifier);


    /**
     * Issues task using the given scheme class and params protocol builder.
     *
     * This method designed to support formal task schemas definitions.
     *
     * @param taskSchemeName
     * @param paramsProtocolBuilder
     * @param creatorIdentifier
     * @return
     */
    <T extends ParamsProtocol> UUID issueTask(final Class<? extends TaskScheme<T>> taskSchemeName,
                                              final Consumer<T> paramsProtocolBuilder,
                                              final String creatorIdentifier);

}
