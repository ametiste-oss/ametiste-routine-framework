package org.ametiste.routine.mod.backlog.application.service;

import org.ametiste.routine.mod.backlog.domain.Backlog;

/**
 * <p>
 *  Simple predicate to controll backlog renew policy. Used to detect
 *  that the backlog service is allowed to renew given backlog at the moment of time.
 * </p>
 *
 * @since 0.1.0
 */
public interface BacklogRenewConstraint {

    boolean isApplicable(Backlog backlog);

}
