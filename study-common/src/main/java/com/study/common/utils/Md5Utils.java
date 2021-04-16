package com.study.common.utils;

import com.study.common.exception.BizException;
import com.study.common.exception.ErrorEnum;

import java.security.MessageDigest;


/**
 * @Author: zj
 * @Date: 2020/11/26 12:12
 * @Description:
 * @Version: 1.0
 */
public class Md5Utils {
    /**
     * md5加密
     *
     * @param s
     * @return
     */
    public static String Md5Util(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        } catch (Exception e) {
            throw new BizException(ErrorEnum.MD5_ERROR, e);
        }
    }

    private static String toHex(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }
}
