package com.mina;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.*;

public class NioSocketServer {

    private static Logger logger = LoggerFactory.getLogger(NioSocketServer.class.getName());
    /**
     * socket acceptor
     */
    private SocketAcceptor acceptor;

    /**
     * 主机绑定地址
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * soLiner time
     */
    private int soLingerTime = 0;


    private static ThreadPoolExecutor threadPoolExecutor;
    /**
     * accept handler
     */
    private IoHandler acceptHandler = new IoHandler() {

        /**
         * session create
         */
        @Override
        public void sessionCreated(IoSession session) throws Exception {
            int checkInterval = 600000 / (4 * 1000);
            // 获取连接的地址
            SocketAddress socketAddress = session.getRemoteAddress();
            // 设置idle事件
            SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();
            cfg.setSoLinger(soLingerTime);
            cfg.setTcpNoDelay(true);
            cfg.setIdleTime(IdleStatus.BOTH_IDLE, checkInterval);
            logger.info("接收到报文");
        }

        @Override
        public void sessionOpened(IoSession ioSession) throws Exception {

        }

        @Override
        public void sessionClosed(IoSession ioSession) throws Exception {

        }

        @Override
        public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {

        }

        @Override
        public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {

        }

        @Override
        public void messageReceived(IoSession ioSession, Object o) throws Exception {
                    logger.info("method: messageReceived");
            JSONObject msg = new JSONObject();
            msg.put("nioserver", "yes");
            byte[] buffer = msg.toString().getBytes("UTF-8");
            ioSession.write(buffer);
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    logger.info("业务执行开始");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    logger.info("业务执行结束");
                }
            });
        }

        @Override
        public void messageSent(IoSession ioSession, Object o) throws Exception {

        }
    };

    public static void main(String[] args) {
        NioSocketServer nioSocketServer = new NioSocketServer();
        nioSocketServer.start();
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("bus-server-handler-%03d").build();
        threadPoolExecutor = new ThreadPoolExecutor(ServerConstants.BUS_CORE_POOL_SIZE, ServerConstants.BUS_MAXIMUM_POOL_SIZE,
                ServerConstants.BUS_KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), namedThreadFactory);
    }

    public void start() {
        int numCpu = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = new ThreadPoolExecutor(ServerConstants.NIO_CORE_POOL_SIZE, ServerConstants.NIO_MAXIMUM_POOL_SIZE,
                ServerConstants.NIO_KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
        this.acceptor = new NioSocketAcceptor(pool,
                new SimpleIoProcessorPool<NioSession>(NioProcessor.class,
                        numCpu + 1));
        this.acceptor.setReuseAddress(true);
        this.acceptor.getFilterChain().addFirst("protocol",
                new ProtocolCodecFilter(new MessageProtocolCodecFactory()));
        this.acceptor.setHandler(this.acceptHandler);
        this.host = "127.0.0.1";
        this.port = 11156;
        try {
            if (this.host != null) {
                this.acceptor.bind(new InetSocketAddress(host, port));
            } else {
                this.acceptor.bind(new InetSocketAddress(port));
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("server启动失败, cause by "
                    + e.getMessage(), e);
        }
        logger.info("server start success");
    }
}
