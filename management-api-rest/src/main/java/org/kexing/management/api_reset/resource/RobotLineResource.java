package org.kexing.management.api_reset.resource;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.kexing.management.domin.model.mysql.robot.PatrolRule;
import org.kexing.management.domin.model.mysql.robot.RobotPatrolLine;
import org.kexing.management.infrastruction.repository.jpa.mysql.RobotPatrolLineJpaRepository;
import org.kexing.management.infrastruction.repository.mybatis.mysql.RobotPatrolLineQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lh
 */
@RequestMapping("/api/robots/{id}/lines")
@RestController
@RequiredArgsConstructor
@Tag(name = "机器巡检路线资源")
public class RobotLineResource {
  @Autowired
  RobotPatrolLineJpaRepository robotPatrolLineRepository;

  @Autowired
  RobotPatrolLineQueryMapper robotPatrolLineQueryMapper;
  @GetMapping
  public List<RobotPatrolLine> get(@PathVariable("id") Long robotId, @RequestParam(value="patrolType",required=false) String patrolType){
    return robotPatrolLineQueryMapper.findByRobotId(robotId,patrolType);
  }
}
