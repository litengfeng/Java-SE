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

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author litengfeng
 * @version 1.0
 * @date 2018/6/15 10:32
 * @project javase
 */


public class HttpsMockServer {

    public static void main(String[] args) throws IOException {

        ExecutorService executor = Executors.newFixedThreadPool(20);

        ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();
        ServerSocket serverSocket = serverSocketFactory.createServerSocket(80);

        serverSocket.setReceiveBufferSize(1024 * 100);
        serverSocket.setReuseAddress(false);
        DataInputStream dataInputStream = null;
        DataOutputStream dataOutputStream = null;
        System.out.println("服务器端开启监听");
        while (true) {
            final Socket socket = serverSocket.accept();
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            try {
                final byte[] desKey = HttpsUtil.serverSSLShakeHands(dataInputStream, dataOutputStream);
                executor.submit(new SocketTransportTask(desKey, socket));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class SocketTransportTask implements Runnable {
        private byte[] desKey;
        private DataOutputStream out;
        private DataInputStream in;

        public SocketTransportTask(byte[] desKey, Socket socket) throws IOException {
            this.desKey = desKey;
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());
        }

        public void run() {
            try {
                doSocketTransport(out, in);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void doSocketTransport(DataOutputStream out, DataInputStream in) throws Exception {
            System.out.println("--------------------------------------------------------");
            int length = in.readInt();
            byte[] clientMsg = readBytes(in, length);
            System.out.println("客户端指令内容为:" + DataUtil.byte2Hex(clientMsg));
            SocketUtils.write(out, "服务器已经接受请求".getBytes());
        }

        public byte[] readBytes(DataInputStream in, int length) throws Exception {
            //读取未解密的报文
            byte[] undecrpty = SocketUtils.read(in, length);
            System.out.println("读取未解密的报文：" + undecrpty);
            return DesCoder.decrypt(undecrpty, desKey);
        }

        public void writeBytes(DataOutputStream out, byte[] bytes) throws Exception {
            byte[] encrpted = DesCoder.encrypt(bytes, desKey);
            SocketUtils.write(out, encrpted);
        }
    }

}
