package org.kexing.management.infrastruction.repository.jpa.mysql;

import org.kexing.management.domin.model.mysql.IotErpDeviceBindMap;
import org.kexing.management.domin.repository.mysql.IotErpDeviceBindMapRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/** @author lh */
public interface IotErpDeviceBindMapJpaRepository
    extends JpaRepository<IotErpDeviceBindMap, Long>, IotErpDeviceBindMapRepository {}
