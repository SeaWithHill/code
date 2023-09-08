package com.threadusage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class SyncQueueTask implements Runnable{
    private static Logger logger =  LoggerFactory.getLogger(SyncQueueTask.class.getName());
    @Override
    public void run() {
        logger.info(MessageFormat.format("线程信息：{0}", Thread.currentThread().getName()));
    }
}
