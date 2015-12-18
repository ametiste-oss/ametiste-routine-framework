package org.ametiste.routine.infrastructure.execution;

import java.util.concurrent.*;

public class BoundedExecutor {

    private final ExecutorService exec;
    private final Semaphore semaphore;

    public BoundedExecutor(ExecutorService exec, int bound) {
        this.exec = exec;
        this.semaphore = new Semaphore(bound);
    }

    public Future<?> submitTask(final Runnable command)
            throws InterruptedException, RejectedExecutionException {

        semaphore.acquire();

        try {
            return exec.submit(new Runnable() {
                public void run() {
                    try {
                        command.run();
                    } finally {
                        semaphore.release();
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
            throw e;
        }

    }
}