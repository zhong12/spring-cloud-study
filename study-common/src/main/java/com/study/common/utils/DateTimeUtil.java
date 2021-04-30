package com.study.common.utils;

import com.study.common.exception.BizException;
import com.study.common.exception.ErrorEnum;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: z.j
 * @Date: 2020/7/9 11:22
 * @Describe:
 */
public class DateTimeUtil {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    /**
     * 时间转String，并格式化
     *
     * @param date
     * @param format
     * @return
     */
    public static String getDateTimeToStr(Date date, String format) {
        date = verification(date);
        simpleDateFormat.applyLocalizedPattern(format);
        return simpleDateFormat.format(date);
    }

    /**
     * String 转 date
     *
     * @param dateTimeStr
     * @return
     * @throws BizException
     */
    public static Date getStrToDateTime(String dateTimeStr) throws BizException {
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateTimeStr);
        } catch (ParseException e) {
            throw new BizException(ErrorEnum.DATE_ERROR, e);
        }
        return date;
    }

    /**
     * 时间转long
     *
     * @param date
     * @return
     */
    public static Long getDateTimeLong(Date date) {
        date = verification(date);
        return date.getTime();
    }

    /**
     * 校验
     *
     * @param date
     * @return
     */
    private static Date verification(Date date) {
        return null == date ? new Date() : date;
    }

    /**
     * long
     * @param timestamp
     * @return
     */
    public static String toSecondString(long timestamp) {
        return simpleDateFormat.format(timestamp);
    }
}
