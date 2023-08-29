package com.jacoco;

import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestJacocoReport {
    private static Logger logger =  Logger.getLogger(TestJacocoReport.class.getName());

    public static void main(String[] args) throws IOException {
        try {
            Class<?> targetClass = PheClassLoaderJacoco.pheClassLoaderJacoco.loadClass("com.jacoco.TestTarget");
            Object obj = targetClass.newInstance();
            Method method = targetClass.getMethod("isPrime");
            method.invoke(obj);
        } catch (Exception e) {
            logger.info("");
            e.printStackTrace();
        }
        ExecutionDataStore executionDataStore = new ExecutionDataStore();
        SessionInfoStore sessionInfoStore = new SessionInfoStore();
        PheClassLoaderJacoco.data.collect(executionDataStore, sessionInfoStore, false);
        try {
            ExecFileUtils.createExecFileByList(sessionInfoStore, executionDataStore);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(e.getLocalizedMessage());
        }
        ReportGenerator generator = new ReportGenerator(new File("D:\\demo\\code"));
        generator.create();
    }
}
