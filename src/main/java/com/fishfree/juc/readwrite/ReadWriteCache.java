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
package com.fishfree.juc.readwrite;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 简单利用读写锁实现的map的缓存
 * 主要为了使用读写锁的api，熟悉如何使用
 *
 * @author litengfeng
 * @version 1.0
 * @date 2018/5/23 11:54
 * @project javase
 */

public class ReadWriteCache<K, V> {
    //使用hashMap充当简单的缓存
    private Map<K, V> cache = new HashMap<K, V>();
    //获取读写对象
    static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    //通过读写锁对象获取内部内读写锁对象
    static ReentrantReadWriteLock.ReadLock readLock = rwl.readLock();
    static ReentrantReadWriteLock.WriteLock writeLock = rwl.writeLock();

    /**
     * 通过k获取对象
     *
     * @param k
     * @return
     */
    public Object get(K k) {
        readLock.lock();
        try {
            return cache.get(k);
        } finally {
            readLock.unlock();
        }
    }

    public void set(K key, V value) {
        writeLock.lock();
        try {
            cache.put(key, value);
        } finally {
            writeLock.lock();
        }
    }
}
