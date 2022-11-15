package org.kexing.management.infrastruction.repository.jpa.mysql;

import com.yunmo.attendance.api.entity.Member;
import org.kexing.management.domin.repository.mysql.MemberRepository;
import org.kexing.management.domin.repository.mysql.OrganizationRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/** @author lh */
public interface MemberJpaRepository
    extends JpaRepository<Member, Long>, MemberRepository{

}
