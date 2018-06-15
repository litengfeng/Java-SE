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
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;

/**
 * @author litengfeng
 * @version 1.0
 * @date 2018/6/14 15:33
 * @project javase
 */


public class HttpsUtil {

    public static void clientSSLShakeHands(DataInputStream in, DataOutputStream out) throws Exception {
        //1 客户端发送自己支持的hash算法
        int length = CipherUtil.SHA1.getBytes().length;
        out.writeInt(length);
        SocketUtils.write(out, CipherUtil.SHA1.getBytes(), length);

        //2 客户端验证服务器端的证书真实合法性
        //报文长度
        int skip = in.readInt();
        //根据长度获取证书报文内容
        byte[] certificateBytes = SocketUtils.read(in, skip);
        Certificate certificate = CertificateUtil.createCertificate(certificateBytes);
        //从证书中获取公钥
        PublicKey publicKey = certificate.getPublicKey();
        //校验公钥
        certificate.verify(publicKey);
        System.out.println("客户端验证服务器端的证书通过");

        //3 客户端验证证书成功，生成生成随机数并用公钥加密
        System.out.println("客户端校验服务器端发送过来的证书成功,生成随机数并用公钥加密");
        SecureRandom seed = new SecureRandom();
        int seedLength = 2;
        byte seedBytes[] = seed.generateSeed(seedLength);
        System.out.println("生成的随机数为 : " + DataUtil.byte2Hex(seedBytes));
        System.out.println("将随机数用公钥加密后发送到服务器");
        String publicKeyAlgorithm = publicKey.getAlgorithm();
        System.out.println("公钥的加密算法： " + publicKeyAlgorithm);
        byte[] encryptSeed = CipherUtil.encrypt(seedBytes, publicKey, publicKeyAlgorithm, null);
        SocketUtils.write(out, encryptSeed, encryptSeed.length);
        System.out.println("加密后的seed值为 :" + DataUtil.byte2Hex(encryptSeed));

        //客户端生成消息
        String message = DataUtil.random();
        System.out.println("客户端生成消息为:" + message);
        System.out.println("使用随机数并用公钥对消息加密");
        byte[] encrptMessge = CipherUtil.encrypt(message.getBytes(), publicKey, publicKey.getAlgorithm(), seed);
        System.out.println("加密后的位数：" + encrptMessge.length);
        SocketUtils.write(out, encrptMessge, encrptMessge.length);


        System.out.println("客户端使用SHA1计算消息摘要");
        byte hash[] = CipherUtil.digest(message.getBytes());
        System.out.println("摘要信息为:" + DataUtil.byte2Hex(hash));
        System.out.println("消息加密完成，摘要计算完成，发送服务器");
        SocketUtils.write(out, hash, hash.length);


        System.out.println("客户端向服务器发送消息完成，开始接受服务器端发送回来的消息和摘要");
        System.out.println("接受服务器端发送的消息");
        int serverMessageLength = in.readInt();
        byte[] serverMessage = SocketUtils.read(in, serverMessageLength);

        System.out.println("开始用之前生成的随机密码和DES算法解密消息,密码为:" + DataUtil.byte2Hex((seedBytes)));
        byte[] desKey = DesCoder.initSecretKey(new SecureRandom(seedBytes));
        Key key = DesCoder.toKey(desKey);
        byte[] decrpytedServerMsg = DesCoder.decrypt(serverMessage, key);
        System.out.println("解密后的消息为:" + DataUtil.byte2Hex(decrpytedServerMsg));

        int serverHashLength = in.readInt();
        byte[] serverHash = SocketUtils.read(in, serverHashLength);
        System.out.println("开始接受服务器端的摘要消息:" + DataUtil.byte2Hex(serverHash));

        byte[] serverHashValues = CipherUtil.digest(decrpytedServerMsg);
        System.out.println("计算服务器端发送过来的消息的摘要 : " + DataUtil.byte2Hex(serverHashValues));
        System.out.println("判断服务器端发送过来的hash摘要是否和计算出的摘要一致");
        boolean isHashEquals = DataUtil.byteEquals(serverHashValues, serverHash);
        if (isHashEquals) {
            System.out.println("验证完成，握手成功");
        } else {
            System.out.println("验证失败，握手失败");
        }

    }


