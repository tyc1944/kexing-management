package org.kexing.management.infrastruction.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kexing.management.domin.model.mysql.robot.PatrolRule;
import org.kexing.management.domin.util.RobotPatrolUtil;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 机器人配置 工具类.
 *
 * @author lh
 */
public interface PatrolRobotConfigUtil {
  public static DayOfWeek getDayOfWeek(Instant instant, ZoneId zoneId) {
    return Instant.now().atZone(zoneId).getDayOfWeek();
  }

  /**
   *
   * 机器人巡检规则 配置
   */
  @Data
  class RobotRegularPatrolConfig {

    private List<List<RegularPatrolRobot>> week;

    {
      List<List<RegularPatrolRobot>> lists = new ArrayList<>();

      List<RegularPatrolRobot> sundayRegularPatrolRobots = new ArrayList<>();
      lists.add(sundayRegularPatrolRobots);

      List<RegularPatrolRobot> mondayRegularPatrolRobots = new ArrayList<>();
      lists.add(mondayRegularPatrolRobots);

      List<RegularPatrolRobot> tuesdayRegularPatrolRobots = new ArrayList<>();
      lists.add(tuesdayRegularPatrolRobots);

      List<RegularPatrolRobot> wednesdayRegularPatrolRobots = new ArrayList<>();
      lists.add(wednesdayRegularPatrolRobots);

      List<RegularPatrolRobot> thursdayRegularPatrolRobots = new ArrayList<>();
      lists.add(thursdayRegularPatrolRobots);

      List<RegularPatrolRobot> fridayRegularPatrolRobots = new ArrayList<>();
      lists.add(fridayRegularPatrolRobots);

      List<RegularPatrolRobot> saturdayRegularPatrolRobots = new ArrayList<>();
      lists.add(saturdayRegularPatrolRobots);
      week = lists;
    }

    public void add(PatrolRule.TaskStartTime taskStartTime, String patrolNum) {
      List<RegularPatrolRobot> oneOfWeekRegularPatrolRobots = null;
      switch (taskStartTime.getWeekDay()) {
        case MONDAY:
          oneOfWeekRegularPatrolRobots = week.get(1);
          break;
        case TUESDAY:
          oneOfWeekRegularPatrolRobots = week.get(2);
          break;
        case WEDNESDAY:
          oneOfWeekRegularPatrolRobots = week.get(3);
          break;
        case THURSDAY:
          oneOfWeekRegularPatrolRobots = week.get(4);
          break;
        case FRIDAY:
          oneOfWeekRegularPatrolRobots = week.get(5);
          break;
        case SATURDAY:
          oneOfWeekRegularPatrolRobots = week.get(6);
          break;
        case SUNDAY:
          oneOfWeekRegularPatrolRobots = week.get(0);
          break;
      }
      oneOfWeekRegularPatrolRobots.add(
          RegularPatrolRobot.builder()
              .patrol_num(patrolNum)
              .patrol_time(RobotPatrolUtil.getPatrolTime(taskStartTime.getEveryDayTime()))
              .build());
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegularPatrolRobot {
      private String patrol_num;
      private String patrol_time;
    }
  }
}
