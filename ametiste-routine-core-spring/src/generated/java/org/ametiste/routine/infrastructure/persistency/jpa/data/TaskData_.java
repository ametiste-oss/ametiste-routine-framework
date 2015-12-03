package org.ametiste.routine.infrastructure.persistency.jpa.data;

import java.time.Instant;
import java.util.UUID;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TaskData.class)
public abstract class TaskData_ {

	public static volatile ListAttribute<TaskData, TaskNoticeData> notices;
	public static volatile SingularAttribute<TaskData, Instant> completionTime;
	public static volatile SingularAttribute<TaskData, Instant> creationTime;
	public static volatile ListAttribute<TaskData, OperationData> operationData;
	public static volatile SingularAttribute<TaskData, Instant> executionStartTime;
	public static volatile SingularAttribute<TaskData, UUID> id;
	public static volatile SingularAttribute<TaskData, String> state;
	public static volatile ListAttribute<TaskData, TaskPropertyData> properties;

}

