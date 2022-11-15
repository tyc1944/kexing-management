package org.kexing.management.infrastruction.service;

import com.yunmo.domain.common.Events;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.kexing.management.domin.event.RobotRuleEvent;
import org.kexing.management.domin.model.mysql.robot.PatrolRule;
import org.kexing.management.domin.repository.mysql.PatrolRuleRepository;
import org.kexing.management.infrastruction.util.PatrolRobotCommandUtil;
import org.kexing.management.infrastruction.util.PatrolRobotConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.kexing.management.domin.model.mysql.robot.PatrolRule.PatrolType.REPEAT_INSPECTION;

/**
 * @author lh
 */
@Service
public class ConvertPatrolRuleToDeviceService {
  @Autowired private PatrolRuleRepository patrolRuleRepository;

  @EventListener(value = {RobotRuleEvent.UpdateEvent.class, RobotRuleEvent.DeleteEvent.class})
  @SneakyThrows
  public void sendDeviceConfig() {
    PatrolRobotConfigUtil.RobotRegularPatrolConfig robotRegularPatrolConfig = getDeviceConfig();
    Events.post(robotRegularPatrolConfig);
  }

  private PatrolRobotConfigUtil.RobotRegularPatrolConfig getDeviceConfig() {
    List<PatrolRule> patrolRuleList = patrolRuleRepository.findByPatrolType(REPEAT_INSPECTION);
    PatrolRobotConfigUtil.RobotRegularPatrolConfig robotRegularPatrolConfig =
        new PatrolRobotConfigUtil.RobotRegularPatrolConfig();

    for (PatrolRule patrolRule : patrolRuleList) {
      if (CollectionUtils.isNotEmpty(patrolRule.getTaskStartTime())) {
        for (PatrolRule.TaskStartTime taskStartTime : patrolRule.getTaskStartTime()) {
          robotRegularPatrolConfig.add(
              taskStartTime, String.valueOf(patrolRule.getRobotPatrolLine().getPatrolNum()));
        }
      }
    }
    return robotRegularPatrolConfig;
  }

  @EventListener
  @SneakyThrows
  public void sendDeviceConfigOrCommand(RobotRuleEvent.AddEvent addEvent) {
    PatrolRule addedPatrolRule = addEvent.getAddedPatrolRule();

    if (addedPatrolRule.getPatrolType() == REPEAT_INSPECTION) {
      sendDeviceConfig();
    } else {
      String patrolNum = addedPatrolRule.getRobotPatrolLine().getPatrolNum();
      var taskCommand = PatrolRobotCommandUtil.createTaskCommand(patrolNum);
      Events.post(taskCommand);
    }
  }
}
