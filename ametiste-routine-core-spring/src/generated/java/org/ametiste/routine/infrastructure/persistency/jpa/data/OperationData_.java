package org.ametiste.routine.infrastructure.persistency.jpa.data;

import java.util.UUID;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OperationData.class)
public abstract class OperationData_ {

	public static volatile ListAttribute<OperationData, OperationNoticeData> notices;
	public static volatile SingularAttribute<OperationData, TaskData> task;
	public static volatile SingularAttribute<OperationData, UUID> id;
	public static volatile SingularAttribute<OperationData, String> label;
	public static volatile SingularAttribute<OperationData, String> state;
	public static volatile ListAttribute<OperationData, OperationPropertyData> properties;

}

