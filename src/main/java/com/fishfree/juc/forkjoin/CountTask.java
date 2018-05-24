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
package com.fishfree.juc.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * @author litengfeng
 * @version 1.0
 * @date 2018/5/24 10:54
 * @project javase
 */


public class CountTask extends RecursiveTask<Integer> {

    private static int THRESHOLD = 2;
    private int start;
    private int end;

    public CountTask(int start, int end) {
        this.end = end;
        this.start = start;
    }

    protected Integer compute() {
        boolean fork = end - start <= THRESHOLD;
        int sum = 0;
        if (fork) {
            for (int i = start; i < end; i++) {
                sum += i;
            }
        } else {
            int middle = (end + start) / 2;
            CountTask leftCount = new CountTask(start, middle);
            CountTask rightCount = new CountTask(middle + 1, end);
            //执行子任务
            leftCount.fork();
            rightCount.fork();

            //等待子任务执行完成，返回结果
            int left = leftCount.join();
            int right = rightCount.join();

            sum = left + right;
        }
        return sum;
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        CountTask countTask = new CountTask(1, 4);
        Future<Integer> future = forkJoinPool.submit(countTask);
        System.out.println(future.get());

    }
}
