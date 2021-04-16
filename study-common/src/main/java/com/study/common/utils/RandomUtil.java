package com.study.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * @author zhongjing
 * @Describe
 */
public class RandomUtil {

    /**
     * 随机
     */
    private static Random random = new Random();

    /**
     * 随机生成汉字
     *
     * @param num
     * @return
     */
    public static String createRandomChar(int num) {
        if (0 > num || num > 1000) {
            num = 2;
        }
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < num; i++) {
            String str = "";
            int highPos = (176 + Math.abs(random.nextInt(39)));
            int lowPos = (161 + Math.abs(random.nextInt(93)));
            byte[] b = new byte[2];
            b[0] = (Integer.valueOf(highPos)).byteValue();
            b[1] = (Integer.valueOf(lowPos)).byteValue();
            try {
                str = new String(b, "GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            result.append(str.charAt(0));
        }
        return result.toString();
    }

    /**
     * 生成英文字母
     *
     * @return
     */
    public static String createRandomENCode(int num) {
        if (0 > num || num > 1000) {
            num = 2;
        }
        StringBuffer code = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int s = random.nextInt(25) + 65;
            code.append((char) s);
        }
        return code.toString();
    }

    /**
     * 随机数
     *
     * @param start
     * @param end
     * @return
     */
    public static Integer createRandomNum(int start, int end) {
        if (0 > start) {
            start = 0;
        }
        if (0 > end) {
            end = 1;
        }
        return (int) (Math.random() * (end - start + 1) + start);
    }
}
