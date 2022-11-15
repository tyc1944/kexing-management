package org.kexing.management.infrastruction.repository.jpa.mysql;

import org.kexing.management.domin.model.mysql.DevicePictureMap;
import org.kexing.management.domin.repository.mysql.DevicePictureMapRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/** @author lh */
public interface DevicePictureMapRepositoryImpl
    extends JpaRepository<DevicePictureMap, Long>, DevicePictureMapRepository {}
