package org.kexing.management.infrastruction.repository.jpa.mysql;

import com.yunmo.attendance.api.entity.Organization;
import com.yunmo.attendance.api.entity.Staff;
import org.kexing.management.domin.repository.mysql.OrganizationRepository;
import org.kexing.management.domin.repository.mysql.StaffRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/** @author lh */
public interface OrganizationJpaRepository
    extends JpaRepository<Organization, Long>, OrganizationRepository {}
