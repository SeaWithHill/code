package com.mina;

public class ServerConstants {

    /**
     * mina 线程池配置
     */
    public static int NIO_MAXIMUM_POOL_SIZE =  Integer.MAX_VALUE;
    public static int NIO_CORE_POOL_SIZE = 0;
    public static long NIO_KEEP_ALIVE_TIME = 60L;


    public static int BUS_MAXIMUM_POOL_SIZE = Integer.MAX_VALUE;
    public static int BUS_CORE_POOL_SIZE = 60;
    public static long BUS_KEEP_ALIVE_TIME = 30L;
}
