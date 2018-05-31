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

import com.fishfree.juc.condition.BlockedQueue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 简单实现一个线程池的功能
 *
 * @author litengfeng
 * @version 1.0
 * @date 2018/5/31 13:59
 * @project javase
 */

public class MyThreadPool {
    //任务添加到阻塞队列中
    private BlockedQueue<Runnable> blockedQueue;
    //核心线程大小
    private int corePoolSize;
    //当前核心线程个数
    private AtomicInteger currentCorePoolSize = new AtomicInteger(0);
    private volatile boolean poolRunning;
    private HashSet<Worker> workers = new HashSet<Worker>();
    private ReentrantLock lock = new ReentrantLock();

    public MyThreadPool(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        blockedQueue = new BlockedQueue<Runnable>();
        poolRunning = true;
    }

    //提交任务的方法
    public void submit(Runnable task) throws InterruptedException {
        if (!poolRunning) {
            throw new IllegalStateException("线程池非运行状态");
        }
        //1. 如果当前工作线程个数没有达到核心线程，增加工作线程
        if (currentCorePoolSize.get() < corePoolSize) {
            if(addWorker(task)){
                currentCorePoolSize.incrementAndGet();
            }
        } else if (blockedQueue.add(task)) {//2. 将任务放到阻塞队列
            System.out.println("任务添加到队列");
        }
    }
    private Runnable getTask() throws InterruptedException {
        return blockedQueue.remove();
    }
    private boolean addWorker(Runnable task) {
        //创建工作线程，并运行提交的任务。
        Worker worker = new Worker(task);
        lock.lock();
        try {
            //添加到worker集合中
            workers.add(worker);
            worker.thread.start();
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        for (Iterator<Worker> workerIterator = workers.iterator(); workerIterator.hasNext(); ) {
            Worker worker = workerIterator.next();
            //关闭运行的工作线程
            worker.setRunFlag(false);
            if(!worker.thread.isInterrupted()){
                //将运行的线程进行中断处理
                worker.thread.interrupt();
            }
        }
        poolRunning = false;
    }

    /**
     * 真正执行任务的线程
     */
    private class Worker implements Runnable {
        //需要执行的任务
        private Runnable task;
        //线程运行标志
        private boolean runFlag = true;
        private Thread thread;

        public void setRunFlag(boolean runFlag) {
            this.runFlag = runFlag;
        }
        public Worker(Runnable task) {
            this.task = task;
            thread = new Thread(this);
        }
        public void run() {
            try {
                while (runFlag && (task != null || (task = getTask()) != null)) {
                    task.run();
                    //执行完成，让gc回收
                    task = null;
                }
            } catch (InterruptedException e) {
                System.out.println("线程被中断");
            }
        }
    }
}
