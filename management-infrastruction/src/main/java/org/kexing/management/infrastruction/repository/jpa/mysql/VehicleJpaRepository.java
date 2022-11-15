package org.kexing.management.infrastruction.repository.jpa.mysql;

import org.kexing.management.domin.model.mysql.Vehicle;
import org.kexing.management.domin.repository.mysql.VehicleRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleJpaRepository extends JpaRepository<Vehicle, Long>, VehicleRepository {
}
