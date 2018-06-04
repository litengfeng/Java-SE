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
package com.fishfree.spring.data.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URL;

/**
 * @author litengfeng
 * @version 1.0
 * @date 2018/5/31 17:57
 * @project javase
 */

@Component
public class RedisExample {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOps;


    public void addLink(String userId, URL url) {
        listOps.leftPush(userId, url.toExternalForm());
        //直接使用redisTemplate
        redisTemplate.boundListOps(userId).leftPush(url.toExternalForm());
    }

    private void test(){
        System.out.println("RedisExample.test method invoked");
    }
}
