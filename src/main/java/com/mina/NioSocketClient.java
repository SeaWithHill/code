package com.mina;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NioSocketClient {
    private static Logger logger =  LoggerFactory.getLogger(NioSocketClient.class.getName());
    private static IoSession session;

    public NioSocketClient() {
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("bus-client-handler-%03d").build();
        SynchronousQueue queue = new SynchronousQueue<Runnable>();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(50, 100,
                30L, TimeUnit.SECONDS,
                queue, namedThreadFactory);
        for (int i = 0;i < 60;i++) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        new NioSocketClient().init();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public  void init() throws InterruptedException, UnsupportedEncodingException {
        NioSocketConnector connector = new NioSocketConnector();
        connector.setHandler(new IoHandler() {
            @Override
            public void sessionCreated(IoSession ioSession) throws Exception {

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
                byte[] messageBytes = (byte[]) o;
                logger.info("接受到报文:" + new String(messageBytes));
            }

            @Override
            public void messageSent(IoSession ioSession, Object o) throws Exception {

            }

        });
        connector.getFilterChain().addFirst("protocol",
                new ProtocolCodecFilter(new MessageProtocolCodecFactory()));
        ConnectFuture cf = connector.connect(new InetSocketAddress("127.0.0.1", 11156));
        // 等待连接成功
        cf.await(10000);
        // 判断连接是否成功
        if (cf.isConnected()) {
            session = cf.getSession();
            // 获取session配置
            SocketSessionConfig sessionConfig = (SocketSessionConfig) session
                    .getConfig();
            sessionConfig.setTcpNoDelay(true);
            // 设置空闲检查间隔
            sessionConfig.setIdleTime(IdleStatus.BOTH_IDLE,
                    60000);
            JSONObject msg = new JSONObject();
            msg.put("id", "1");
            while(true){
                WriteFuture future = session.write(msg.toString().getBytes("utf-8"));
                // 判断写是否发生异常
                Throwable e = future.getException();
                if (e != null) {
                    logger.error("报文异常", e);
                }
                cf.awaitUninterruptibly(5, TimeUnit.SECONDS);
                cf.getSession().getConfig().setUseReadOperation(true);
                Object message = cf.getSession().read().awaitUninterruptibly().getMessage();
                logger.info("客户端接收到报文："+new String((byte[])message));
                Thread.sleep(100);
            }
        }

    }

}
