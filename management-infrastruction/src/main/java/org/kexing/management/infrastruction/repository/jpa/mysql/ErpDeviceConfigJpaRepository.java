package org.kexing.management.infrastruction.repository.jpa.mysql;

import org.kexing.management.domin.model.mysql.ErpDeviceConfig;
import org.kexing.management.domin.repository.mysql.ErpDeviceConfigRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/** @author lh */
public interface ErpDeviceConfigJpaRepository
    extends JpaRepository<ErpDeviceConfig, Long>, ErpDeviceConfigRepository {}
