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
package com.fishfree.https;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.Key;
import java.util.concurrent.TimeUnit;

/**
 * @author litengfeng
 * @version 1.0
 * @date 2018/6/14 15:31
 * @project javase
 */


public class HttpsMackClient {

    public static void main(String[] args) throws Exception {
        DataInputStream in;
        DataOutputStream out;
        //监听80端口
        Socket socket = new Socket("localhost", 80);
        socket.setReceiveBufferSize(1024 * 100);
        socket.setKeepAlive(true);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        //开始进行握手
        System.out.println("客户端开始握手");
        HttpsUtil.clientSSLShakeHands(in, out);
        //开始进行消息的发送
        System.out.println("-----握手成功-----");
        out.write("duck".getBytes());

        int len = in.readInt();
        byte[] msg = SocketUtils.read(in, len);
        System.out.println("服务器反馈消息:" + DataUtil.byte2Hex(msg));

        TimeUnit.SECONDS.sleep(20);
    }
}
