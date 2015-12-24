/**
 * This package conntains {@code Routine Core} data model definition used with JPA repository implementation.
 *
 * There is standard JPA model, compatible with {@code Hibernate} 4.3>.
 *
 * Note, to support {@link SpringDataTaskRepository#deleteTasks(java.util.List, java.time.Instant)} operation,
 * schema for this model must contain set of {@code ON DELETE CASCADE} constaints. With the reference
 * implementation this constraint intorduced via update ddl. See update scripts located in
 * {@code classpath://update.sql} file for implementation details.
 *
 * @see update.sql
 */
package org.ametiste.routine.infrastructure.persistency.jpa.data;
import org.ametiste.routine.infrastructure.persistency.jpa.SpringDataTaskRepository;