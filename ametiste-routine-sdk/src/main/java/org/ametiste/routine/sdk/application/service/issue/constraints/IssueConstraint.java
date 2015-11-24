package org.ametiste.routine.sdk.application.service.issue.constraints;

/**
 *
 * <p>Represent objects that describes application-layer constraints.</p>
 *
 * <p>Application-layer constraints usualy defines how application must perform,
 * when the domain-layer define how logic must perform.</p>
 *
 * <p>In other words, application constraints more technical and defines concrete
 * use-case constaints, while the domain constraints defines broad domain logic constraints.</p>
 *
 * @since 0.0.1
 */
public interface IssueConstraint {

    @Deprecated
    void evaluate(Object taskForm) throws IssueConstratintException;

}
