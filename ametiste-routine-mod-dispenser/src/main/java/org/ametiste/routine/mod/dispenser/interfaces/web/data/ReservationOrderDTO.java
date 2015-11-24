package org.ametiste.routine.mod.dispenser.interfaces.web.data;

import java.util.List;

/**
 *
 * @since
 */
public class ReservationOrderDTO {

    private final List<ReservedTaskDTO> reservedTaskDTOs;

    public ReservationOrderDTO(List<ReservedTaskDTO> reservedTaskDTOs) {
        this.reservedTaskDTOs = reservedTaskDTOs;
    }

    public List<ReservedTaskDTO> getReservedTask() {
        return reservedTaskDTOs;
    }

}
