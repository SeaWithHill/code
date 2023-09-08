package com.jacoco;


import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;

import java.io.IOException;

public class PheJacocoThread1 extends Thread {

    public static IRuntime runtime = new LoggerRuntime();
    public static Instrumenter instr = new Instrumenter(runtime);
    public static RuntimeData data = new RuntimeData();
    public static PheClassLoaderJacoco pheClassLoaderJacoco = new PheClassLoaderJacoco();


    @Override
    public void run() {
        try {
            runtime.startup(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            ExecutionDataStore executionDataStore = new ExecutionDataStore();
            SessionInfoStore sessionInfoStore = new SessionInfoStore();
            data.collect(executionDataStore, sessionInfoStore, false);
            try {
                if (executionDataStore.getContents().size() > 0 && sessionInfoStore.getInfos().size() > 0) {
                    System.out.println("开始收集探针信息");
                    ExecFileUtils.createExecFileByList(sessionInfoStore, executionDataStore);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
