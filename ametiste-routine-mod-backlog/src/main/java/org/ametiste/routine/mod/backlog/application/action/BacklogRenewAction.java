package org.ametiste.routine.mod.backlog.application.action;

import org.ametiste.routine.mod.backlog.application.service.BacklogRenewService;
import org.ametiste.routine.mod.backlog.domain.BacklogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * @since 0.1.0
 */
public class BacklogRenewAction {

    private BacklogRepository backlogRepository;

    private BacklogRenewService backlogRenewService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public BacklogRenewAction(BacklogRepository backlogRepository, BacklogRenewService backlogRenewService) {
        this.backlogRepository = backlogRepository;
        this.backlogRenewService = backlogRenewService;
    }

    // TODO: I need to externalize scheduling and make it framework agnostic. if possible
    @Scheduled(fixedRateString = "${org.ametiste.routine.mod.backlog.renewRate:60000}")
    public void renewAll() {
        backlogRepository.loadAll().forEach(backlogRenewService::renewBy);
    }

}
