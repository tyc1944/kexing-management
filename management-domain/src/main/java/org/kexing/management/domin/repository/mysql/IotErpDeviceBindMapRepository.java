package org.kexing.management.domin.repository.mysql;

import com.yunmo.domain.common.EntityRepository;
import org.kexing.management.domin.model.mysql.IotErpDeviceBindMap;

import java.util.List;

/** @author lh */
public interface IotErpDeviceBindMapRepository
    extends EntityRepository<IotErpDeviceBindMap, Long> {
    List<IotErpDeviceBindMap> findByErpDeviceId(String erpDeviceId);
    List<IotErpDeviceBindMap> findByIotDeviceId(long deviceId);
}
