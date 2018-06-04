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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author litengfeng
 * @version 1.0
 * @date 2018/6/1 16:23
 * @project javase
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-spring.xml"})
public class RedisExampleTest {

    @Resource
    private RedisExample redisExample;

    @Test
    public void test() throws MalformedURLException {
        ReflectionTestUtils.invokeMethod(redisExample,"test");
        redisExample.addLink("123", new URL("http://www.baidu.com"));
    }
}
