package com.qg.exclusiveplug.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author net
 * @version 1.0
 * 密码加密工具类；
 */
public class DigestUtil {
    /**
     * MD加密方法
     *
     * @param plainText 需要加密的文段；
     * @return 加密后的密码；
     */
    public static String digestPassword(String plainText) {
        byte[] secretBytes;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NO MD5");
        }
        //将加密后的byte数组转为16进制表示，并且返回；
        return new BigInteger(1, secretBytes).toString(16);
    }
}
