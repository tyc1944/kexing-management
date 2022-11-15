package org.kexing.management.infrastruction.repository.jpa.mysql;

import org.kexing.management.domin.model.mysql.AlertWorkOrderConfig;
import org.kexing.management.domin.repository.mysql.AlertWorkOrderConfigRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertWorkOrderConfigJpaRepository
        extends JpaRepository<AlertWorkOrderConfig, Long>, AlertWorkOrderConfigRepository {
}
