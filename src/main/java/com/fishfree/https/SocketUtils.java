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

/**
 * @author litengfeng
 * @version 1.0
 * @date 2018/6/14 16:04
 * @project javase
 */


public class SocketUtils {


    /**
     * 通过socket的输入流获取数据
     *
     * @param in     输入流
     * @param length 数据的长度
     * @return
     * @throws IOException
     */
    public static byte[] read(DataInputStream in, int length) throws IOException {
        int index = 0;
        byte[] data = new byte[length];
        while (index < length) {
            index += in.read(data, index, length - index);
        }
        return data;
    }

    /**
     * 输出流写数据
     * @param out
     * @param bytes
     * @param length
     * @throws IOException
     */
    public static void write(DataOutputStream out, byte[] bytes, int length) throws IOException {
        out.writeInt(length);
        out.write(bytes, 0, length);
        out.flush();
    }

    public static void write(DataOutputStream out, byte[] bytes) throws IOException {
        write(out,bytes,bytes.length);
    }

}
