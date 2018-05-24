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
package com.fishfree.juc.countdownlatch;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 参考CountDownLatch源码自己实现一个
 *
 * @author litengfeng
 * @version 1.0
 * @date 2018/5/24 11:45
 * @project javase
 */

public class MyCountDownLatch {

    /**
     * CountDownLatch利用共享锁的模式完成，
     * 1、初始化的时候设置，需要等待多少个线程获取锁，state赋值
     * 2、每次release后，state-1
     * 3、获取锁的时候，只有state被减到0,初始化的时候，才能获取成功，否则加入到同步队列中
     */
    private final Sync sync;

    public MyCountDownLatch(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count < 0");
        }
        sync = new Sync();
        //设置状态为线程的个数
        sync.setCount(count);
    }

    /**
     * 一般都是使用AQS同步器完成锁，所以定义一个同步器
     */
    private static class Sync extends AbstractQueuedSynchronizer {
        public void setCount(int count) {
            setState(count);
        }

        /**
         * 实现相应的模板方法
         */
        @Override
        protected int tryAcquireShared(int arg) {
            //如果获取的状态不是0，则一直等待
            return getState() == 0 ? 1 : -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            for (; ; ) {
                int state = getState();
                if (state == 0) return false;
                int current = state - arg;
                if (compareAndSetState(state, current)) {
                    return getState() == 0;
                }
            }
        }
    }

    public void countDown() {
        sync.releaseShared(1);
    }

    /**
     * 获取共享锁
     */
    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }


    public static void main(String[] args) throws InterruptedException {
        final MyCountDownLatch countDownLatch = new MyCountDownLatch(1);
        new Thread() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("after await");
            }
        }.start();
        TimeUnit.SECONDS.sleep(5);
        System.out.println("begin countDown");
        countDownLatch.countDown();
    }
}
