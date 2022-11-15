package org.kexing.management.domin.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kexing.management.domin.model.mysql.robot.PatrolRule;

import java.util.List;

/**
 * @author lh
 */
public class RobotRuleEvent {
  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class AddEvent {
    private PatrolRule addedPatrolRule;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class DeleteEvent {
    private PatrolRule deletedPatrolRule;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UpdateEvent {
    private List<PatrolRule.TaskStartTime> oldTaskStartTimes;
    private PatrolRule updatedPatrolRule;
  }
}
