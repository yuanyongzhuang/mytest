package com.example;

import java.util.concurrent.*;

/**
 * Created by LL on 2018/6/18 0018.
 */
public class FutureTest {

    private static ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {

        long start = System.currentTimeMillis();

        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(3000L);

                return "callable";
            }
        };

        Callable<String> callable1 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(2000L);

                return "callable1";
            }
        };

        Callable<String> callable2 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(1000L);

                return "callable2";
            }
        };

        FutureTask<String> task = new FutureTask<>(callable);
        FutureTask<String> task1 = new FutureTask<>(callable1);
        FutureTask<String> task2 = new FutureTask<>(callable2);

//        new Thread(task).start();
//        new Thread(task1).start();
//        new Thread(task2).start();

        // 使用线程池，减少线程的创建时间
        threadPool.submit(task);
        threadPool.submit(task1);
        threadPool.submit(task2);



        ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap<>();

//        concurrentMap.put("task", task.get(1, TimeUnit.SECONDS));
        concurrentMap.put("task", task.get());
        concurrentMap.put("task1", task1.get()); // get()阻塞方法
        concurrentMap.put("task2", task2.get());

        System.out.println(System.currentTimeMillis() - start);
        System.out.println(concurrentMap);
    }

}
