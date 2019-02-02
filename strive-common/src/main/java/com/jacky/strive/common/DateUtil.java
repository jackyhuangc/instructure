package com.jacky.strive.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import qsq.biz.common.util.LogUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil extends DateUtils {

    public static final int MONTHS_OF_YEAR = 12;

    public static final int HOUR_SECONDS = 60 * 60;
    public static final int MINUTE_SECONDS = 60;
    public static final int DAY_SECONDS = 24 * 60 * 60;
    private static final int MILLI_2_NANO = 1000 * 1000;
    private static final int SECOND_2_MILLI = 1000;
    private static final Date MIN_DATE = qsq.biz.common.util.DateUtil.parseDate("1970-01-01");

    /**
     * 默认日期时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    /**
     * 没有时间的日期格式：yyyy-MM-dd
     */
    public static final String WITHOUT_TIME_FORMAT = "yyyy-MM-dd";
    /**
     * 没有秒的日期格式：yyyy-MM-dd HH:mm
     */
    public static final String WITHOUT_SECOND_FORMAT = "yyyy-MM-dd HH:mm";

    /**
     * 获取去除时分秒的日期
     *
     * @param date
     * @return
     */
    public static Date date(Date date) {
        String dateFormatString = "yyyy-MM-dd";
        DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
        String dateTxt = dateFormat.format(date);
        try {
            return dateFormat.parse(dateTxt);
        } catch (Exception e) {
            return date;
        }
    }

    public static Date today() {
        return date(qsq.biz.common.util.DateUtil.now());
    }

    /**
     * 在当前时间基础上增加分钟数,默认返回带时分秒的日期
     *
     * @param minutes
     * @return
     */
    public static Date addMinutes(int minutes) {
        return addMinutes(new Date(), minutes);
    }

    /**
     * 在特定日期基础上加分钟,默认返回带时分秒的日期
     *
     * @param minutes
     * @return
     */
    public static Date addMinutes(Date date, int minutes) {
        return addMinutes(date, minutes, true);
    }

    /**
     * 在特定时间上增加分钟数
     *
     * @param minutes  待加的分钟数
     * @param withTime 是否返回带时分秒
     * @return
     */
    public static Date addMinutes(Date date, int minutes, boolean withTime) {
        Date calDate = DateUtils.addMinutes(date, minutes);
        if (withTime) {
            return calDate;
        }
        return date(calDate);
    }

    /**
     * 在当前时间基础上增加天数,默认返回带时分秒的日期
     *
     * @param days
     * @return
     */
    public static Date addDays(int days) {
        return addDays(new Date(), days);
    }

    /**
     * 在特定日期基础上加天数,默认返回带时分秒的日期
     *
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        return addDays(date, days, true);
    }

    /**
     * 在特定时间上增加天数
     *
     * @param days     待加的天数
     * @param withTime 是否返回带时分秒
     * @return
     */
    public static Date addDays(Date date, int days, boolean withTime) {
        Date addedDate = DateUtils.addDays(date, days);
        if (withTime) {
            return addedDate;
        }
        return date(addedDate);
    }

    /**
     * 在当前时间基础上增加小时数,默认返回带时分秒的日期
     *
     * @param hours
     * @return
     */
    public static Date addHours(int hours) {
        return addHours(new Date(), hours);
    }

    /**
     * 在特定日期基础上加小时数,默认返回带时分秒的日期
     *
     * @param hours
     * @return
     */
    public static Date addHours(Date date, int hours) {
        return addHours(date, hours, true);
    }

    /**
     * 在特定时间上增加小时数
     *
     * @param hours    待加的小时数
     * @param withTime 是否返回带时分秒
     * @return
     */
    public static Date addHours(Date date, int hours, boolean withTime) {
        Date addedDate = DateUtils.addHours(date, hours);
        if (withTime) {
            return addedDate;
        }
        return date(addedDate);
    }

    /**
     * 带时分秒的当前时间
     *
     * @return
     */
    public static Date now() {
        return now(true);
    }

    /**
     * 当前时间
     *
     * @param withTime 是否带时分秒
     * @return
     */
    public static Date now(boolean withTime) {
        Date current = new Date();
        if (withTime) {
            return current;
        }
        return date(current);
    }

    /**
     * 日期转换成字符串，格式：yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return format(date, DEFAULT_FORMAT_STRING);
    }


    public static String formatOnlyDay(Date date) {
        return format(date, WITHOUT_TIME_FORMAT);
    }

    /**
     * 日期转换成字符串，格式不传则是默认格式yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        try {
            if (date == null) {
                return StringUtils.EMPTY;
            }

            if (StringUtils.isEmpty(format)) {
                format = DEFAULT_FORMAT_STRING;
            }

            DateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date);
        } catch (Exception e) {
            qsq.biz.common.util.LogUtil.error("日期转换出错", e);
            return StringUtils.EMPTY;
        }
    }

    /**
     * 将字符串转换为日期
     *
     * @param dateText
     * @param format
     * @return
     */
    public static Date parseDate(String dateText, String format) {
        try {
            if (StringUtils.isEmpty(dateText)) {
                return null;
            }
            if (StringUtils.isEmpty(format)) {
                format = DEFAULT_FORMAT_STRING;
            }
            DateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.parse(dateText);
        } catch (Exception e) {
            String msg = String.format("字符串%s转日期格式：%s出错", dateText, format);
            LogUtil.error(msg, e);
            return null;
        }
    }

    /**
     * 将日期字符串转换为日期，如果都不成功，则失败
     *
     * @param dateString
     * @return
     */
    public static Date parseDate(String dateString) {
        if (StringUtils.isEmpty(dateString)) {
            return null;
        }

        if (0 < NumberUtils.toLong(dateString)) {
            return parseDate(NumberUtils.toLong(dateString));
        }
        try {
            Date date = DateUtils.parseDate(dateString, "yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd'T'HH:mm:ssZ",
                    "yyyy-MM-dd'T'HH:mm:ssz", "EEE MMM ddHH:mm:ss 'GMT' yyyy", "EEE MMM ddHH:mm:ss 'GST' yyyy",
                    DEFAULT_FORMAT_STRING, WITHOUT_SECOND_FORMAT, WITHOUT_TIME_FORMAT);
            if (dateString.indexOf("T") > 0) {// 增加对应的时间
                int offset = TimeZone.getDefault().getRawOffset();
                date = addMilliseconds(date, offset);
            }
            return date;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 将时间戳转为日期
     * <p>
     * 10位长度为 秒；13位长度为毫秒
     * </p>
     *
     * @param timestamp
     * @return
     */
    public static Date parseDate(Long timestamp) {
        if (String.valueOf(timestamp).length() > 10) {
            return new Date(timestamp);
        } else {
            return new Date(timestamp * 1000);
        }
    }

    /**
     * 获取默认
     *
     * @return
     */

    public static Date getDefault() {
        return parseDate("1000-01-01 00:00:00", DEFAULT_FORMAT_STRING);
    }

    /**
     * 比较时间分秒 小于
     */
    public static boolean lessThan(String time) {
        return StringUtils.compare(qsq.biz.common.util.DateUtil.format(new Date(), "HH:mm"), time) < 0;
    }

    /**
     * 比较时间分秒 大于
     */
    public static boolean greaterThan(String time) {
        return StringUtils.compare(qsq.biz.common.util.DateUtil.format(new Date(), "HH:mm"), time) > 0;
    }

    private static double diff(Date start, Date end, int seconds) {
        long diffMiiSecond = end.getTime() - start.getTime();
        return diffMiiSecond / (seconds * 1000.0);
    }

    public static double diffHours(Date start, Date end) {

        return diff(start, end, HOUR_SECONDS);
    }

    public static double diffMinutes(Date start, Date end) {
        return diff(start, end, MINUTE_SECONDS);
    }

    public static double diffSeconds(Date start, Date end) {
        return diff(start, end, 1);
    }

    public static int diffDays(Date start, Date end) {
        return (int) diff(date(start), date(end), DAY_SECONDS);
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    public static Long getTimestamp(Date date) {
        return date.getTime();
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static Long getTimestamp() {
        return getTimestamp(qsq.biz.common.util.DateUtil.now());
    }

    public static long nano2Milli(long nanoSeconds) {
        return nanoSeconds / MILLI_2_NANO;
    }

    public static int second2Milli(int second) {
        return second * SECOND_2_MILLI;
    }

    public static Integer diffYears(Date start, Date end) {
        Calendar cal = Calendar.getInstance();
        if (end != null) {
            cal.setTime(end);
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(start);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;
        if (monthNow > monthBirth) {
            return age;
        }

        if (monthNow == monthBirth) {
            if (dayOfMonthNow < dayOfMonthBirth) {
                age--;
            }
        } else {
            age--;
        }

        return age;
    }

    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }

        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }
}
