//package com.synchronousqueueusage;
//
//import com.google.common.util.concurrent.ThreadFactoryBuilder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.concurrent.*;
//
//public class SynqueueConsumerAndProducer {
//
//
//
//    private static Logger logger =  LoggerFactory.getLogger(SynqueueConsumerAndProducer.class.getName());
//
//    public static void main(String[] args) {
//
//        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
//                .setNameFormat("demo-pool-%d").build();
//        //Common Thread Pool
//        ExecutorService executor = new ThreadPoolExecutor(3, 200,
//                0L, TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
//        SynchronousQueue<Integer> queue = new SynchronousQueue<>();
//
//        Runnable producer = () -> {
//            Integer producedElement = ThreadLocalRandom
//                    .current()
//                    .nextInt();
//            try {
//                queue.put(producedElement);
//                queue.put(producedElement);
//                logger.info("设置数据"+producedElement);
//
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }
//        };
//        Runnable consumer1 = () -> {
//            try {
//                Integer consumedElement = queue.take();
//                logger.info("获取数据"+consumedElement);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }
//        };
//        Runnable consumer2 = () -> {
//            try {
//                Integer consumedElement = queue.take();
//                logger.info("获取数据"+consumedElement);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }
//        };
//        executor.execute(producer);
//        executor.execute(consumer1);
//        executor.execute(consumer2);
//        SynqueueConsumerAndProducer synqueue = new SynqueueConsumerAndProducer();
//
////        try {
////            executor.awaitTermination(500, TimeUnit.MILLISECONDS);
////            executor.shutdown();
////            logger.info("同步队列大小:"+queue.size());
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//
//    }
//}
