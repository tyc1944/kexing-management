package org.kexing.management.infrastruction.repository.jpa.mysql;

import org.kexing.management.domin.model.mysql.UserAccount;
import org.kexing.management.domin.repository.mysql.UserAccountRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/** @author lh */
public interface UserAccountJpaRepository
    extends JpaRepository<UserAccount, Long>, UserAccountRepository {}
