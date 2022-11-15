package org.kexing.management.infrastruction.repository.jpa.mysql;

import org.kexing.management.domin.model.mysql.WorkOrder;
import org.kexing.management.domin.repository.mysql.WorkOrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/** @author lh */
public interface WorkOrderJpaRepository
    extends JpaRepository<WorkOrder, Long>, WorkOrderRepository {}
