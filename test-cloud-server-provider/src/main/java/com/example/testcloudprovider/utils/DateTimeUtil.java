package com.example.testcloudprovider.utils;

import org.springframework.util.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

/**
 * Created by czh on 2020/7/7 11:50
 *
 * @author chengzhenhua
 */
public class DateTimeUtil {

    private static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
    private static final String datePattern = "yyyy-MM-dd";
    private static final String dateTimePattern6 = "yyyyMM";
    private static final String dateTimePattern14 = "yyyyMMddHHmmss";

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
    private static final DateTimeFormatter dateTimeFormatter6 = DateTimeFormatter.ofPattern(dateTimePattern6);
    private static final DateTimeFormatter dateTimeFormatter14 = DateTimeFormatter.ofPattern(dateTimePattern14);

    /**
     * 时间比较
     *
     * @param timeStr
     * @return 传入时间在当前时间之前 返回true 否则返回false
     */
    public static boolean isBefore(String timeStr) {
        return LocalDateTime.parse(timeStr, dateTimeFormatter).isBefore(LocalDateTime.now());
    }

    /**
     * 时间比较
     *
     * @param dateTime
     * @return 传入时间在当前时间之前 返回true 否则返回false
     */
    public static boolean isBefore(LocalDateTime dateTime) {
        return dateTime.isBefore(LocalDateTime.now());
    }

    /**
     * 时间比较
     *
     * @param timeStr
     * @return 传入时间在当前时间之后 返回true 否则返回false
     */
    public static boolean isAfter(String timeStr) {
        return LocalDateTime.parse(timeStr, dateTimeFormatter).isAfter(LocalDateTime.now());
    }

    /**
     * 时间比较
     *
     * @param dateTime
     * @return 传入时间在当前时间之后 返回true 否则返回false
     */
    public static boolean isAfter(LocalDateTime dateTime) {
        return dateTime.isAfter(LocalDateTime.now());
    }

