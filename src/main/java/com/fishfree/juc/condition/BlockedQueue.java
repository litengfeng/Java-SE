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
package com.fishfree.juc.condition;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 利用condition简单的实现一个阻塞队列
 * 理解如何适用condition
 *
 * @author litengfeng
 * @version 1.0
 * @date 2018/5/23 16:14
 * @project javase
 */


public class BlockedQueue<T> {

    public Object[] objects;

    //当前数组元素个数
    private int count;
    private int addIndex;
    private int removeIndex;

    private Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    public BlockedQueue(int size) {
        //初始化数组
        objects = new Object[size];
    }

    public void add(T t) throws InterruptedException {
        lock.lock();
        try {
            while (count == objects.length) {
                //队列满了，阻塞线程,等待notFull信号
                System.out.println("begin wait for notFull signal");
                notFull.await();
            }
            //说明队列有空位了
            objects[addIndex++] = t;
            //等于数组的大小
            if (addIndex == objects.length) {
                addIndex = 0;
            }
            count++;
            //发送notEmpty信号
            System.out.println("send notEmpty signal");
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T remove() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                //空的队列等待数据加入
                System.out.println("begin wait notEmpty signal");
                notEmpty.await();
            }
            T t = (T) objects[removeIndex];
            objects[removeIndex++] = null;
            if (removeIndex == objects.length) {
                removeIndex = 0;
            }
            --count;
            System.out.println("send notFull signal");
            notFull.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final int size = 5;
        final BlockedQueue<Integer> blockedQueue = new BlockedQueue(size);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                    for (int i = 0; i < size + 10; i++) {
                        blockedQueue.add(i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                    for (int j = 0; j < size; j++) {
                        System.out.println(blockedQueue.remove());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        countDownLatch.countDown();
        TimeUnit.SECONDS.sleep(10);
    }
}
