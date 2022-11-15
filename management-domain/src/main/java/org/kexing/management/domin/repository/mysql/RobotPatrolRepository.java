package org.kexing.management.domin.repository.mysql;

import com.yunmo.domain.common.EntityRepository;
import org.kexing.management.domin.model.mysql.robot.PatrolRule;
import org.kexing.management.domin.model.mysql.robot.RobotPatrol;

import java.time.Instant;
import java.util.Optional;

public interface RobotPatrolRepository extends EntityRepository<RobotPatrol, Long> {

  Optional<RobotPatrol>
      findTopByPatrolNumAndPatrolTypeAndAssociatedPatrolRuleAndCreatedDateBetweenOrderByCreatedDateAsc(
          String patrolNum,
          PatrolRule.PatrolType patrolType,
          boolean associatedPatrolRule,
          Instant startTimeStart,
          Instant startTimeEnd);
}
