package org.kexing.management.domin.repository.mysql;

import com.yunmo.attendance.api.entity.Organization;
import com.yunmo.attendance.api.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
