package org.kexing.management.domin.repository.mysql;

import com.yunmo.attendance.api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findByStaffId(long staffId);

    Optional<Member> findByStaffIdOrderByCreatedDate(Long staffId);
}
