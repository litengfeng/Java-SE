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
package com.fishfree.juc.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author litengfeng
 * @version 1.0
 * @date 2018/5/24 11:27
 * @project javase
 */


public class AtomicReferenceTest {
    static class User {
        private String name;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
    public static AtomicReference<User> userAtomicReference = new AtomicReference<User>();
    public static void main(String arg[]) {
        User user = new User("zhang san ", 2);
        userAtomicReference.set(user);
        User updateUser = new User("li si", 4);
        userAtomicReference.compareAndSet(user,updateUser);

        System.out.println(userAtomicReference.get().age);
        System.out.println(userAtomicReference.get().name);
    }
}
