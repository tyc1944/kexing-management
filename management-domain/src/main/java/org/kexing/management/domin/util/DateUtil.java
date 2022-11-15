package org.kexing.management.domin.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtil {

    public static final String YYY_MM_DD = "yyyy-MM-dd";
    public static final String HH_MM = "HH:mm";

    public static String convert2String(Instant time,String format){
        if(time==null){
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format)
                .withZone(ZoneId.systemDefault()).withLocale(Locale.CHINA);
        return formatter.format(time);
    }

    public static long diffMinute(Instant start,Instant end){
        LocalDateTime startDateTime = start.atZone(ZoneId.systemDefault()).toLocalDateTime().withSecond(0);
        LocalDateTime endDateTime = end.atZone(ZoneId.systemDefault()).toLocalDateTime().withSecond(0);
        return Duration.between(startDateTime,endDateTime).toMinutes();
    }

    public static String getWeek(Instant time){
        DayOfWeek dayOfWeek = LocalDateTime.ofInstant(time,ZoneId.systemDefault()).getDayOfWeek();
        switch (dayOfWeek){
            case MONDAY:
                return "星期一";
            case TUESDAY:
                return "星期二";
            case WEDNESDAY:
                return "星期三";
            case THURSDAY:
                return "星期四";
            case FRIDAY:
                return "星期五";
            case SATURDAY:
                return "星期六";
            case SUNDAY:
                return "星期天";
        }
        return "";
    }

}
