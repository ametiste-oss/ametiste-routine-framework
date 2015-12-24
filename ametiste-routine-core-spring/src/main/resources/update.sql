-- Task ON DELETE CASCADE constratints
--
-- Alter tables to support JPATaskDataRepository#deleteByStateAndCompletionDate(List<String> states, Date afterCompletion).
-- Hibernate does not allow to cascade during batch modifications, so the schema need to be constained to
-- maintain integrity and allow this functionality.
--
-- Main idea behind this solution that is tasks only can be deleted in the final states where
-- no othere usage of task is allowed. So this constraints does not harm an ORM data model in any maner.
--
-- Standard technique to redefine constraints used, ORM provides well known constraint names ( see
-- o.a.r.i.p.j.data.TaskData and o.a.r.i.p.j.data.OperationData for mapping details ), hibernate creates
-- database schema using well known names, then these alterations are replace them with the new constraints
-- which have ON DELETE CASCADE enabled.

ALTER TABLE ame_routine_task_operation DROP CONSTRAINT fk_task_op_task_id;
ALTER TABLE ame_routine_task_operation ADD CONSTRAINT fk_task_op_task_id FOREIGN KEY (task_id) REFERENCES ame_routine_task(id) ON DELETE CASCADE;

ALTER TABLE ame_routine_task_notice DROP CONSTRAINT fk_task_notice_task_id;
ALTER TABLE ame_routine_task_notice ADD CONSTRAINT fk_task_notice_task_id FOREIGN KEY (task_id) REFERENCES ame_routine_task(id) ON DELETE CASCADE;

ALTER TABLE ame_routine_task_property DROP CONSTRAINT fk_task_prop_task_id;
ALTER TABLE ame_routine_task_property ADD CONSTRAINT fk_task_prop_task_id FOREIGN KEY (task_id) REFERENCES ame_routine_task(id) ON DELETE CASCADE;

ALTER TABLE ame_routine_task_operation_notice DROP CONSTRAINT fk_op_notice_op_id;
ALTER TABLE ame_routine_task_operation_notice ADD CONSTRAINT fk_op_notice_op_id FOREIGN KEY (operation_id) REFERENCES ame_routine_task_operation(id) ON DELETE CASCADE;

ALTER TABLE ame_routine_task_operation_property DROP CONSTRAINT fk_op_prop_op_id;
ALTER TABLE ame_routine_task_operation_property ADD CONSTRAINT fk_op_prop_op_id FOREIGN KEY (operation_id) REFERENCES ame_routine_task_operation(id) ON DELETE CASCADE;

-- -- -- --
