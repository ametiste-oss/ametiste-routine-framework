package org.ametiste.routine.mod.dispenser.interfaces.web.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

/**
 * <p>
 *     Describes request to reserve bulk of tasks for exeution by the worker.
 * </p>
 *
 * @since 0.0.1
 */
public class ReservationIssueDTO {

    /**
     * Defines maximum of tasks count that a worker wants to reserv for termination.
     */
    private final int maxReservationCount;

    /**
     * Defines URI by which worker could be communicated by the dispenser.
     */
    private final URI workerCommunicationURI;

    /**
     * Defines identifier of the worker which do reservation issue.
     */
    private final String workerId;

    @JsonCreator
    public ReservationIssueDTO(
            @JsonProperty("maxReservationCount") int maxReservationCount,
            @JsonProperty("workerCommunicationURI") URI workerCommunicationURI,
            @JsonProperty("workerId") String workerId) {

        if (maxReservationCount < 1) {
            throw new IllegalArgumentException("maxReservationCount should be positive, given: " + maxReservationCount);
        }

        if (workerCommunicationURI == null) {
            throw new IllegalArgumentException("workerCommunicationURI can't be null.");
        }

        if (workerId == null || workerId.isEmpty()) {
            throw new IllegalArgumentException("workerId can't be null nor empty, given: " + workerId);
        }

        this.maxReservationCount = maxReservationCount;
        this.workerCommunicationURI = workerCommunicationURI;
        this.workerId = workerId;
    }

    public int getMaxReservationCount() {
        return maxReservationCount;
    }

}
