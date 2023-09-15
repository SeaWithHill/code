package com.synchronousqueueusage;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.concurrent.*;

public class SynQueue {



    private static Logger logger =  LoggerFactory.getLogger(SynQueue.class.getName());

    public static void main(String[] args) {

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%03d").build();
        SynchronousQueue queue = new SynchronousQueue<Runnable>();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 100,
                60L, TimeUnit.SECONDS,
                queue, namedThreadFactory);
        threadPoolExecutor.prestartAllCoreThreads();
        for (int i = 0;i < 50;i++) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    logger.info(MessageFormat.format("thread info:{0}", Thread.currentThread().getName()));
                }
            });
        }
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
