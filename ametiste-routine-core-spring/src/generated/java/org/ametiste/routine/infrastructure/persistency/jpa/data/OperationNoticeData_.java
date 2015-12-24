package org.ametiste.routine.infrastructure.persistency.jpa.data;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OperationNoticeData.class)
public abstract class OperationNoticeData_ {

	public static volatile SingularAttribute<OperationNoticeData, Instant> creationTime;
	public static volatile SingularAttribute<OperationNoticeData, String> text;

}

