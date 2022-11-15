package org.kexing.management.infrastruction.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.kexing.management.domin.model.mysql.robot.RobotPatrol;
import org.kexing.management.domin.model.mysql.robot.PatrolRule;
import org.kexing.management.infrastruction.query.BaseParam;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * @author lh
 */
@Setter
@Getter
@Schema(description = "巡检机器人巡检记录")
public class ListInspectRobotInspectionRequest extends BaseParam {

  private RobotPatrol.TaskStatus taskStatus;

  private PatrolRule.PatrolType patrolType;

  @Schema(description = "时间范围::[开始,结束]")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Size(min = 2, max = 2)
  private LocalDate[] dateRange;
}
