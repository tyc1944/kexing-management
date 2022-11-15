package org.kexing.management.api_reset.resource;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.kexing.management.domin.model.mysql.robot.RobotPatrol;
import org.kexing.management.domin.model.mysql.robot.RobotPatrolLine;
import org.kexing.management.domin.model.mysql.robot.value.RobotPatrolValue;
import org.kexing.management.domin.repository.mysql.RobotPatrolLineRepository;
import org.kexing.management.domin.repository.mysql.RobotPatrolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;

import static org.kexing.management.domin.model.mysql.robot.RobotPatrol.TaskStatus.END;
import static org.kexing.management.domin.model.mysql.robot.RobotPatrol.TaskStatus.FORCED_END;

/**
 * @author lh
 */
@RestController
@RequestMapping("/api/robots/{id}/patrols")
@RequiredArgsConstructor
@Tag(name = "机器人巡检资源")
public class RobotPatrolResource {

  @Autowired RobotPatrolRepository robotPatrolRepository;

  @Autowired RobotPatrolLineRepository patrolLineRepository;

  @PostMapping
  public RobotPatrol create(
      @PathVariable("id") Long robotId, @Valid @RequestBody RobotPatrolValue value) {
    RobotPatrolLine robotPatrolLine =
        patrolLineRepository.findByPatrolNum(value.getPatrolNum()).orElse(null);

    RobotPatrol robotPatrol = value.create();
    robotPatrol.setRobotId(robotId).setRobotPatrolLine(robotPatrolLine).setStartTime(Instant.now());
    robotPatrol.setRobotPatrolLine(robotPatrolLine);

    return robotPatrolRepository.save(robotPatrol);
  }

  @PatchMapping("/{patrolId}")
  public void update(
      @PathVariable("id") Long robotId,
      @PathVariable("patrolId") RobotPatrol robotPatrol,
      @Valid @RequestBody RobotPatrolValue value) {
    value.patchTo(robotPatrol);

    if (value.getTaskStatus() == FORCED_END || value.getTaskStatus() == END) {
      robotPatrol.setEndTime(Instant.now());
    }

    robotPatrolRepository.save(robotPatrol);
  }
}
