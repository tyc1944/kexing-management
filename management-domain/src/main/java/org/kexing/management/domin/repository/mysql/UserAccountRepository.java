package org.kexing.management.domin.repository.mysql;

import com.yunmo.domain.common.EntityRepository;
import org.kexing.management.domin.model.mysql.UserAccount;

import java.util.Optional;

/** @author lh */
public interface UserAccountRepository extends EntityRepository<UserAccount, Long> {
  Optional<UserAccount> findByAccountName(String name);
  Optional<UserAccount> findByStaffId(long staffId);
}
