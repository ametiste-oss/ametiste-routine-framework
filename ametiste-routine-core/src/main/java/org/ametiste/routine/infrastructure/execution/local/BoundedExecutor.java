package org.ametiste.routine.infrastructure.execution.local;

import java.util.concurrent.*;
import java.util.function.Consumer;

public class BoundedExecutor {

    private final ExecutorService exec;
    private final Semaphore semaphore;

    public BoundedExecutor(ExecutorService exec, int bound) {
        this.exec = exec;
        this.semaphore = new Semaphore(bound);
    }

    public Future<?> submitTask(final Runnable command, final Runnable callback)
            throws InterruptedException, RejectedExecutionException {

        semaphore.acquire();

        try {
            return exec.submit(() -> {
                try {
                    command.run();
                } finally {
                    callback.run();
                    semaphore.release();
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
            throw e;
        }

    }
}