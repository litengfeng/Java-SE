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

import org.springframework.util.StringUtils;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/**
 * 证书操作类
 *
 * @author litengfeng
 * @version 1.0
 * @date 2018/6/14 14:07
 * @project javase
 */


public class CertificateUtil {

    public static final String CRET_FILE = "C:/https.crt";

    /**
     * 读取证书信息
     *
     * @param certFile 证书文件具体文件全路径
     * @return
     * @throws CertificateException, FileNotFoundException
     */
    public static byte[] readCertificates(String certFile) throws Exception {
        if (StringUtils.isEmpty(certFile)) {
            certFile = CRET_FILE;
        }
        return readCertificate(certFile).getEncoded();
    }

    /**
     * 读取证书信息
     *
     * @return
     * @throws Exception
     */
    public static Certificate readCertificate(String certFile) throws Exception {
        if (StringUtils.isEmpty(certFile)) {
            certFile = CRET_FILE;
        }
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        //获取c盘已经生成好的证书
        InputStream in = new FileInputStream(certFile);
        Certificate certificate = factory.generateCertificate(in);
        return certificate;
    }

    /**
     * 从keystore文件中获取私钥
     *
     * @return
     * @throws KeyStoreException
     */
    public static byte[] readPrivateKey() throws Exception {
        return readPrivateKeys().getEncoded();
    }

    /**
     * 从keystore文件中获取私钥
     *
     * @return
     * @throws KeyStoreException
     */
    public static PrivateKey readPrivateKeys() throws Exception {
        //JKS文件是使用keytool生成的keystore文件，存放私钥和证书
        //但是我们用keytool的时候，私钥并没有单独生成出来。这个
        // 不利于我们后期的一些扩展工作。所以，我们需要把私钥从
        // keytool中提取出来
        KeyStore keyStore = KeyStore.getInstance("JKS");
        InputStream in = new FileInputStream("c:/htpps.keystore");
        //根据密码获取私钥
        keyStore.load(in, "litengfeng".toCharArray());
        PrivateKey privateKey = (PrivateKey) keyStore.getKey("litengfeng", "litengfeng".toCharArray());
        return privateKey;
    }

    /**
     * 从证书中获取公钥
     *
     * @return
     */
    public static byte[] readPublicKey() throws Exception {
        Certificate certificate = readCertificate(CRET_FILE);
        return certificate.getPublicKey().getEncoded();
    }

    /**
     * 创建证书对象
     *
     * @param certData 证书数据
     * @return
     * @throws Exception
     */
    public static Certificate createCertificate(byte[] certData) throws Exception {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream in = new ByteArrayInputStream(certData);
        return factory.generateCertificate(in);
    }



}
