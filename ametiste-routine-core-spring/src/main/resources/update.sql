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