package org.kexing.management.infrastruction.repository.jpa.mysql;

import org.kexing.management.domin.model.mysql.ErpDeviceConfig;
import org.kexing.management.domin.model.mysql.ErpDeviceRunStatusConfig;
import org.kexing.management.domin.repository.mysql.ErpDeviceConfigRepository;
import org.kexing.management.domin.repository.mysql.ErpDeviceRunStatusConfigRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/** @author lh */
public interface ErpDeviceRunStatusConfigJpaRepository
    extends JpaRepository<ErpDeviceRunStatusConfig, Long>, ErpDeviceRunStatusConfigRepository {}