    public static byte[] serverSSLShakeHands(DataInputStream in, DataOutputStream out) throws Exception {
        //第一步 获取客户端发送的支持的验证规则，包括hash算法，这里选用SHA1作为hash
        int length = in.readInt();
        in.skipBytes(4);
        byte[] clientSupportHash = SocketUtils.read(in, length);
        String clientHash = new String(clientSupportHash);
        System.out.println("客户端发送了hash算法为:" + clientHash);

        //第二步，发送服务器证书到客户端
        Certificate certificate = CertificateUtil.readCertificate(CertificateUtil.CRET_FILE);
        byte[] certificateBytes = certificate.getEncoded();
        PrivateKey privateKey = CertificateUtil.readPrivateKeys();
        System.out.println("发送证书给客户端,字节长度为:" + certificateBytes.length);
        System.out.println("证书内容为:" + DataUtil.byte2Hex(certificateBytes));
        SocketUtils.write(out, certificateBytes, certificateBytes.length);

        //获取服务器端的随机数密文，用公钥加密的
        System.out.println("获取客户端通过公钥加密后的随机数");
        int secureByteLength = in.readInt();
        byte[] secureBytes = SocketUtils.read(in, secureByteLength);
        System.out.println("读取到的客户端的随机数为:" + DataUtil.byte2Hex(secureBytes));

        //第三步 获取客户端加密字符串
        int skip = in.readInt();
        System.out.println("第三步 获取客户端加密消息,消息长度为 ：" + skip);
        byte[] data = SocketUtils.read(in, skip);

        System.out.println("客户端发送的加密消息为 : " + DataUtil.byte2Hex(data));
        System.out.println("用私钥对消息解密，并计算SHA1的hash值");
        //公钥解密出报文信息
        byte message[] = CipherUtil.decrypt(data, privateKey, privateKey.getAlgorithm(), new SecureRandom(secureBytes));
        System.out.println("--------------验证签名开始---------------");
        //对报文进行消息摘要
        byte serverHash[] = CipherUtil.digest(message);

        //获取客户端发送的摘要信息
        int hashSkip = in.readInt();
        byte[] clientHashBytes = SocketUtils.read(in, hashSkip);
        System.out.println("客户端SHA1摘要为 : " + DataUtil.byte2Hex(clientHashBytes));

        System.out.println("开始比较客户端hash和服务器端从消息中计算的hash值是否一致");
        boolean equalsFlag = DataUtil.byteEquals(serverHash, clientHashBytes);
        System.out.println("是否一致结果为 ： " + equalsFlag);
        System.out.println("第一次校验客户端发送过来的消息和摘译一致，服务器开始向客户端发送消息和摘要");
        System.out.println("--------------验证签名结束---------------");


        System.out.println("-----------开始使用客户端生成的对称密钥进行加密服务器消息------------");
        //使用公钥解密随机字符串
        byte secureSeed[] = CipherUtil.decrypt(secureBytes, privateKey, privateKey.getAlgorithm());
        System.out.println("解密后的随机数密码为:" + DataUtil.byte2Hex(secureSeed));
        SecureRandom secureRandom = new SecureRandom(secureSeed);
        String randomMessage = DataUtil.random();
        System.out.println("服务器端生成的随机消息为 : " + randomMessage);
        System.out.println("用DES算法并使用客户端生成的随机密码对消息加密");
        byte[] desKey = DesCoder.initSecretKey(secureRandom);
        byte[] encryptMsg = DesCoder.encrypt(randomMessage.getBytes(), DesCoder.toKey(desKey));
        SocketUtils.write(out, encryptMsg, encryptMsg.length);
        System.out.println("-----------结束使用客户端生成的对称密钥进行加密服务器消息------------");

        System.out.println("-----------开始计算摘要------------");
        byte[] serverMessageHash = CipherUtil.digest(randomMessage.getBytes());
        System.out.println("服务器端生成的摘要信息：" + serverMessageHash);
        SocketUtils.write(out, serverMessageHash, serverMessageHash.length);
        System.out.println("握手成功，之后所有通信都将使用DES加密算法进行加密");
        return secureSeed;
    }

}
