package org.ametiste.routine.mod.shredder.application.action;

import org.ametiste.routine.mod.shredder.application.service.ShreddingTaskService;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * @since
 */
public class ShreddingAction {

    private final ShreddingTaskService shreddingTaskService;

    public ShreddingAction(ShreddingTaskService shreddingTaskService) {
        this.shreddingTaskService = shreddingTaskService;
    }

    @Scheduled(fixedRateString = "${org.ametiste.routine.mod.shredder.shreddingRate:60000}")
    public void issueShreddingTask() {
        shreddingTaskService.issueShreddingTask();
    }

}
