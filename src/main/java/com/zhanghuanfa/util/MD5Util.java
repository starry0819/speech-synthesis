package com.zhanghuanfa.util;

import java.security.MessageDigest;

/**
 * @author zhanghuanfa
 * @date 2018-04-23 13:43
 */
public class MD5Util {
    public static String MD5(String content){
        return MD5(content,"UTF-8");
    }
    public static String MD5(String content, String encoding){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md.digest(content.getBytes(encoding));
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return new String(hexValue.toString().getBytes(encoding),encoding);
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
