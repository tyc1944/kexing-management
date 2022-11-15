package org.kexing.management.domin.repository.mysql;

import com.yunmo.domain.common.EntityRepository;
import org.kexing.management.domin.model.mysql.Vehicle;

public interface VehicleRepository extends EntityRepository<Vehicle, Long> {

    Vehicle findByPlateNumber(String plateNumber);

    Vehicle findByStaffId(long staffId);
}
