package org.kexing.management.infrastruction.repository.jpa.mysql;

import org.kexing.management.domin.model.mysql.robot.RobotPatrol;
import org.kexing.management.domin.repository.mysql.RobotPatrolRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RobotPatrolJpaRepository extends JpaRepository<RobotPatrol, Long>, RobotPatrolRepository {
}
