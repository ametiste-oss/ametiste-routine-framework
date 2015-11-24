package org.ametiste.routine.mod.dispenser.interfaces.web;

import org.ametiste.routine.domain.task.ExecutionLine;
import org.ametiste.routine.mod.dispenser.application.service.OperationReservationService;
import org.ametiste.routine.mod.dispenser.interfaces.web.data.ReservationIssueDTO;
import org.ametiste.routine.mod.dispenser.interfaces.web.data.ReservationOrderDTO;
import org.ametiste.routine.mod.dispenser.interfaces.web.data.ReservedTaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * <p>
 *     Represents the gateway to "dispense" process, that executes reservation of the tasks
 *     for external workers.
 * </p>
 *
 * @since 0.0.1
 */
@RestController
@RequestMapping("/dispense")
public class DispenseController {

    @Autowired
    private OperationReservationService operationReservationService;

    @RequestMapping(method = RequestMethod.POST)
    public ReservationOrderDTO reserveTasks(@RequestBody ReservationIssueDTO reservationIssue) {

        final List<ExecutionLine> executionLines = operationReservationService
                .reserveOperationsExecution(reservationIssue.getMaxReservationCount());

        final List<ReservedTaskDTO> collect = executionLines
                .stream()
                .map(l -> new ReservedTaskDTO(l.operationId().toString(), l.line(), l.properties())
                ).collect(Collectors.toList());

        return new ReservationOrderDTO(collect);
    }

}
