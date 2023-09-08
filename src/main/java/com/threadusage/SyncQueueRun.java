package com.threadusage;

import java.util.concurrent.*;

public class SyncQueueRun {
    public static void main(String[] args) {
        SynchronousQueue synchronousQueue = new SynchronousQueue();
        ExecutorService executor = new ThreadPoolExecutor(3, 20,
                120L, TimeUnit.SECONDS,
                synchronousQueue);
        for (int i = 0; i < 20; i++) {
            SyncQueueTask syncQueueTask = new SyncQueueTask();
            executor.execute(syncQueueTask);
        }
    }
}
