package org.kexing.management.infrastruction.repository.jpa.mysql;

import org.kexing.management.domin.model.mysql.Message;
import org.kexing.management.domin.repository.mysql.MessageRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/** @author lh */
public interface MessageJpaRepository
    extends JpaRepository<Message, Long>, MessageRepository {}
