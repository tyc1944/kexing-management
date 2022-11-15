package org.kexing.management.api_reset.schedule;

import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.kexing.management.domin.event.RobotRuleEvent;
import org.kexing.management.domin.model.mysql.robot.PatrolRule;
import org.kexing.management.domin.model.mysql.robot.RobotPatrolLine;
import org.kexing.management.domin.util.RobotPatrolUtil;
import org.kexing.management.infrastruction.util.ScheduleUtil;
import org.kexing.management.infrastruction.util.ZoneUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.kexing.management.domin.model.mysql.robot.PatrolRule.PatrolType.REPEAT_INSPECTION;
import static org.kexing.management.domin.util.RobotPatrolUtil.DEFAULT_PATROL_ROBOT_ID;
import static org.kexing.management.infrastruction.util.ScheduleUtil.AddPatrolRuleNoExecuteJob.*;
import static org.kexing.management.infrastruction.util.ScheduleUtil.AddPatrolRuleNoExecuteJob.DELAY_DURATION_CHECK_RUN_JOB;

/**
 * @author lh
 */
@Component
public class PatrolRuleSchedule {

  @Autowired Scheduler scheduler;

  @EventListener
  public void onAdd(RobotRuleEvent.AddEvent addEvent) {
    addScheduleTask(addEvent.getAddedPatrolRule());
  }

  @EventListener
  public void onDelete(RobotRuleEvent.DeleteEvent deleteEvent) {
    PatrolRule patrolRule = deleteEvent.getDeletedPatrolRule();
    removeTask(RobotPatrolUtil.DEFAULT_PATROL_ROBOT_ID, patrolRule.getTaskStartTime(),patrolRule.getPatrolType());
  }

  @EventListener
  public void onUpdate(RobotRuleEvent.UpdateEvent updateEvent) {
    updateTask(updateEvent.getOldTaskStartTimes(), updateEvent.getUpdatedPatrolRule());
  }

  @SneakyThrows
  private void removeTask(Long robotId, List<PatrolRule.TaskStartTime> taskStartTimes, PatrolRule.PatrolType patrolType) {
    for (PatrolRule.TaskStartTime taskStartTime : taskStartTimes) {
      scheduler.deleteJob(getScheduleJobKey(robotId, taskStartTime,patrolType));
    }
  }

  @SneakyThrows
  private void updateTask(List<PatrolRule.TaskStartTime> oldTaskStartTimes,PatrolRule patrolRule) {
    oldTaskStartTimes.removeAll(patrolRule.getTaskStartTime());
    for (PatrolRule.TaskStartTime needRemoveTaskStartTime : oldTaskStartTimes) {
      scheduler.deleteJob(
              getScheduleJobKey(RobotPatrolUtil.DEFAULT_PATROL_ROBOT_ID, needRemoveTaskStartTime,patrolRule.getPatrolType()));
    }
    addScheduleTask(patrolRule);
  }


  @SneakyThrows
  public void addScheduleTask(PatrolRule patrolRule) {
    RobotPatrolLine line = patrolRule.getRobotPatrolLine();

    if (patrolRule.getPatrolType() == REPEAT_INSPECTION) {
      if (CollectionUtils.isNotEmpty(patrolRule.getTaskStartTime())) {
        for (PatrolRule.TaskStartTime taskStartTime : patrolRule.getTaskStartTime()) {
          JobDataMap jobDataMap =
                  getCommonJobDataMap(patrolRule.getPatrolType(), line.getPatrolNum());
          JobKey jobKey =
                  getScheduleJobKey(
                          DEFAULT_PATROL_ROBOT_ID,
                          taskStartTime,
                          patrolRule.getPatrolType());

          if (scheduler.checkExists(jobKey)) {
            continue;
          }

          JobDetail jobDetail = getJobDetail(jobDataMap, jobKey);

          LocalTime startLocalTime = taskStartTime.getEveryDayTime();
          DayOfWeek startDayOfWeek = taskStartTime.getWeekDay();

          LocalTime scheduleStartLocalTime =startLocalTime.plus(DELAY_DURATION_CHECK_RUN_JOB);
          DayOfWeek scheduleStartDayOfWeek = startDayOfWeek;
          if(scheduleStartLocalTime.isBefore(startLocalTime)){
            scheduleStartDayOfWeek = startDayOfWeek.plus(1);
          }

          PatrolRule.TaskStartTime  scheduleStartTime = new PatrolRule.TaskStartTime(scheduleStartDayOfWeek,scheduleStartLocalTime);

          Trigger trigger =
                  ScheduleUtil.buildJobTrigger(
                          jobDetail,
                          String.format(
                                  "0 %s %s ? * %s",
                                  scheduleStartTime.getEveryDayTime().format(DateTimeFormatter.ofPattern("mm")),
                                  scheduleStartTime.getEveryDayTime().format(DateTimeFormatter.ofPattern("HH")),
                                  scheduleStartTime.getWeekDay().toString().substring(0, 3)));

          scheduler.scheduleJob(jobDetail, trigger);
        }
      }
    } else {
      JobDataMap jobDataMap = getCommonJobDataMap(patrolRule.getPatrolType(), line.getPatrolNum());
      JobKey jobKey =
              getScheduleJobKey(
                      DEFAULT_PATROL_ROBOT_ID, Instant.now(), patrolRule.getPatrolType());

      JobDetail jobDetail = getJobDetail(jobDataMap, jobKey);

      Trigger trigger =
              ScheduleUtil.buildJobTrigger(
                      jobDetail,
                      patrolRule
                              .getCreatedDate()
                              .plus(DELAY_DURATION_CHECK_RUN_JOB)
                              .atZone(ZoneUtil.SHANGHAI_ZONE_ID));
      scheduler.scheduleJob(jobDetail, trigger);
    }
  }

}
