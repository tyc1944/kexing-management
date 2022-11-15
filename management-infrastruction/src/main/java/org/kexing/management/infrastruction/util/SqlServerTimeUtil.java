package org.kexing.management.infrastruction.util;

import java.time.Instant;
import java.time.LocalTime;

/** @author lh */
public class SqlServerTimeUtil {
  public static Instant columnTimeAddToDate(Instant date,Integer time) {
    if (date != null) {
      if (time != null) {
        Long second = SqlServerTimeUtil.convertTimeColumnToSecond(time);
        if (second != null) {
          return date.plusSeconds(second);
        }
      }
    }
    return date;
  }

  public static Long convertTimeColumnToSecond(int time) {
    if (time > 0 && String.valueOf(time).length() <= 4) {
      StringBuilder hourMinStringBuilder = new StringBuilder(String.valueOf(time));
      if (hourMinStringBuilder.length() < 4) {
        int currentLength = hourMinStringBuilder.length();
        for (int i = 0; i < 4 - currentLength; i++) {
          hourMinStringBuilder.insert(0, "0");
        }
      }
      return Long.parseLong(hourMinStringBuilder.substring(2, 4)) * 60
          + Long.parseLong(hourMinStringBuilder.substring(0, 2)) * 60 * 60;
    }
    return null;
  }

  public static LocalTime convert2Time(int time){
    String hms = String.format("%04d",time);
    return LocalTime.of(Integer.valueOf(hms.substring(0,2)),Integer.valueOf(hms.substring(2,4)),0);
  }

  public static int convert2int(LocalTime time){
    return Integer.valueOf(String.valueOf(time.getHour()).concat(String.valueOf(String.format("%02d",time.getMinute()))));
  }
}
