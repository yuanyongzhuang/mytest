package com.example.testcloudprovider.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

 class ThreadPoolTest {
    //定义4个线程的线程池
    private static final ExecutorService pool = Executors.newFixedThreadPool(4);

    public static void main(String[] args) {
        //定义任务list，创建任务并加入list
        List<Callable<Map<String,Object>>> taskList = new ArrayList<>();
        taskList.add(new Task("url1"));
        taskList.add(new Task("url2"));
        taskList.add(new Task("url3"));
        taskList.add(new Task("url4"));

        //定义返回结果list，通过future获取返回值
        List<Future<Map<String,Object>>> resultList = new ArrayList<>();

        for (final Callable<Map<String,Object>> task:taskList){
            Future<Map<String,Object>> future = pool.submit(task);
            resultList.add(future);
        }

        for (final Future<Map<String,Object>> future:resultList){
            try {
                System.out.println(future.get(3,TimeUnit.SECONDS).get("url"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }

    }
}
//线程执行任务类
class Task implements Callable<Map<String,Object>>{

    private String url;

    public Task (String url){
        this.url = url;
    }

    @Override
    public Map<String, Object> call() throws Exception {
        Map<String,Object> result = new HashMap<>();
        result.put("url", this.url);
        result.put("result", url);
        System.out.println("task running: " + this.url);
        return result;
    }
}

