package org.kexing.management.infrastruction.util;

import lombok.extern.slf4j.Slf4j;
import org.kexing.management.domin.model.mysql.robot.PatrolRule;
import org.kexing.management.domin.model.mysql.robot.RobotPatrol;
import org.kexing.management.domin.repository.mysql.RobotPatrolRepository;
import org.kexing.management.domin.util.RobotPatrolUtil;
import org.kexing.management.infrastruction.repository.jpa.mysql.RobotPatrolJpaRepository;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

import static org.kexing.management.domin.model.mysql.robot.PatrolRule.PatrolType.REPEAT_INSPECTION;
import static org.kexing.management.domin.util.RobotPatrolUtil.getPatrolTime;

/**
 * @author lh
 */
@Slf4j
@Component
public class ScheduleUtil {

  @Autowired Scheduler scheduler;

  public static String getJobKeyName(String... strings) {
    return String.join("_", strings);
  }

  public static String getJobKeyGroup(String... strings) {
    return getJobKeyName(strings);
  }

  public static Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
    return TriggerBuilder.newTrigger()
        .forJob(jobDetail)
        .withIdentity(jobDetail.getKey().getName(), jobDetail.getKey().getGroup())
        .startAt(Date.from(startAt.toInstant()))
        .withSchedule(
            SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionIgnoreMisfires())
        .build();
  }

  public static Trigger buildJobTrigger(JobDetail jobDetail, String cronExpression) {
    return TriggerBuilder.newTrigger()
        .forJob(jobDetail)
        .withIdentity(jobDetail.getKey().getName(), jobDetail.getKey().getGroup())
        .withSchedule(
            CronScheduleBuilder.cronSchedule(cronExpression)
                .withMisfireHandlingInstructionIgnoreMisfires()
                .inTimeZone(TimeZone.getTimeZone(ZoneUtil.SHANGHAI_ZONE_ID)))
        .build();
  }

  /** 校验巡检规则是否执行 */
  @Component
  @DisallowConcurrentExecution
  public static final class AddPatrolRuleNoExecuteJob extends QuartzJobBean {

    public static final Duration DELAY_DURATION_CHECK_RUN_JOB = Duration.of(5, ChronoUnit.MINUTES);
    @Autowired RobotPatrolJpaRepository robotPatrolJpaRepository;
    @Autowired RobotPatrolRepository robotPatrolRepository;

    public static JobKey getScheduleJobKey(
        Long robotId, PatrolRule.TaskStartTime taskStartTime, PatrolRule.PatrolType patrolType) {
      return new JobKey(
          getJobKeyName(
              taskStartTime.getWeekDay().toString(),
              getPatrolTime(taskStartTime.getEveryDayTime())),
          getJobKeyGroup(String.valueOf(robotId), patrolType.toString()));
    }

    public static JobKey getScheduleJobKey(
        Long robotId, Instant startTime, PatrolRule.PatrolType patrolType) {
      return new JobKey(
          getJobKeyName(startTime.toString()),
          getJobKeyGroup(String.valueOf(robotId), patrolType.toString()));
    }

    public static JobDetail getJobDetail(JobDataMap jobDataMap, JobKey jobKey) {
      return JobBuilder.newJob(ScheduleUtil.AddPatrolRuleNoExecuteJob.class)
          .withIdentity(jobKey)
          .usingJobData(jobDataMap)
          .storeDurably()
          .build();
    }

    public static JobDataMap getCommonJobDataMap(
        PatrolRule.PatrolType patrolType, String patrolNum) {
      JobDataMap jobDataMap = new JobDataMap();
      jobDataMap.put("robotId", RobotPatrolUtil.DEFAULT_PATROL_ROBOT_ID);
      jobDataMap.put("patrolNum", patrolNum);
      jobDataMap.put("patrolType", patrolType);
      return jobDataMap;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
      Instant scheduledFireTime = context.getScheduledFireTime().toInstant();
      log.error(context.toString());
      JobDataMap jobDataMap = context.getMergedJobDataMap();
      Long robotId = jobDataMap.getLong("robotId");
      String patrolNum = jobDataMap.getString("patrolNum");
      PatrolRule.PatrolType patrolType = (PatrolRule.PatrolType) jobDataMap.get("patrolType");

      Instant startTime = scheduledFireTime.minus(DELAY_DURATION_CHECK_RUN_JOB);

      Optional<RobotPatrol> robotPatrol =
          robotPatrolJpaRepository
              .findTopByPatrolNumAndPatrolTypeAndAssociatedPatrolRuleAndCreatedDateBetweenOrderByCreatedDateAsc(
                  patrolNum, patrolType, false, startTime, scheduledFireTime);
      robotPatrol.ifPresentOrElse(
          item -> {
            item.setAssociatedPatrolRule(true);
            robotPatrolJpaRepository.save(item);
          },
          () -> {
            RobotPatrol newRobotPatrol =
                RobotPatrol.builder()
                    .patrolNum(patrolNum)
                    .robotId(robotId)
                    .patrolType(patrolType)
                    .associatedPatrolRule(true)
                    .taskStatus(RobotPatrol.TaskStatus.NO_RUNNING)
                    .build();
            robotPatrolRepository.save(newRobotPatrol);
          });
    }
  }
}
