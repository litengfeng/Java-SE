/*
 * -------------------------------------------------------------------------------------
 *    Mi-Me Confidential
 *
 *    Copyright (C) 2016 Shanghai Mi-Me Financial Information Service Co., Ltd.
 *    All rights reserved.
 *
 *   No part of this file may be reproduced or transmitted in any form or by any means,
 *    electronic, mechanical, photocopying, recording, or otherwise, without prior
 *    written permission of Shanghai Mi-Me Financial Information Service Co., Ltd.
 * -------------------------------------------------------------------------------------
 */
package com.fishfree.juc.threadPool;

import java.text.MessageFormat;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author litengfeng
 * @version 1.0
 * @date 2018/5/30 16:52
 * @project javase
 */


public class ThreadPoolExecutorDemo {

    static AtomicInteger threadId = new AtomicInteger(1);

    public static void main(String[] args) throws InterruptedException {
//        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5, new ThreadFactory() {
//            //自定义线程池创建工厂，为了实现线程编号的查看
//            public Thread newThread(Runnable r) {
//                return new Thread(r, "pool-thread-id-" + threadId.getAndIncrement());
//            }
//        });
//        for (int i = 0; i < 10; i++) {
//            threadPool.submit(new Runnable() {
//                public void run() {
//                    //打印出执行这个任务的线程名称
//                    System.out.println("执行线程名称" + Thread.currentThread().getName());
//                }
//            });
//        }
//        //关闭线程池
//        threadPool.shutdown();


        MyThreadPool myThreadPool = new MyThreadPool(5);
        for (int i = 0; i < 10; i++) {
            myThreadPool.submit(new Task());
        }
        //关闭线程池
        myThreadPool.shutdown();
//        myThreadPool.submit(new Task());
    }

    private static class Task implements Runnable {
        public void run() {
            //打印出执行这个任务的线程名称
            System.out.println("执行线程名称" + Thread.currentThread().getName());
        }
    }
}
