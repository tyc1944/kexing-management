package org.kexing.management.domin.repository.mysql;

import com.yunmo.domain.common.EntityRepository;
import org.kexing.management.domin.model.mysql.DevicePictureMap;

import java.util.List;
import java.util.Optional;

/** @author lh */
public interface DevicePictureMapRepository extends EntityRepository<DevicePictureMap, Long> {
    Optional<DevicePictureMap> findByType(DevicePictureMap.Type type);
}
