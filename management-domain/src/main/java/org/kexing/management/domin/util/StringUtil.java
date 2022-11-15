package org.kexing.management.domin.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class StringUtil {

    public static String getDownloadFileName(String name) {
        return URLEncoder.encode(name, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }

    public static String convertDateTime(Instant date, int time){
        LocalDate localDate = date.atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                .concat("T")
                .concat(String.format("%04d",time)).concat("00Z");
    }

    public static String convertInstant(Instant dateTime){
        LocalDateTime localDateTime = dateTime.atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")).concat("T")
                .concat(localDateTime.format(DateTimeFormatter.ofPattern("HHmmss"))).concat("Z");
    }
}
