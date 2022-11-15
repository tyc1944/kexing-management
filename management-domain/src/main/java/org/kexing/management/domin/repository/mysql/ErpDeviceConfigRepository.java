package org.kexing.management.domin.repository.mysql;

import com.yunmo.domain.common.EntityRepository;
import org.kexing.management.domin.model.mysql.ErpDeviceConfig;

import java.util.List;
import java.util.Optional;

/** @author lh */
public interface ErpDeviceConfigRepository extends EntityRepository<ErpDeviceConfig, Long> {
  Optional<ErpDeviceConfig> findByDeviceId(String deviceId);

  List<ErpDeviceConfig> findByDeviceIdStartsWithIgnoreCase(String hongxiangIdPre);
}
