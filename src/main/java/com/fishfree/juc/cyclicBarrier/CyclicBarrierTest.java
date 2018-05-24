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
package com.fishfree.juc.cyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 循环屏障的简单例子
 *
 * @author litengfeng
 * @version 1.0
 * @date 2018/5/24 14:16
 * @project javase
 */


public class CyclicBarrierTest {
    final static CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {

        new Thread() {
            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                    System.out.println("after thread await");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        cyclicBarrier.await();
        System.out.println("main await");

    }
}
