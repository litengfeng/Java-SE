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

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author litengfeng
 * @version 1.0
 * @date 2018/5/23 16:00
 * @project javase
 */


public class ConditionCase {
    private Lock lock = new ReentrantLock();
    //通过lock才能获取condition，从这里也可以说明，condition是和lock绑定的
    private Condition condition = lock.newCondition();

    public void condtionWait() throws InterruptedException {
        lock.lock();
        try{
            //do something,
            //wait for signal
            condition.await();
        }finally {
            lock.unlock();
        }
    }

    public void conditionSignal(){
        lock.lock();
        try{
            condition.signal();
        }finally {
            lock.unlock();
        }
    }


}
