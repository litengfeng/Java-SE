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
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Des加密算法
 * 秘钥长度 56bit
 * 支持模式：ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128
 * padding:	Nopadding/PKCS5Padding/ISO10126Padding/
 *
 * @author litengfeng
 * @version 1.0
 * @date 2018/6/14 15:01
 * @project javase
 */


public class DesCoder {

    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "DES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";


    /**
     * 初始化密钥
     *
     * @param secureRandom
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] initSecretKey(SecureRandom secureRandom) throws NoSuchAlgorithmException {
        //返回生成指定算法的秘密密钥的 KeyGenerator 对象
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        //初始化此密钥生成器，使其具有确定的密钥大小
        keyGenerator.init(secureRandom);
        return keyGenerator.generateKey().getEncoded();
    }

    /**
     * 转换密钥
     *
     * @param key 二进制密钥
     * @return Key    密钥
     * @throws Exception
     */
    public static Key toKey(byte[] key) throws Exception {
        //实例化DES密钥规则
        DESKeySpec dks = new DESKeySpec(key);
        //实例化密钥工厂
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        //生成密钥
        SecretKey secretKey = skf.generateSecret(dks);
        return secretKey;
    }

    /**
     * 加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[]    加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, Key key) throws Exception {
        return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * 加密
     *
     * @param data 待加密数据
     * @param key  二进制密钥
     * @return byte[]    加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * 加密
     *
     * @param data            待加密数据
     * @param key             二进制密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]    加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
        //还原密钥
        Key k = toKey(key);
        return encrypt(data, k, cipherAlgorithm);
    }

    /**
     * 加密
     *
     * @param data            待加密数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]    加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        //使用密钥初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key);
        //执行操作
        return cipher.doFinal(data);
    }

    /**
     * 解密
     *
     * @param data 待解密数据
     * @param key  二进制密钥
     * @return byte[]    解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * 解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[]    解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, Key key) throws Exception {
        return CipherUtil.decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * 解密
     *
     * @param data            待解密数据
     * @param key             二进制密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]    解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
        //还原密钥
        Key k = toKey(key);
        return CipherUtil.decrypt(data, k, cipherAlgorithm);
    }


    private static String showByteArray(byte[] data) {
        if (null == data) {
            return null;
        }
        StringBuilder sb = new StringBuilder("{");
        for (byte b : data) {
            sb.append(b).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }

}
