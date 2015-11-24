package org.ametiste.routine.domain.task.properties;

/**
 *
 * @since
 */
public interface TaskPropertiesRegistry {

    TaskProperty createTaskProperty(String kind, String value);

}
