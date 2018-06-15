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

import javax.crypto.Cipher;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 加解密工具类
 *
 * @author litengfeng
 * @version 1.0
 * @date 2018/6/14 15:11
 * @project javase
 */


public class CipherUtil {

    public static final String SHA1 = "SHA1";

    /**
     * 解密
     *
     * @param data            待解密数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]    解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
        return decrypt(data, key, cipherAlgorithm, null);
    }

    /**
     * 解密
     *
     * @param data            待解密数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]    解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm, SecureRandom seed) throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        //使用密钥初始化，设置为解密模式
        if (seed != null) {
            cipher.init(Cipher.DECRYPT_MODE, key, seed);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, key);
        }
        //执行操作
        return cipher.doFinal(data);
    }

    /**
     * 加密
     *
     * @param data            明文数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法
     * @param seed            种子
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, Key key, String cipherAlgorithm, SecureRandom seed) throws Exception {
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        if (seed != null) {
            cipher.init(Cipher.ENCRYPT_MODE, key, seed);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        }
        return cipher.doFinal(data);
    }

    /**
     * 对数据进行签名
     *
     * @param bytes 需要签名的数据
     * @return
     */
    public static byte[] digest(byte[] bytes) {
        byte[] _bytes = null;
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(SHA1);
            messageDigest.update(bytes);
            _bytes = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return _bytes;
    }

}
