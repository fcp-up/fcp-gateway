package com.dfkj.fcp.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @ClassName: DateTimeUtil
 * @Description: 日期时间工具类
 * @author YuanXu
 * @date 2016-4-27 上午11:07:04
 * 
 */
public class DateTimeUtil {

    public static long MILLIONSECOND_OF_SECOND = 1000;

    public static long MILLIONSECOND_OF_MINUTE = MILLIONSECOND_OF_SECOND * 60;

    public static long MILLIONSECOND_OF_HOUR = MILLIONSECOND_OF_MINUTE * 60;

    public static long MILLIONSECOND_OF_DAY = MILLIONSECOND_OF_HOUR * 24;

    public static final String WHOLE_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    
    public static final String WHOLE_T_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);

    public static long TIMEZONE_OFFSET = Calendar.getInstance().get(Calendar.ZONE_OFFSET) + Calendar.getInstance().get(Calendar.DST_OFFSET);

    private DateTimeUtil() {
        // Nothing
    }

    /**
     * 
     * @Title: parse
     * @Description: 用缺省的时间、日期格式(yyyy-MM-dd HH:mm:ss)去解析一个字符串
     * @author YuanXu
     * @param datestr
     *            时间字符串
     * @return
     */
    public static Date parse(String datestr) {
        if (datestr == null) {
            return null;
        }

        Date date = null;
        try {
            date = DATETIME_FORMAT.parse(datestr);
        } catch (ParseException ex) {
            date = null;
        }

        if (date != null) {
            return date;
        }

        try {
            date = DATE_FORMAT.parse(datestr);
        } catch (ParseException ex) {
            date = null;
        }

        return date;
    }

    /**
     * 
     * @Title: parse
     * @Description: 使用给出的时间格式去解析一个字符串
     * @author YuanXu
     * @param datestr
     *            时间字符串
     * @param formatstr
     *            时间格式字符串
     * @return
     */
    public static Date parse(String datestr, String formatstr) {
        SimpleDateFormat dateformat = new SimpleDateFormat(formatstr);
        Date date = null;
        try {
            date = dateformat.parse(datestr);
        } catch (ParseException ex) {
            date = null;
        }

        dateformat = null;
        return date;
    }

    /**
     * 
     * @Title: parse
     * @Description: 将毫秒数转成时间格式
     * @author YuanXu
     * @param times
     *            毫秒数
     * @return
     */
    public static Date parse(Long times) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(times);
        return calendar.getTime();
    }

    /**
     * 
     * @Title: formatDate
     * @Description: 用缺省的日期格式来将时间转化为字符串，结果字符串中只包含日期部分
     * @author YuanXu
     * @param date
     *            时间对象
     * @return String 如果传入的日期为null，则返回null
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }

        return DATE_FORMAT.format(date);
    }

    /**
     * 
     * @Title: formatDateTime
     * @Description: 用缺省的日期时间格式来将时间转化为字符串，结果字符串中包含完整的日期、时间格式
     * @author YuanXu
     * @param date
     *            时间对象
     * @return String 如果传入的日期为null，则返回null
     */
    public static String formatDateTime(final Date date) {
        if (date == null) {
            return null;
        }

        return DATETIME_FORMAT.format(date);
    }

    /**
     * 
     * @Title: format
     * @Description: 用给定日期时间格式来将时间转化为字符串
     * @author YuanXu
     * @param date
     *            时间对象
     * @param formatstr
     *            时间格式字符串
     * @return String 如果传入的时间对象或时间格式字符串为空，则返回空
     */
    public static String format(final Date date, final String formatstr) {
        if (date == null || formatstr == null) {
            return null;
        }

        SimpleDateFormat format = new SimpleDateFormat(formatstr);
        String datestr = format.format(date);
        format = null;
        return datestr;
    }

    /**
     * 
     * @Title: getNow
     * @Description: 获取系统当前的时间
     * @author YuanXu
     * @return
     */
    public static Date getNow() {
        Calendar now = Calendar.getInstance();
        return now.getTime();
    }

    /**
     * 
     * @Title: getNowDateString
     * @Description: 获取当前日期的缺省字符串形式
     * @author YuanXu
     * @return
     */
    public static String getNowDateString() {
        return formatDate(getNow());
    }

    /**
     * 
     * @Title: getNowDateTimeString
     * @Description: 获取当前日期及时间的缺省字符串形式
     * @author YuanXu
     * @return
     */
    public static String getNowDateTimeString() {
        return formatDateTime(getNow());
    }

    /**
     * 
     * @Title: getNowWholeTimeString
     * @Description: 获取全时间(包括毫秒)的时间字符串
     * @author YuanXu
     * @return
     */
    public static String getNowWholeTimeString() {
        return DateTimeUtil.format(getNow(), WHOLE_DATETIME_FORMAT);
    }

    /**
     * 
     * @Title: getNowTimeString
     * @Description: 获取当前时间的缺省字符串形式
     * @author YuanXu
     * @return
     */
    public static String getNowTimeString() {
        return format(getNow(), DEFAULT_TIME_FORMAT);
    }

    /**
     * 
     * @Title: getThisYearStr
     * @Description: 得到当前的年度（四位数字字符串）
     * @author YuanXu
     * @return
     */
    public static String getThisYearStr() {
        Calendar now = Calendar.getInstance();
        return String.format("%s", now.get(Calendar.YEAR));
    }

    /**
     * 
     * @Title: getAdjustTime
     * @Description: 时间调整
     * @author YuanXu
     * @param date
     *            原始时间对象(为null时自动获取当前时间)
     * @param day
     *            int 时间偏移天数
     * @param hour
     *            int 时间偏移小时数
     * @param minute
     *            int 时间偏移分钟数
     * @param second
     *            int 时间偏移秒数
     * @param microsecond
     *            long 时间偏移微妙数
     * @return Date 返回一个调整后的时间
     */
    public static Date getAdjustTime(Date date, int day, int hour, int minute, int second, long microsecond) {
        Date d = date == null ? getNow() : date;
        return new Date(d.getTime() + MILLIONSECOND_OF_DAY * day + MILLIONSECOND_OF_HOUR * hour + MILLIONSECOND_OF_MINUTE * minute + MILLIONSECOND_OF_SECOND * second + microsecond);
    }

    /**
     * 
     * @Title: setDayFirstTime
     * @Description: 将时间设置为当天的第一个时间
     * @author YuanXu
     * @param date
     */
    public static void setDayFirstTime(Date date) {
        if (date == null) {
            return;
        }

        long l = date.getTime();
        l = l - (l + TIMEZONE_OFFSET) % MILLIONSECOND_OF_DAY;
        date.setTime(l);
    }

    /**
     * 
     * @Title: setDayLastTime
     * @Description: 将时间设置为当天的最后一个时间
     * @author YuanXu
     * @param date
     */
    public static void setDayLastTime(Date date) {
        if (date == null) {
            return;
        }

        long l = date.getTime();
        l = l - (l + TIMEZONE_OFFSET) % MILLIONSECOND_OF_DAY + MILLIONSECOND_OF_DAY - 1;
        date.setTime(l);
    }

    /**
     * 
     * @Title: getStartOfDay
     * @Description: 返回当天第一个时间
     * @author YuanXu
     * @param date
     * @return
     */
    public static Date getStartOfDay(Date date) {
        setDayFirstTime(date);
        return date;
    }

    /**
     * 
     * @Title: getEndOfDay
     * @Description: 返回当天最后一个时间
     * @author YuanXu
     * @param date
     * @return
     */
    public static Date getEndOfDay(Date date) {
        setDayLastTime(date);
        return date;
    }

    /**
     * 
     * @Title: getStartOfWeek
     * @Description: 得到星期的第一个时间
     * @author YuanXu
     * @param date
     * @return
     */
    public static Date getStartOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DAY_OF_MONTH, -(dayOfWeek - 2));
        return getStartOfDay(cal.getTime());
    }

    /**
     * 
     * @Title: getEndOfWeek
     * @Description: 得到星期的最后一个时间
     * @author YuanXu
     * @param date
     * @return
     */
    public static Date getEndOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DAY_OF_MONTH, -(dayOfWeek - 8));
        return getEndOfDay(cal.getTime());
    }

    /**
     * 
     * @Title: getStartOfMonth
     * @Description: 得到一个月的第一个时间
     * @author YuanXu
     * @param date
     * @return
     */
    public static Date getStartOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);
        return getStartOfDay(cal.getTime());
    }

    /**
     * 
     * @Title: getEndOfMonth
     * @Description: 得到一个月的最后一个时间
     * @author YuanXu
     * @param date
     * @return
     */
    public static Date getEndOfMonth(Date date) {
        int year, month;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        if (month > 11) {
            year++;
            month = 0;
        }
        cal.set(year, month, 0);
        return getEndOfDay(cal.getTime());
    }

    /**
     * 
     * @Title: getYear
     * @Description: 获取年份
     * @author YuanXu
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 
     * @Title: getMonth
     * @Description: 获取月份
     * @author YuanXu
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 
     * @Title: getDay
     * @Description: 获取日期
     * @author YuanXu
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    /**
     * 
     * @Title: getBeforeYearDate
     * @Description: 获取给定时间往前推一年的时间
     * @author YuanXu
     * @param date
     * @return
     */
    public static Date getBeforeYearDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, -1);
        date = c.getTime();
        return date;
    }

    /**
     * 
     * @Title: getWeekOfDate
     * @Description: 获取给定时间星期中文字符串
     * @author YuanXu
     * @param date
     * @return
     */
    public static String getWeekStrOfDate(Date date) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

    /**
     * 
     * @Title: getWeekOfDate
     * @Description: 获取给定时间星期索引 ( 0:星期天; 1:星期一; 2:星期二; 3:星期三; 4:星期四; 5:星期五;
     *               6:星期六;)
     * @author YuanXu
     * @param date
     * @return
     */
    public static int getWeekIndexOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
    }

}
