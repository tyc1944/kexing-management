package org.kexing.management.domin.repository.mysql;

import com.yunmo.attendance.api.entity.Organization;
import com.yunmo.domain.common.EntityRepository;
import org.kexing.management.domin.model.mysql.robot.PatrolRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatrolRuleRepository extends EntityRepository<PatrolRule, Long> {
  List<PatrolRule> findByPatrolType(PatrolRule.PatrolType patrolType);
}
