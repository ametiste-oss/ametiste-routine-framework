-- SET DATABASE SQL SYNTAX MYS TRUE;
CREATE SCHEMA ame_routine;

CREATE TABLE ame_routine.ame_routine_task (
  id CHAR(36) NOT NULL,
  state VARCHAR(16) NOT NULL,
  cr_time TIMESTAMP NOT NULL,
  execs_time TIMESTAMP,
  co_time TIMESTAMP,
  agregate BLOB NOT NULL,
  PRIMARY KEY (id)
);

-- ALTER TABLE sro_task ADD INDEX state_idx (state);
-- ALTER TABLE sro_task ADD INDEX propbiid_idx (prop_bound_item_id);
-- ALTER TABLE sro_task ADD INDEX execst_idx (execs_time);

CREATE TABLE ame_routine.ame_routine_task_property (
  task_id CHAR(36) NOT NULL,
  name VARCHAR(128) NOT NULL,
  value VARCHAR(128) NOT NULL
);

CREATE TABLE ame_routine.ame_routine_mod_property (
  mod_id CHAR(36) NOT NULL,
  name VARCHAR(128) NOT NULL,
  value VARCHAR(128) NOT NULL
);

CREATE TABLE ame_routine.ame_routine_task_operation (
  id CHAR(36) NOT NULL,
  task_id CHAR(36) NOT NULL,
  PRIMARY KEY (id)
);