    /**
     * 时间比较
     *
     * @param timeStr 要比较的时间
     * @param pattern 时间格式化规则
     * @return 传入时间在当前时间之前 返回true 否则返回false
     */
    public static boolean isBefore(String timeStr, String pattern) {
        return LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern)).isBefore(LocalDateTime.now());
    }

    /**
     * 时间比较
     *
     * @param timeStr 要比较的时间
     * @param pattern 时间格式化规则
     * @return 传入时间在当前时间之后 返回true 否则返回false
     */
    public static boolean isAfter(String timeStr, String pattern) {
        return LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern)).isAfter(LocalDateTime.now());
    }

    /**
     * 返回年月
     *
     * @return yyyyMM
     */
    public static String getTodayChar6() {
        return dateTimeFormatter6.format(LocalDateTime.now());
    }

    /**
     * 返回年月日时分秒
     *
     * @return yyyyMMddHHmmss
     */
    public static String getTodayChar14() {
        return dateTimeFormatter14.format(LocalDateTime.now());
    }

    /**
     * 获取今日月份
     *
     * @return MM
     */
    public static String getTodayMonth() {
        return LocalDateTime.now().getMonth().toString();
    }

    /**
     * 根据指定的格式 获取当前时间对应的字符串
     *
     * @param pattern
     * @return
     */
    public static String getNowTimeStr(String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.now());
    }

    /**
     * 根据指定的格式 获取时间对应的字符串
     *
     * @param localDateTime
     * @param pattern
     * @return
     */
    public static String getDateTimeStr(LocalDateTime localDateTime, String pattern) {
        if (StringUtils.isEmpty(localDateTime)) {
            return null;
        }
        return DateTimeFormatter.ofPattern(pattern).format(localDateTime);
    }

    public static String fmtDateToStrYMDHMS(LocalDateTime localDateTime) {

        return dateTimeFormatter.format(localDateTime);
    }

    /**
     * 将时间戳转换为时间
     *
     * @param epochMilli
     * @return
     */
    public static LocalDateTime parse(Long epochMilli) {
        if (StringUtils.isEmpty(epochMilli)) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneOffset.ofHours(8));
    }

    /**
     * 根据指定的格式 获取时间对应的字符串
     *
     * @param localDate
     * @param pattern
     * @return
     */
    public static String getDateStr(LocalDate localDate, String pattern) {
        if (StringUtils.isEmpty(localDate)) {
            return null;
        }
        return DateTimeFormatter.ofPattern(pattern).format(localDate);
    }

    /**
     * 根据指定的格式 获取字符串对应的日期
     *
     * @param str
     * @return
     */
    public static LocalDate getStrToDate(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return LocalDate.parse(str, dateFormatter);
    }

    /**
     * 根据指定的格式 获取字符串对应的时间
     *
     * @param str
     * @return
     */
    public static LocalDateTime getStrToDateTime(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return LocalDateTime.parse(str, dateTimeFormatter);
    }

    /**
     * 自定义格式 获取字符串对应的日期
     *
     * @param str
     * @param pattern
     * @return
     */
    public static LocalDate getStrToLocalDateByCustomPattern(String str, String pattern) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return LocalDate.parse(str, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 自定义格式 获取字符串对应的时间
     *
     * @param str
     * @param pattern
     * @return
     */
    public static LocalDateTime getStrToLocalDateTimeByCustomPattern(String str, String pattern) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 根据传入的time和时间格式1 返回指定的时间格式2对应的时间字符串
     *
     * @param time
     * @param pattern1
     * @param pattern2
     * @return
     */
    public static String getTimeFromStrToStr(String time, String pattern1, String pattern2) {
        if (StringUtils.isEmpty(time)) {
            return null;
        }
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(pattern1))
                .format(DateTimeFormatter.ofPattern(pattern2));
    }

    /**
     * 根据指定的格式 获取字符串对应的时间
     *
     * @param str
     * @return
     */
    public static LocalDateTime getStrDateTime14ToLocalDateTime(String str) {

        if (StringUtils.isEmpty(str)) {
            return null;
        }

        return getStrToDateTime(getTimeFromStrToStr(str, dateTimePattern14, dateTimePattern));
    }

    /**
     * 获取传入时间的毫秒
     *
     * @return
     */
    public static Long getMilli(LocalDateTime localDateTime) {
        if (StringUtils.isEmpty(localDateTime)) {
            return null;
        }
        return localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * 获取传入时间的毫秒
     *
     * @return
     */
    public static Long getMilli(LocalDate localDate) {
        if (StringUtils.isEmpty(localDate)) {
            return null;
        }
        return localDate.atStartOfDay().toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * 计算传入的时间和当前时间的时间差
     *
     * @param dateTime
     * @return 如果小于1s则显示刚刚，如果大于1s小于1分钟则显示具体的x秒前...
     */
    public static String getTimeDiff(LocalDateTime dateTime) {
        Instant beginInstant = dateTime.toInstant(ZoneOffset.ofHours(8));
        Instant endInstant = LocalDateTime.now().toInstant(ZoneOffset.ofHours(8));
        Duration between = Duration.between(beginInstant, endInstant);
        Long diffDay = between.toDays();
        Long diffHour = between.toHours();
        Long diffMin = between.toMinutes();
        Long diffSecond = between.toMillis() / 1000;
        if (diffDay > 0) {
            return diffDay + "天前";
        } else if (diffHour > 0) {
            return diffHour + "小时前";
        } else if (diffMin > 0) {
            return diffMin + "分钟前";
        } else if (diffSecond > 0) {
            return diffSecond + "秒前";
        } else {
            return "刚刚";
        }
    }

    /**
     * 计算两个时间的时间差（单位：天）
     *
     * @param beforeDate
     * @param afterDate
     * @return
     */
    public static Long getDifference(LocalDate beforeDate, LocalDate afterDate) {
        return afterDate.toEpochDay() - beforeDate.toEpochDay();
    }

    /**
     * 计算传入时间的时间差(返回微秒)
     *
     * @param start
     * @param end
     * @return
     */
    public static long getTimeDiff(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        return duration.toMillis();
    }

    /**
     * 给指定日期加分钟
     *
     * @param dateTime
     * @param minute
     * @return
     */
    public static LocalDateTime plusMinutes(LocalDateTime dateTime, long minute) {
        return dateTime.plusMinutes(minute);
    }

    /**
     * 给指定日期加秒
     *
     * @param dateTime
     * @param second
     * @return
     */
    public static LocalDateTime plusSeconds(LocalDateTime dateTime, long second) {
        return dateTime.plusSeconds(second);
    }

    /**
     * 给指定日期减月
     *
     * @param dateTime
     * @param months
     * @return
     */
    public static LocalDateTime minusMonths(LocalDateTime dateTime, long months) {
        return dateTime.minusMonths(months);
    }

    /**
     * 给日期添加凌晨时分秒
     *
     * @param date
     * @return
     */
    public static String beforeDawnTime(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        return String.join(" ", date, "00:00:00");
    }

    /**
     * 给日期添加夜晚时分秒
     *
     * @param date
     * @return
     */
    public static String nightTime(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        return String.join(" ", date, "23:59:59");
    }

    /**
     * 给日期添加凌晨时分秒
     *
     * @param date
     * @return
     */
    public static String morningTime(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        return String.join(" ", date, "00:00:00");
    }

    /**
     * 上月的第一天
     *
     * @param dateTime
     * @return
     */
    public static LocalDate getFirstDayOfLastMonth(LocalDate dateTime) {
        return dateTime.plusMonths(-1).with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 本月的第一天
     *
     * @param dateTime
     * @return
     */
    public static LocalDateTime getFirstDayOfMonth(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * 上月的最后一天
     *
     * @param dateTime
     * @return
     */
    public static LocalDate getLastDayOfLastMonth(LocalDate dateTime) {
        return dateTime.plusMonths(-1).with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 本月的最后一天
     *
     * @param dateTime
     * @return
     */
    public static LocalDateTime getLastDayOfMonth(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(0);
    }

    /**
     * 当天指定时间.
     *
     * @param hour   时
     * @param minute 分
     * @param second 秒
     * @return date
     */
    public static LocalTime getLocalDateTime(int hour, int minute, int second) {
        return LocalTime.now().withHour(hour).withMinute(minute).withSecond(second);
    }

    public static String formatterDateTime(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return "--";
        }
        return dateTimeFormatter.format(localDateTime);
    }

    /**
     * 对指定时间添加天数
     * @param localDate
     * @param day
     * @param pattern
     * @return
     */
    public static String getDateStrPlusDays(LocalDate localDate, int day, String pattern){
        if (StringUtils.isEmpty(localDate)) {
            return null;
        }
        return DateTimeFormatter.ofPattern(pattern).format(localDate.plusDays(day));
    }
}