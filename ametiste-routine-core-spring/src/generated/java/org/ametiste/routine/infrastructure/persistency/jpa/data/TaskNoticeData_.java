package org.ametiste.routine.infrastructure.persistency.jpa.data;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TaskNoticeData.class)
public abstract class TaskNoticeData_ {

	public static volatile SingularAttribute<TaskNoticeData, TaskData> task;
	public static volatile SingularAttribute<TaskNoticeData, Instant> creationTime;
	public static volatile SingularAttribute<TaskNoticeData, Integer> id;
	public static volatile SingularAttribute<TaskNoticeData, String> text;

}

