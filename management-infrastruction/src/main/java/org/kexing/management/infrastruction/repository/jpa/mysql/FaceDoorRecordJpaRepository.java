package org.kexing.management.infrastruction.repository.jpa.mysql;

import com.yunmo.haikang.device.api.entity.FaceDoorRecord;
import org.kexing.management.domin.repository.mysql.FaceDoorRecordRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaceDoorRecordJpaRepository extends JpaRepository<FaceDoorRecord, Long>, FaceDoorRecordRepository {
    List<FaceDoorRecord> findByOrganizationId(Long organizationId, Pageable pageable);
}
