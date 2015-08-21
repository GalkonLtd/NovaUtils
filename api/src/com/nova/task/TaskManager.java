package com.nova.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 7/23/2015
 */
public final class TaskManager {

    private static ExecutorService logicWorker;
    private static ExecutorService taskWorker;

    /**
     * Submits a {@link Task} to be executed on the single threaded executor.
     * @param task
     */
    public static void submitLogic(Task task) {
        if (logicWorker == null) {
            logicWorker = Executors.newSingleThreadExecutor();
        }
        logicWorker.submit(task);
    }

    /**
     * Submits a {@link Task} to be executed on the cached thread pool executor.
     * @param task
     */
    public static void submitTask(Task task) {
        if (taskWorker == null) {
            taskWorker = Executors.newCachedThreadPool();
        }
        taskWorker.submit(task);
    }

}
