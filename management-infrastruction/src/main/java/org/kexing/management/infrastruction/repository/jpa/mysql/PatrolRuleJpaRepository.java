package org.kexing.management.infrastruction.repository.jpa.mysql;

import org.kexing.management.domin.model.mysql.AlertWorkOrderConfig;
import org.kexing.management.domin.model.mysql.robot.PatrolRule;
import org.kexing.management.domin.repository.mysql.AlertWorkOrderConfigRepository;
import org.kexing.management.domin.repository.mysql.PatrolRuleRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatrolRuleJpaRepository
        extends JpaRepository<PatrolRule, Long>, PatrolRuleRepository {
}
