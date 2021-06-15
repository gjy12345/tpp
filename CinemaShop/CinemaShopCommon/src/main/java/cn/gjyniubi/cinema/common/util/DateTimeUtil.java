package cn.gjyniubi.cinema.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/3/7
 * @Class DateTimeUtil
 */
public class DateTimeUtil {

    private final static DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final static DateTimeFormatter dateFormat=DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final static DateTimeFormatter orderDateFormat=DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String getNowDateTime(){
        return dateTimeFormatter.format(LocalDateTime.now());
    }

    public static String getNowDate(){
        return dateFormat.format(LocalDate.now());
    }

    public static DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public static DateTimeFormatter getDateFormat() {
        return dateFormat;
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static boolean isSameDay(Date d1, Date d2) {
        LocalDate l1 = d1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate l2 = d2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return l1.getYear()==l2.getYear()&&l1.getDayOfYear()==l2.getDayOfYear();
    }

    public static String getOrderDate(){
        return orderDateFormat.format(LocalDate.now());
    }

    public static void main(String[] args) {
        System.out.println(isSameDay(new Date(), new Date()));
    }
}
