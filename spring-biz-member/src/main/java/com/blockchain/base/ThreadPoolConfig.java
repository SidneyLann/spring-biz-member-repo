package com.blockchain.base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Configuration
public class ThreadPoolConfig {
  public static String THREAD_NAME = "thread-name-%d";
  public static final int CORE_POOL_SIZE = 2;
  public static final int MAX_POOL_SIZE = 10;
  public static final int QUEUE_SIZE = 10000;

  @Bean(value = "executorService")
  public ExecutorService buildThreadPool() {
    ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(THREAD_NAME).build();

    /**
     * 1. CallerRunsPolicy ： 这个策略重试添加当前的任务，他会自动重复调用 execute() 方法，直到成功。 
     * 2. AbortPolicy ： 对拒绝任务抛弃处理，并且抛出异常。 
     * 3. DiscardPolicy ： 对拒绝任务直接无声抛弃，没有异常信息。
     * 4. DiscardOldestPolicy ：对拒绝任务不抛弃，而是抛弃队列里面等待最久的一个线程，然后把拒绝任务加到队列。
     */
    ExecutorService threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(QUEUE_SIZE), threadFactory,
        new ThreadPoolExecutor.AbortPolicy());
    return threadPool;
  }
}
