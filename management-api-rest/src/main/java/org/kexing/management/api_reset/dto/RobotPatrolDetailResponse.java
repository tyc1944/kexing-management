package org.kexing.management.api_reset.dto;

import com.yunmo.iot.domain.core.Device;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.kexing.management.domin.model.mysql.robot.RobotPatrol;
import org.kexing.management.domin.model.mysql.robot.RobotPatrolLine;

/**
 * @author lh
 */
@Setter
@Getter
@Schema(description = "巡检机器人执行记录详情")
@Builder
public class RobotPatrolDetailResponse {
  private RobotPatrol robotPatrol;
  private String deviceName;
}
