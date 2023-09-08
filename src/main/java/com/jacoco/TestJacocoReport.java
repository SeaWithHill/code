package com.jacoco;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

public class TestJacocoReport {

    public static void main(String[] args) throws IOException {
        PheJacocoThread1 pheJacocoThread = new PheJacocoThread1();
        pheJacocoThread.start();
        try {
            Class<?> targetClass = PheClassLoaderJacoco.pheClassLoaderJacoco.loadClass("com.jacoco.TestTarget");
            Object obj = targetClass.newInstance();
            Method method = targetClass.getMethod("isPrime", int.class);
            method.invoke(obj, 7);
//            Class<?> targetClass1 = PheClassLoaderJacoco.pheClassLoaderJacoco.loadClass("com.jacoco.TestTarget2");
//            Object obj1 = targetClass1.newInstance();
//            Method method1 = targetClass1.getMethod("isPrime", int.class);
//            method1.invoke(obj1, 6);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ReportGenerator generator = new ReportGenerator(new File("D:\\demo\\code"));
        generator.create();
        System.out.println("生成jacoco报告结束");
    }
}
