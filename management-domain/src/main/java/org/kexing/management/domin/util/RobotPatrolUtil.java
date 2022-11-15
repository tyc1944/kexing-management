package org.kexing.management.domin.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lh
 */
public interface  RobotPatrolUtil {
    public static final long DEFAULT_PATROL_ROBOT_ID =1L;

  public static String getPatrolTime(LocalTime patrolTimeLocalTime) {
      return patrolTimeLocalTime.format(DateTimeFormatter.ofPattern("HH:mm"));
  }


}
