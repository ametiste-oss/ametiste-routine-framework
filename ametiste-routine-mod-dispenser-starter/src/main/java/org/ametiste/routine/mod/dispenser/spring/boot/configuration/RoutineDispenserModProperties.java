package org.ametiste.routine.mod.dispenser.spring.boot.configuration;

import org.ametiste.routine.mod.dispenser.application.service.DefaultOperationReservationService;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @since
 */
@ConfigurationProperties
public class RoutineDispenserModProperties {

    private int reservationLimit = DefaultOperationReservationService.DEFAULT_RESERVATION_LIMIT;

    public int getReservationLimit() {
        return reservationLimit;
    }

}
