package org.kexing.management.domin.repository.mysql;

import com.yunmo.attendance.api.entity.Staff;
import com.yunmo.domain.common.EntityRepository;
import org.kexing.management.domin.model.mysql.IotErpDeviceBindMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {
}
