package org.ametiste.routine.sdk.mod;

/**
 *
 * @since
 */
public final class TaskPool {

    public final static String PROTOCOL_NAME = "task-pool";

    public final static class Message {

        public final static class IssueTask {

            public static final String TYPE = "task-pool:session:issue-task";

            public final static class Param {

                public static final String TASK_SCHEME = "task-pool:session:issue-task:params:task.scheme";

                public static final String TASK_SCHEME_PARAMS = "task-pool:session:issue-task:params:task.scheme.params";

            }

            public final static class Response {

                public static final String ISSUED_TASK_ID = "task-pool:session:issue-task:respone:task.issued.id";

            }

        }

    }

}
