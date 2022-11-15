package org.kexing.management.infrastruction.repository.jpa.mysql;

import org.kexing.management.domin.model.mysql.robot.RobotPatrolLine;
import org.kexing.management.domin.repository.mysql.RobotPatrolLineRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RobotPatrolLineJpaRepository extends JpaRepository<RobotPatrolLine, Long>, RobotPatrolLineRepository {
}
