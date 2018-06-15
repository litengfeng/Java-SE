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

import java.util.Random;

/**
 * @author litengfeng
 * @version 1.0
 * @date 2018/6/14 15:10
 * @project javase
 */


public class DataUtil {
    public static boolean byteEquals(byte a[],byte[] b){
        boolean equals=true;
        if(a==null || b==null){
            equals=false;
        }

        if(a!=null && b!=null){
            if(a.length!=b.length){
                equals=false;
            }else{
                for(int i=0;i<a.length;i++){
                    if(a[i]!=b[i]){
                        equals=false;
                        break;
                    }
                }
            }

        }
        return equals;
    }

    /**
     * byte转成Hex字符串
     * @param bytes
     * @return
     */
    public static String byte2Hex(byte[] bytes) {
        StringBuilder hs = new StringBuilder();
        String temp = "";
        for (int n = 0; n < bytes.length; n++) {
            temp = Integer.toHexString(bytes[n] & 0xFF);
            if(temp.length() == 1){
                hs.append("0").append(temp);
            }else {
                hs.append(temp);
            }
        }
        return hs.toString();
    }

    public static String random(){
        StringBuilder builder=new StringBuilder();
        Random random=new Random();
        int seedLength=10;
        for(int i=0;i<seedLength;i++){
            builder.append(digits[random.nextInt(seedLength)]);
        }

        return builder.toString();
    }

    static char[] digits={
            '0','1','2','3','4',
            '5','6','7','8','9',
            'a','b','c','d','e',
            'f','g','h','i','j'
    };
}
