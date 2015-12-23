package org.ametiste.routine.sdk.domain;

import java.time.Instant;
import java.util.List;

/**
 *  <p>
 *      Filter that may be applied to limit task query. Note,
 *      each predicate is incremental, "AND" semantic must be used to build
 *      filter based on the given predicates.
 *  </p>
 *
 *  @since 0.1.0
 */
public interface TaskFilter {

    void stateIn(List<String> state);

    void creationTimeAfter(Instant crTime);

    void execStartTimeAfter(Instant exTime);

    void completionTimeAfter(Instant exTime);

}