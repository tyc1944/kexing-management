package org.kexing.management.domin.repository.mysql;

import com.yunmo.domain.common.EntityRepository;
import org.kexing.management.domin.model.mysql.robot.PatrolRule;
import org.kexing.management.domin.model.mysql.robot.RobotPatrolLine;

import java.util.List;
import java.util.Optional;

public interface RobotPatrolLineRepository extends EntityRepository<RobotPatrolLine, Long> {
    List<RobotPatrolLine> findByRobotId(Long robotId);

    Optional<RobotPatrolLine> findByPatrolNum(String patrolNup);
}
